(ns chromex.ext.system-indicator
  "Manages an app's system indicator icon, an image displayed in the system's
   menubar, system tray, or other visible area provided by the OS.
   This is modelled after the other extension action APIs, such as
   chrome.browserAction and chrome.pageAction.

     * available since Chrome 50
     * https://developer.chrome.com/extensions/systemIndicator"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex.wrapgen :refer [gen-wrap-from-table]]
            [chromex.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro set-icon
  "Set the image to be used as an indicator icon, using a set of ImageData objects. These objects should have multiple
   resolutions so that an appropriate size can be selected for the given icon size and DPI scaling settings. Only square
   ImageData objects are accepted.

     |details| - https://developer.chrome.com/extensions/systemIndicator#property-setIcon-details.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].

   https://developer.chrome.com/extensions/systemIndicator#method-setIcon."
  ([details] (gen-call :function ::set-icon &form details)))

(defmacro enable
  "Show the icon in the status tray.

   https://developer.chrome.com/extensions/systemIndicator#method-enable."
  ([] (gen-call :function ::enable &form)))

(defmacro disable
  "Hide the icon from the status tray.

   https://developer.chrome.com/extensions/systemIndicator#method-disable."
  ([] (gen-call :function ::disable &form)))

; -- events -----------------------------------------------------------------------------------------------------------------
;
; docs: https://github.com/binaryage/chromex/#tapping-events

(defmacro tap-on-clicked-events
  "Fired only when a click on the icon does not result in a menu being shown.

   Events will be put on the |channel| with signature [::on-clicked []].

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call.

   https://developer.chrome.com/extensions/systemIndicator#event-onClicked."
  ([channel & args] (apply gen-call :event ::on-clicked &form channel args)))

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
  {:namespace "chrome.systemIndicator",
   :since "50",
   :functions
   [{:id ::set-icon,
     :name "setIcon",
     :callback? true,
     :params [{:name "details", :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::enable, :name "enable"}
    {:id ::disable, :name "disable"}],
   :events [{:id ::on-clicked, :name "onClicked"}]})

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