(ns chromex.ext.mojo-private
  "The chrome.mojoPrivate API provides access to the mojo modules.

     * available since Chrome 42"

  (:refer-clojure :only [defmacro defn apply declare meta let partial])
  (:require [chromex.wrapgen :refer [gen-wrap-helper]]
            [chromex.callgen :refer [gen-call-helper gen-tap-all-events-call]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro define
  "Defines a AMD module.

     |module-name| - ?
     |dependencies| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [modules] where:

     |modules| - ?

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error."
  ([module-name dependencies] (gen-call :function ::define &form module-name dependencies))
  ([module-name] `(define ~module-name :omit)))

(defmacro require-async
  "Returns a promise that will resolve to an asynchronously loaded module.

     |name| - ?"
  ([name] (gen-call :function ::require-async &form name)))

; -- convenience ------------------------------------------------------------------------------------------------------------

(defmacro tap-all-events
  "Taps all valid non-deprecated events in chromex.ext.mojo-private namespace."
  [chan]
  (gen-tap-all-events-call api-table (meta &form) chan))

; ---------------------------------------------------------------------------------------------------------------------------
; -- API TABLE --------------------------------------------------------------------------------------------------------------
; ---------------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.mojoPrivate",
   :since "42",
   :functions
   [{:id ::define,
     :name "define",
     :callback? true,
     :params
     [{:name "module-name", :type "string"}
      {:name "dependencies", :optional? true, :type "[array-of-strings]"}
      {:name "factory", :type :callback, :callback {:params [{:name "modules", :type "[array-of-anys]"}]}}]}
    {:id ::require-async, :name "requireAsync", :return-type "any", :params [{:name "name", :type "string"}]}]})

; -- helpers ----------------------------------------------------------------------------------------------------------------

; code generation for native API wrapper
(defmacro gen-wrap [kind item-id config & args]
  (apply gen-wrap-helper api-table kind item-id config args))

; code generation for API call-site
(def gen-call (partial gen-call-helper api-table))