(ns chromex.app.dashboard-private
  "  * available since Chrome 46"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex.wrapgen :refer [gen-wrap-from-table]]
            [chromex.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro show-permission-prompt-for-delegated-install
  "Shows a permission prompt for the given extension, for installing to a different account.

     |details| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [result] where:

     |result| - A string result code, which will be empty upon success. The possible values in the case of errors include
                'unknown_error', 'user_cancelled', 'manifest_error', 'icon_error', 'invalid_id', and 'invalid_icon_url'."
  ([details] (gen-call :function ::show-permission-prompt-for-delegated-install &form details)))

(defmacro show-permission-prompt-for-delegated-bundle-install
  "Shows a permission prompt for the given bundle, for installing to a different account.

     |details| - ?
     |contents| - An array of extension details to be installed.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is []."
  ([details contents] (gen-call :function ::show-permission-prompt-for-delegated-bundle-install &form details contents)))

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
  {:namespace "chrome.dashboardPrivate",
   :since "46",
   :functions
   [{:id ::show-permission-prompt-for-delegated-install,
     :name "showPermissionPromptForDelegatedInstall",
     :callback? true,
     :params
     [{:name "details", :type "object"}
      {:name "callback",
       :optional? true,
       :type :callback,
       :callback {:params [{:name "result", :type "dashboardPrivate.Result"}]}}]}
    {:id ::show-permission-prompt-for-delegated-bundle-install,
     :name "showPermissionPromptForDelegatedBundleInstall",
     :callback? true,
     :params
     [{:name "details", :type "object"}
      {:name "contents", :type "[array-of-objects]"}
      {:name "callback", :optional? true, :type :callback}]}]})

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