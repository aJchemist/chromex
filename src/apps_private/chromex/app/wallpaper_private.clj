(ns chromex.app.wallpaper-private
  "  * available since Chrome 22"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex.wrapgen :refer [gen-wrap-from-table]]
            [chromex.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- events -----------------------------------------------------------------------------------------------------------------
;
; docs: https://github.com/binaryage/chromex/#tapping-events

(defmacro tap-on-wallpaper-changed-by3rd-party-events
  "This event is sent when the current wallpaper was set by a third party application.

   Events will be put on the |channel| with signature [::on-wallpaper-changed-by3rd-party [wallpaper thumbnail layout
   app-name]] where:

     |wallpaper| - The third party custom wallpaper data.
     |thumbnail| - The third party custom wallpaper thumbnail data.
     |layout| - ?
     |app-name| - The third party wallpaper app name.

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-wallpaper-changed-by3rd-party &form channel args)))

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
  {:namespace "chrome.wallpaperPrivate",
   :since "22",
   :events
   [{:id ::on-wallpaper-changed-by3rd-party,
     :name "onWallpaperChangedBy3rdParty",
     :since "43",
     :params
     [{:name "wallpaper", :type "binary"}
      {:name "thumbnail", :type "binary"}
      {:name "layout", :type "wallpaper.WallpaperLayout"}
      {:name "app-name", :type "string"}]}]})

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