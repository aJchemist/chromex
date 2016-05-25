(ns chromex.ext.system.display
  "Use the system.display API to query display metadata.

     * available since Chrome 30
     * https://developer.chrome.com/extensions/system.display"

  (:refer-clojure :only [defmacro defn apply declare meta let partial])
  (:require [chromex.wrapgen :refer [gen-wrap-helper]]
            [chromex.callgen :refer [gen-call-helper gen-tap-all-events-call]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro get-info
  "Get the information of all attached display devices.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [display-info] where:

     |display-info| - https://developer.chrome.com/extensions/system.display#property-callback-displayInfo.

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/extensions/system.display#method-getInfo."
  ([] (gen-call :function ::get-info &form)))

(defmacro set-display-properties
  "Updates the properties for the display specified by |id|, according to the information provided in |info|. On failure,
   'runtime.lastError' will be set. NOTE: This is only available to Chrome OS Kiosk apps and Web UI.

     |id| - The display's unique identifier.
     |info| - The information about display properties that should be changed.     A property will be changed only if a new
              value for it is specified in     |info|.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/extensions/system.display#method-setDisplayProperties."
  ([id info] (gen-call :function ::set-display-properties &form id info)))

(defmacro enable-unified-desktop
  "Enables/disables the unified desktop feature. Note that this simply enables the feature, but will not change the actual
   desktop mode. (That is, if the desktop is in mirror mode, it will stay in mirror mode) NOTE: This is only available to
   Chrome OS Kiosk apps and Web UI.

     |enabled| - True if unified desktop should be enabled.

   https://developer.chrome.com/extensions/system.display#method-enableUnifiedDesktop."
  ([enabled] (gen-call :function ::enable-unified-desktop &form enabled)))

(defmacro overscan-calibration-start
  "Starts overscan calibration for a display. This will show an overlay on the screen indicating the current overscan insets.
   If overscan calibration for display |id| is in progress this will reset calibration.

     |id| - The display's unique identifier.

   https://developer.chrome.com/extensions/system.display#method-overscanCalibrationStart."
  ([id] (gen-call :function ::overscan-calibration-start &form id)))

(defmacro overscan-calibration-adjust
  "Adjusts the current overscan insets for a display. Typically this should etiher move the display along an axis (e.g.
   left+right have the same value) or scale it along an axis (e.g. top+bottom have opposite values). Each Adjust call is
   cumulative with previous calls since Start.

     |id| - The display's unique identifier.
     |delta| - The amount to change the overscan insets.

   https://developer.chrome.com/extensions/system.display#method-overscanCalibrationAdjust."
  ([id delta] (gen-call :function ::overscan-calibration-adjust &form id delta)))

(defmacro overscan-calibration-reset
  "Resets the overscan insets for a display to the last saved value (i.e before Start was called).

     |id| - The display's unique identifier.

   https://developer.chrome.com/extensions/system.display#method-overscanCalibrationReset."
  ([id] (gen-call :function ::overscan-calibration-reset &form id)))

(defmacro overscan-calibration-complete
  "Complete overscan adjustments for a display  by saving the current values and hiding the overlay.

     |id| - The display's unique identifier.

   https://developer.chrome.com/extensions/system.display#method-overscanCalibrationComplete."
  ([id] (gen-call :function ::overscan-calibration-complete &form id)))

; -- events -----------------------------------------------------------------------------------------------------------------
;
; docs: https://github.com/binaryage/chromex/#tapping-events

(defmacro tap-on-display-changed-events
  "Fired when anything changes to the display configuration.

   Events will be put on the |channel| with signature [::on-display-changed []].

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call.

   https://developer.chrome.com/extensions/system.display#event-onDisplayChanged."
  ([channel & args] (apply gen-call :event ::on-display-changed &form channel args)))

; -- convenience ------------------------------------------------------------------------------------------------------------

(defmacro tap-all-events
  "Taps all valid non-deprecated events in chromex.ext.system.display namespace."
  [chan]
  (gen-tap-all-events-call api-table (meta &form) chan))

; ---------------------------------------------------------------------------------------------------------------------------
; -- API TABLE --------------------------------------------------------------------------------------------------------------
; ---------------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.system.display",
   :since "30",
   :functions
   [{:id ::get-info,
     :name "getInfo",
     :callback? true,
     :params
     [{:name "callback", :type :callback, :callback {:params [{:name "display-info", :type "[array-of-objects]"}]}}]}
    {:id ::set-display-properties,
     :name "setDisplayProperties",
     :callback? true,
     :params
     [{:name "id", :type "string"}
      {:name "info", :type "object"}
      {:name "callback", :optional? true, :type :callback}]}
    {:id ::enable-unified-desktop,
     :name "enableUnifiedDesktop",
     :since "46",
     :params [{:name "enabled", :type "boolean"}]}
    {:id ::overscan-calibration-start,
     :name "overscanCalibrationStart",
     :since "master",
     :params [{:name "id", :type "string"}]}
    {:id ::overscan-calibration-adjust,
     :name "overscanCalibrationAdjust",
     :since "master",
     :params [{:name "id", :type "string"} {:name "delta", :type "system.display.Insets"}]}
    {:id ::overscan-calibration-reset,
     :name "overscanCalibrationReset",
     :since "master",
     :params [{:name "id", :type "string"}]}
    {:id ::overscan-calibration-complete,
     :name "overscanCalibrationComplete",
     :since "master",
     :params [{:name "id", :type "string"}]}],
   :events [{:id ::on-display-changed, :name "onDisplayChanged"}]})

; -- helpers ----------------------------------------------------------------------------------------------------------------

; code generation for native API wrapper
(defmacro gen-wrap [kind item-id config & args]
  (apply gen-wrap-helper api-table kind item-id config args))

; code generation for API call-site
(def gen-call (partial gen-call-helper api-table))