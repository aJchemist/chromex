(ns chromex.ext.app-view-guest-internal
  "  * available since Chrome 40"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex.wrapgen :refer [gen-wrap-from-table]]
            [chromex.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro attach-frame
  "Attaches the specified url to the AppView with the provided instance ID.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [instance-id] where:

     |instance-id| - ?"
  ([] (gen-call :function ::attach-frame &form)))

(defmacro deny-request
  "Denies the embedding request made by the AppView with the provided instance ID."
  ([] (gen-call :function ::deny-request &form)))

; -- convenience ------------------------------------------------------------------------------------------------------------

(defmacro tap-all-events
  "Taps all valid non-deprecated events in this namespace."
  [chan]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (gen-tap-all-call static-config api-table (meta &form) config chan)))

; ---------------------------------------------------------------------------------------------------------------------------
; -- API TABLE --------------------------------------------------------------------------------------------------------------
; ---------------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.appViewGuestInternal",
   :since "40",
   :functions
   [{:id ::attach-frame,
     :name "attachFrame",
     :callback? true,
     :params
     [{:name "callback",
       :optional? true,
       :type :callback,
       :callback {:params [{:name "instance-id", :type "integer"}]}}]}
    {:id ::deny-request, :name "denyRequest"}]})

; -- helpers ----------------------------------------------------------------------------------------------------------------

; code generation for native API wrapper
(defmacro gen-wrap [kind item-id config & args]
  (let [static-config (get-static-config)]
    (apply gen-wrap-from-table static-config api-table kind item-id config args)))

; code generation for API call-site
(defn gen-call [kind item src-info & args]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (apply gen-call-from-table static-config api-table kind item src-info config args)))