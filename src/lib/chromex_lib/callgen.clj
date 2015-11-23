(ns chromex-lib.callgen
  (:require [chromex-lib.support :refer [valid-api-version? emit-api-version-warning emit-deprecation-warning
                                         get-wrap-symbol get-api-id get-item-by-id get-src-info]]))

; ---------------------------------------------------------------------------------------------------------------------------

(defn gen-wrap-call [static-config src-info api-table descriptor config & args]
  (let [{:keys [id]} descriptor
        api (get-api-id api-table descriptor)
        since (or (:since descriptor) (:since api-table))
        until (or (:until descriptor) (:until api-table))
        valid? (valid-api-version? static-config since until)
        deprecation-info (or (:deprecated descriptor) (:deprecated api-table))
        deprecated? (not (nil? deprecation-info))
        wrap-sym (get-wrap-symbol id)
        wrap-call `(~wrap-sym ~config ~@args)]
    (if-not valid? (emit-api-version-warning static-config src-info api))
    (if deprecated? (emit-deprecation-warning static-config src-info api deprecation-info))
    (with-meta wrap-call {:deprecated? deprecated? :valid? valid?})))

(defn gen-call-from-group [collection tag singular static-config api-table item-id src-info config & args]
  (if-let [descriptor (get-item-by-id item-id (collection api-table))]
    (apply gen-wrap-call static-config src-info api-table (assoc descriptor tag true) config args)
    (assert false (str "unable to find " singular " with id " item-id "in:\n" api-table))))

(defn gen-property-call [static-config api-table item-id src-info config & args]
  (apply gen-call-from-group :properties :property? "property" static-config api-table item-id src-info config args))

(defn gen-function-call [static-config api-table item-id src-info config & args]
  (apply gen-call-from-group :functions :function "function" static-config api-table item-id src-info config args))

(defn gen-event-call [static-config api-table item-id src-info config & args]
  (apply gen-call-from-group :events :event? "event" static-config api-table item-id src-info config args))

(defn gen-call-from-table [static-config api-table kind item-id form config & args]
  (let [src-info (get-src-info form)]
    (case kind
      :function (apply gen-function-call static-config api-table item-id src-info config args)
      :property (apply gen-property-call static-config api-table item-id src-info config args)
      :event (apply gen-event-call static-config api-table item-id src-info config args))))

; ---------------------------------------------------------------------------------------------------------------------------

(defn gen-tap-all-call [static-config api-table form config chan]
  (let [src-info (get-src-info form)
        chan-sym (gensym "chan")
        config-sym (gensym "config")
        event-ids (map :id (:events api-table))
        static-config-no-warnings (assoc static-config :silence-compilation-warnings true)
        gen-event-call-for-id #(gen-event-call static-config-no-warnings api-table % src-info config-sym chan-sym)
        all-tap-events-calls (map gen-event-call-for-id event-ids)
        valid-and-non-deprecated? (fn [call]
                                    (let [{:keys [valid? deprecated?]} (meta call)]
                                      (and valid? (not deprecated?))))
        tap-events-calls (filter valid-and-non-deprecated? all-tap-events-calls)]
    `(let [~chan-sym ~chan
           ~config-sym ~config]
       ~@tap-events-calls)))
