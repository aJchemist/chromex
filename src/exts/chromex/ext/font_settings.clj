(ns chromex.ext.font-settings
  "Use the chrome.fontSettings API to manage Chrome's font settings.
   
     * available since Chrome 22
     * https://developer.chrome.com/extensions/fontSettings"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex.wrapgen :refer [gen-wrap-from-table]]
            [chromex.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro clear-font
  "Clears the font set by this extension, if any.
   
     |details| - See https://developer.chrome.com/extensions/fontSettings#property-clearFont-details.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].
   
   See https://developer.chrome.com/extensions/fontSettings#method-clearFont."
  ([details #_callback] (gen-call :function ::clear-font &form details)))

(defmacro get-font
  "Gets the font for a given script and generic font family.
   
     |details| - See https://developer.chrome.com/extensions/fontSettings#property-getFont-details.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [details] where:
   
     |details| - See https://developer.chrome.com/extensions/fontSettings#property-callback-details.
   
   See https://developer.chrome.com/extensions/fontSettings#method-getFont."
  ([details #_callback] (gen-call :function ::get-font &form details)))

(defmacro set-font
  "Sets the font for a given script and generic font family.
   
     |details| - See https://developer.chrome.com/extensions/fontSettings#property-setFont-details.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].
   
   See https://developer.chrome.com/extensions/fontSettings#method-setFont."
  ([details #_callback] (gen-call :function ::set-font &form details)))

(defmacro get-font-list
  "Gets a list of fonts on the system.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [results] where:
   
     |results| - See https://developer.chrome.com/extensions/fontSettings#property-callback-results.
   
   See https://developer.chrome.com/extensions/fontSettings#method-getFontList."
  ([#_callback] (gen-call :function ::get-font-list &form)))

(defmacro clear-default-font-size
  "Clears the default font size set by this extension, if any.
   
     |details| - This parameter is currently unused.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].
   
   See https://developer.chrome.com/extensions/fontSettings#method-clearDefaultFontSize."
  ([details #_callback] (gen-call :function ::clear-default-font-size &form details))
  ([] `(clear-default-font-size :omit)))

(defmacro get-default-font-size
  "Gets the default font size.
   
     |details| - This parameter is currently unused.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [details] where:
   
     |details| - See https://developer.chrome.com/extensions/fontSettings#property-callback-details.
   
   See https://developer.chrome.com/extensions/fontSettings#method-getDefaultFontSize."
  ([details #_callback] (gen-call :function ::get-default-font-size &form details))
  ([] `(get-default-font-size :omit)))

(defmacro set-default-font-size
  "Sets the default font size.
   
     |details| - See https://developer.chrome.com/extensions/fontSettings#property-setDefaultFontSize-details.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].
   
   See https://developer.chrome.com/extensions/fontSettings#method-setDefaultFontSize."
  ([details #_callback] (gen-call :function ::set-default-font-size &form details)))

(defmacro clear-default-fixed-font-size
  "Clears the default fixed font size set by this extension, if any.
   
     |details| - This parameter is currently unused.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].
   
   See https://developer.chrome.com/extensions/fontSettings#method-clearDefaultFixedFontSize."
  ([details #_callback] (gen-call :function ::clear-default-fixed-font-size &form details))
  ([] `(clear-default-fixed-font-size :omit)))

(defmacro get-default-fixed-font-size
  "Gets the default size for fixed width fonts.
   
     |details| - This parameter is currently unused.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [details] where:
   
     |details| - See https://developer.chrome.com/extensions/fontSettings#property-callback-details.
   
   See https://developer.chrome.com/extensions/fontSettings#method-getDefaultFixedFontSize."
  ([details #_callback] (gen-call :function ::get-default-fixed-font-size &form details))
  ([] `(get-default-fixed-font-size :omit)))

(defmacro set-default-fixed-font-size
  "Sets the default size for fixed width fonts.
   
     |details| - See https://developer.chrome.com/extensions/fontSettings#property-setDefaultFixedFontSize-details.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].
   
   See https://developer.chrome.com/extensions/fontSettings#method-setDefaultFixedFontSize."
  ([details #_callback] (gen-call :function ::set-default-fixed-font-size &form details)))

(defmacro clear-minimum-font-size
  "Clears the minimum font size set by this extension, if any.
   
     |details| - This parameter is currently unused.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].
   
   See https://developer.chrome.com/extensions/fontSettings#method-clearMinimumFontSize."
  ([details #_callback] (gen-call :function ::clear-minimum-font-size &form details))
  ([] `(clear-minimum-font-size :omit)))

(defmacro get-minimum-font-size
  "Gets the minimum font size.
   
     |details| - This parameter is currently unused.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [details] where:
   
     |details| - See https://developer.chrome.com/extensions/fontSettings#property-callback-details.
   
   See https://developer.chrome.com/extensions/fontSettings#method-getMinimumFontSize."
  ([details #_callback] (gen-call :function ::get-minimum-font-size &form details))
  ([] `(get-minimum-font-size :omit)))

(defmacro set-minimum-font-size
  "Sets the minimum font size.
   
     |details| - See https://developer.chrome.com/extensions/fontSettings#property-setMinimumFontSize-details.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].
   
   See https://developer.chrome.com/extensions/fontSettings#method-setMinimumFontSize."
  ([details #_callback] (gen-call :function ::set-minimum-font-size &form details)))

; -- events -----------------------------------------------------------------------------------------------------------------
;
; docs: https://github.com/binaryage/chromex/#tapping-events

(defmacro tap-on-font-changed-events
  "Fired when a font setting changes.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call.
   
   See https://developer.chrome.com/extensions/fontSettings#event-onFontChanged."
  ([channel & args] (apply gen-call :event ::on-font-changed &form channel args)))

(defmacro tap-on-default-font-size-changed-events
  "Fired when the default font size setting changes.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call.
   
   See https://developer.chrome.com/extensions/fontSettings#event-onDefaultFontSizeChanged."
  ([channel & args] (apply gen-call :event ::on-default-font-size-changed &form channel args)))

(defmacro tap-on-default-fixed-font-size-changed-events
  "Fired when the default fixed font size setting changes.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call.
   
   See https://developer.chrome.com/extensions/fontSettings#event-onDefaultFixedFontSizeChanged."
  ([channel & args] (apply gen-call :event ::on-default-fixed-font-size-changed &form channel args)))

(defmacro tap-on-minimum-font-size-changed-events
  "Fired when the minimum font size setting changes.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call.
   
   See https://developer.chrome.com/extensions/fontSettings#event-onMinimumFontSizeChanged."
  ([channel & args] (apply gen-call :event ::on-minimum-font-size-changed &form channel args)))

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
  {:namespace "chrome.fontSettings",
   :since "22",
   :functions
   [{:id ::clear-font,
     :name "clearFont",
     :callback? true,
     :params [{:name "details", :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::get-font,
     :name "getFont",
     :callback? true,
     :params
     [{:name "details", :type "object"}
      {:name "callback", :optional? true, :type :callback, :callback {:params [{:name "details", :type "object"}]}}]}
    {:id ::set-font,
     :name "setFont",
     :callback? true,
     :params [{:name "details", :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::get-font-list,
     :name "getFontList",
     :callback? true,
     :params
     [{:name "callback",
       :type :callback,
       :callback {:params [{:name "results", :type "[array-of-fontSettings.FontNames]"}]}}]}
    {:id ::clear-default-font-size,
     :name "clearDefaultFontSize",
     :callback? true,
     :params [{:name "details", :optional? true, :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::get-default-font-size,
     :name "getDefaultFontSize",
     :callback? true,
     :params
     [{:name "details", :optional? true, :type "object"}
      {:name "callback", :optional? true, :type :callback, :callback {:params [{:name "details", :type "object"}]}}]}
    {:id ::set-default-font-size,
     :name "setDefaultFontSize",
     :callback? true,
     :params [{:name "details", :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::clear-default-fixed-font-size,
     :name "clearDefaultFixedFontSize",
     :callback? true,
     :params [{:name "details", :optional? true, :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::get-default-fixed-font-size,
     :name "getDefaultFixedFontSize",
     :callback? true,
     :params
     [{:name "details", :optional? true, :type "object"}
      {:name "callback", :optional? true, :type :callback, :callback {:params [{:name "details", :type "object"}]}}]}
    {:id ::set-default-fixed-font-size,
     :name "setDefaultFixedFontSize",
     :callback? true,
     :params [{:name "details", :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::clear-minimum-font-size,
     :name "clearMinimumFontSize",
     :callback? true,
     :params [{:name "details", :optional? true, :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::get-minimum-font-size,
     :name "getMinimumFontSize",
     :callback? true,
     :params
     [{:name "details", :optional? true, :type "object"}
      {:name "callback", :optional? true, :type :callback, :callback {:params [{:name "details", :type "object"}]}}]}
    {:id ::set-minimum-font-size,
     :name "setMinimumFontSize",
     :callback? true,
     :params [{:name "details", :type "object"} {:name "callback", :optional? true, :type :callback}]}],
   :events
   [{:id ::on-font-changed, :name "onFontChanged", :params [{:name "details", :type "object"}]}
    {:id ::on-default-font-size-changed, :name "onDefaultFontSizeChanged", :params [{:name "details", :type "object"}]}
    {:id ::on-default-fixed-font-size-changed,
     :name "onDefaultFixedFontSizeChanged",
     :params [{:name "details", :type "object"}]}
    {:id ::on-minimum-font-size-changed, :name "onMinimumFontSizeChanged", :params [{:name "details", :type "object"}]}]})

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