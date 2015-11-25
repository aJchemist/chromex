(ns chromex-lib.wrapgen
  (:require [chromex-lib.support :refer [gen-logging-if-verbose print-warning get-item-by-id get-api-id print-debug]]
            [clojure.string :as string]))

; This file is responsible for generating code wrapping Chrome API calls.
; Each Chrome API method will get generated one representing ClojureScript stub method (stubs have star postfix).
;
; The gen-wrap-from-table must be called at compile time from a macro and it generates code in 3 layers:
;
;   1) generates code converting callbacks to channels (gen-callback-function-wrap)
;   2) generates code performing marshalling (gen-marshalling)
;   3) generates code performing logging (gen-logging-if-verbose)
;   4) generates code performing actual call/access of native Chrome API (gen-api-access-or-call)
;
; Some implementation notes:
;
; * Generated code slightly differs for different API method types (property, function, event).
; * Marshalling has to deal not only with input parameters, return value, but also with callbacks by wrapping their arguments.
; * Logging has to be performed not only before actual Chrome API call, but also in callbacks (wrap-callback-with-logging).
; * Generated code must use string names when doing Javascript interop to be compatible with advanced compilation:
;     https://github.com/binaryage/chromex/#advanced-mode-compilation
; * Chrome API supports optional arguments, but we rely on argument postions for marshalling. That is why we introduced
;   a special parameter value :omit, which marks arguments which should be omitted from final native API call.
;   omiting arguments is done during runtime, see method `prepare-final-args`.
;   Note: for convenience we generate arities of API methods with trailing optional arguments omitted,
;         for exmaple see `connect` macro in https://github.com/binaryage/chromex/blob/master/src/exts/chromex/runtime.clj
;
; ---------------------------------------------------------------------------------------------------------------------------

; -- hooks in runtime config  -----------------------------------------------------------------------------------------------

(defn make-callback-fn [config chan]
  `(let [config# ~config
         callback-fn-factory# (:callback-fn-factory config#)]
     (assert (and callback-fn-factory# (fn? callback-fn-factory#))
             (str "invalid :callback-fn-factory in chromex config\n"
                  "config: " config#))
     (callback-fn-factory# config# ~chan)))

(defn make-callback-channel [config]
  `(let [config# ~config
         callback-channel-factory# (:callback-channel-factory config#)]
     (assert (and callback-channel-factory# (fn? callback-channel-factory#))
             (str "invalid :callback-channel-factory in chromex config\n"
                  "config: " config#))
     (callback-channel-factory# config#)))

(defn make-event-fn [config event-id chan]
  `(let [config# ~config
         event-fn-factory# (:event-fn-factory config#)]
     (assert (and event-fn-factory# (fn? event-fn-factory#))
             (str "invalid :event-fn-factory in chromex config\n"
                  "config: " config#))
     (event-fn-factory# config# ~event-id ~chan)))

; ---------------------------------------------------------------------------------------------------------------------------

(defn wrap-callback-with-logging [static-config label api config [callback-sym callback-info]]
  (let [{:keys [params]} callback-info
        param-syms (map #(gensym (str "cb-param-" (:name %))) params)]
    `(fn [~@param-syms]
       ~(apply gen-logging-if-verbose static-config config label api param-syms)
       (~callback-sym ~@param-syms))))

(defn wrap-callback-args-with-logging [static-config api config args params]
  (assert (= (count params) (count args))
          (str "a mismatch between parameters and arguments passed into wrap-callback-args-with-logging\n"
               "api: " api "\n"
               "params: " params "\n"
               "args: " args))
  (let [pairs (partition 2 (interleave args (map :callback-info params)))]
    (for [[callback-sym callback-info] pairs]
      (if callback-info
        (wrap-callback-with-logging static-config "callback:" api config [callback-sym callback-info])
        callback-sym))))

; ---------------------------------------------------------------------------------------------------------------------------

(defn gen-api-access-or-call [static-config api-table descriptor config & args]
  (let [{:keys [namespace]} api-table
        {:keys [name params property?]} descriptor
        api (get-api-id api-table descriptor)
        namespace-elements (string/split namespace #"\.")
        wrapped-args (wrap-callback-args-with-logging static-config api config args params)
        param-names (map :name params)
        param-optionalities (map :optional? params)
        arg-descriptors (vec (map vec (partition 3 (interleave wrapped-args param-names param-optionalities))))
        operation (if property? "accessing:" "calling:")
        final-args-sym (gensym "final-args")
        ns-sym (gensym "ns")
        target-sym (gensym "target")
        call-info (str "to " api)]
    `(let [~final-args-sym (chromex-lib.support/prepare-final-args ~arg-descriptors ~call-info)
           ~ns-sym (chromex-lib.support/oget js/window ~@namespace-elements)
           ~target-sym (chromex-lib.support/oget ~ns-sym ~name)]
       ~(apply gen-logging-if-verbose static-config config operation api args)
       ~(if property?
          target-sym
          `(.apply ~target-sym ~ns-sym ~final-args-sym)))))

; ---------------------------------------------------------------------------------------------------------------------------

(defn marshall [static-config & args]
  (let [{:keys [gen-marshalling debug-marshalling]} static-config]
    (assert (and gen-marshalling (fn? gen-marshalling))
            (str "invalid ::gen-marshalling in static-config\n"
                 "static-config: " static-config))
    (let [marshalled-code (apply gen-marshalling args)]
      (if debug-marshalling
        (print-debug (str "marshalling request " args " => " marshalled-code)))
      marshalled-code)))

(defn marshall-callback-param [static-config api [callback-param-sym type]]
  (marshall static-config :from-chrome api type callback-param-sym))

(defn marshall-callback [static-config api [callback-sym callback-info]]
  (let [{:keys [params]} callback-info
        param-syms (map #(gensym (:name %)) params)
        param-types (map :type params)
        params (partition 2 (interleave param-syms param-types))
        marshalled-params (map (partial marshall-callback-param static-config api) params)]
    (if (empty? param-syms)
      callback-sym
      `(fn [~@param-syms]
         (~callback-sym ~@marshalled-params)))))

(defn marshall-result [static-config api [result-sym type]]
  (marshall static-config :from-chrome api type result-sym))

(defn marshall-param [static-config api [sym type]]
  (let [temp-sym (gensym)]
    `(let [~temp-sym ~sym]
       (if (cljs.core/keyword-identical? ~temp-sym :omit)
         :omit
         ~(marshall static-config :to-chrome api type temp-sym)))))

(defn marshall-function-param [static-config api [arg param]]
  (let [{:keys [name type]} param]
    (assert name (str "parameter has missing 'name': " api " " param))
    (assert type (str "parameter has missing 'type': " api " " param))
    (if (= type :callback)
      (marshall-callback static-config (str api ".callback") [arg (:callback param)])
      (marshall-param static-config api [arg type]))))

(defn marshall-function-params [static-config api args params]
  (assert (= (count params) (count args))
          (str "a mismatch between parameters and arguments passed into marshall-function-params\n"
               "api: " api "\n"
               "args: " args
               "params:" params))
  (let [pairs (partition 2 (interleave args params))]
    (for [pair pairs]
      (marshall-function-param static-config api pair))))

(defn gen-marshalling [static-config api-table descriptor config & args]
  (let [api (get-api-id api-table descriptor)
        {:keys [params return-type]} descriptor
        marshalled-params (marshall-function-params static-config api args params)
        param-syms (map #(gensym (:name %)) params)
        marshalling (interleave param-syms marshalled-params)
        result-sym (gensym "result")]
    `(let [~@marshalling
           ~result-sym ~(apply gen-api-access-or-call static-config api-table descriptor config param-syms)]
       ~(marshall-result static-config api [result-sym return-type]))))

; ---------------------------------------------------------------------------------------------------------------------------

(defn gen-event [static-config api-table descriptor config chan & args]
  (let [api (get-api-id api-table descriptor)
        event-id (:id descriptor)
        event-fn-sym (gensym "event-fn")
        handler-fn-sym (gensym "handler-fn")
        logging-fn-sym (gensym "logging-fn")
        event-sym (gensym "event-obj")
        event-path (string/split api #"\.")]
    `(let [~event-fn-sym ~(make-event-fn config event-id chan)
           ~handler-fn-sym ~(marshall-callback static-config (str api ".handler") [event-fn-sym descriptor])
           ~logging-fn-sym ~(wrap-callback-with-logging static-config "event:" api config [handler-fn-sym descriptor])
           ~event-sym (chromex-lib.support/oget js/window ~@event-path)
           result# (chromex-lib.chrome-event-subscription/make-chrome-event-subscription ~event-sym ~logging-fn-sym ~chan)]
       (chromex-lib.protocols/subscribe! result# ~@args)                                                                      ; additional args may specify filtered events, see https://developer.chrome.com/extensions/events#filtered
       result#)))

; ---------------------------------------------------------------------------------------------------------------------------

(defn gen-callback-function-wrap [static-config api-table descriptor config & args]
  (let [chan-sym (gensym "chan")]
    `(let [~chan-sym ~(make-callback-channel config)]
       ~(apply gen-marshalling static-config api-table descriptor config (concat args [(make-callback-fn config chan-sym)]))
       ~chan-sym)))

(defn gen-plain-function-wrap [static-config api-table descriptor config & args]
  (apply gen-marshalling static-config api-table descriptor config args))

(defn gen-function-wrap [static-config api-table item-id config & args]
  (let [descriptor (get-item-by-id item-id (:functions api-table))
        _ (assert descriptor (str "unable to find function with id " item-id "in:\n" api-table))
        tagged-descriptior (assoc descriptor :function? true)]
    (if (:callback? descriptor)
      (apply gen-callback-function-wrap static-config api-table tagged-descriptior config args)
      (apply gen-plain-function-wrap static-config api-table tagged-descriptior config args))))

(defn gen-property-wrap [static-config api-table item-id config & args]
  (let [descriptor (get-item-by-id item-id (:properties api-table))
        _ (assert descriptor (str "unable to find property with id " item-id "in:\n" api-table))
        tagged-descriptor (assoc descriptor :property? true)]
    (apply gen-marshalling static-config api-table tagged-descriptor config args)))

(defn gen-event-wrap [static-config api-table item-id config & args]
  (let [descriptor (get-item-by-id item-id (:events api-table))
        _ (assert descriptor (str "unable to find event with id " item-id "in:\n" api-table))
        tagged-descriptor (assoc descriptor :event? true)]
    (apply gen-event static-config api-table tagged-descriptor config args)))

; ---------------------------------------------------------------------------------------------------------------------------

(defn gen-wrap-from-table [static-config api-table kind item-id config & args]
  (case kind
    :function (apply gen-function-wrap static-config api-table item-id config args)
    :property (apply gen-property-wrap static-config api-table item-id config args)
    :event (apply gen-event-wrap static-config api-table item-id config args)))