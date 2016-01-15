(ns chromex.ext.notifications
  "Use the chrome.notifications API to create rich notifications
   using templates and show these notifications to users in the system tray.
   
     * available since Chrome 28
     * https://developer.chrome.com/extensions/notifications"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex.wrapgen :refer [gen-wrap-from-table]]
            [chromex.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro create
  "Creates and displays a notification.
   
     |notificationId| - Identifier of the notification. If not set or empty, an ID will automatically be generated. If it
                        matches an existing notification, this method first clears that notification before proceeding with
                        the create operation.The notificationId parameter is required before Chrome 42.
     |options| - Contents of the notification.
     |callback| - Returns the notification id (either supplied or generated) that represents the created notification.The
                  callback is required before Chrome 42.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([notification-id options #_callback] (gen-call :function ::create &form notification-id options)))

(defmacro update
  "Updates an existing notification.
   
     |notificationId| - The id of the notification to be updated. This is returned by 'notifications.create' method.
     |options| - Contents of the notification to update to.
     |callback| - Called to indicate whether a matching notification existed.The callback is required before Chrome 42.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([notification-id options #_callback] (gen-call :function ::update &form notification-id options)))

(defmacro clear
  "Clears the specified notification.
   
     |notificationId| - The id of the notification to be cleared. This is returned by 'notifications.create' method.
     |callback| - Called to indicate whether a matching notification existed.The callback is required before Chrome 42.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([notification-id #_callback] (gen-call :function ::clear &form notification-id)))

(defmacro get-all
  "Retrieves all the notifications.
   
     |callback| - Returns the set of notification_ids currently in the system.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::get-all &form)))

(defmacro get-permission-level
  "Retrieves whether the user has enabled notifications from this app or extension.
   
     |callback| - Returns the current permission level.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::get-permission-level &form)))

; -- events -----------------------------------------------------------------------------------------------------------------
;
; docs: https://github.com/binaryage/chromex/#tapping-events

(defmacro tap-on-closed-events
  "The notification closed, either by the system or by user action.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-closed &form channel args)))

(defmacro tap-on-clicked-events
  "The user clicked in a non-button area of the notification.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-clicked &form channel args)))

(defmacro tap-on-button-clicked-events
  "The user pressed a button in the notification.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-button-clicked &form channel args)))

(defmacro tap-on-permission-level-changed-events
  "The user changes the permission level.  As of Chrome 47, only ChromeOS has UI that dispatches this event.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-permission-level-changed &form channel args)))

(defmacro tap-on-show-settings-events
  "The user clicked on a link for the app's notification settings.  As of Chrome 47, only ChromeOS has UI that dispatches this
   event.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-show-settings &form channel args)))

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
  {:namespace "chrome.notifications",
   :since "28",
   :functions
   [{:id ::create,
     :name "create",
     :callback? true,
     :params
     [{:name "notification-id", :optional? true, :type "string"}
      {:name "options", :type "notifications.NotificationOptions"}
      {:name "callback",
       :optional? true,
       :type :callback,
       :callback {:params [{:name "notification-id", :type "string"}]}}]}
    {:id ::update,
     :name "update",
     :callback? true,
     :params
     [{:name "notification-id", :type "string"}
      {:name "options", :type "notifications.NotificationOptions"}
      {:name "callback",
       :optional? true,
       :type :callback,
       :callback {:params [{:name "was-updated", :type "boolean"}]}}]}
    {:id ::clear,
     :name "clear",
     :callback? true,
     :params
     [{:name "notification-id", :type "string"}
      {:name "callback",
       :optional? true,
       :type :callback,
       :callback {:params [{:name "was-cleared", :type "boolean"}]}}]}
    {:id ::get-all,
     :name "getAll",
     :since "29",
     :callback? true,
     :params [{:name "callback", :type :callback, :callback {:params [{:name "notifications", :type "object"}]}}]}
    {:id ::get-permission-level,
     :name "getPermissionLevel",
     :since "32",
     :callback? true,
     :params
     [{:name "callback",
       :type :callback,
       :callback {:params [{:name "level", :type "notifications.PermissionLevel"}]}}]}],
   :events
   [{:id ::on-closed,
     :name "onClosed",
     :params [{:name "notification-id", :type "string"} {:name "by-user", :type "boolean"}]}
    {:id ::on-clicked, :name "onClicked", :params [{:name "notification-id", :type "string"}]}
    {:id ::on-button-clicked,
     :name "onButtonClicked",
     :params [{:name "notification-id", :type "string"} {:name "button-index", :type "integer"}]}
    {:id ::on-permission-level-changed,
     :name "onPermissionLevelChanged",
     :since "32",
     :params [{:name "level", :type "notifications.PermissionLevel"}]}
    {:id ::on-show-settings, :name "onShowSettings", :since "32"}]})

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