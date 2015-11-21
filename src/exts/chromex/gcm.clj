(ns chromex.gcm
  "Use chrome.gcm to enable apps and extensions to send and receive messages through the Google Cloud Messaging
   Service.
   
     * available since Chrome 35
     * https://developer.chrome.com/extensions/gcm"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex-lib.wrapgen :refer [gen-wrap-from-table]]
            [chromex-lib.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex-lib.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- properties -----------------------------------------------------------------------------------------------------

(defmacro get-max-message-size
  "The maximum size (in bytes) of all key/value pairs in a message."
  []
  (gen-call :property ::max-message-size (meta &form)))

; -- functions ------------------------------------------------------------------------------------------------------

(defmacro register
  "Registers the application with GCM. The registration ID will be returned by the callback. If register is called
   again with the same list of senderIds, the same registration ID will be returned.
   
     |senderIds| - A list of server IDs that are allowed to send messages to the application. It should contain at
                   least one and no more than 100 sender IDs.
     |callback| - Function called when registration completes. It should check 'runtime.lastError' for error when
                  registrationId is empty."
  [sender-ids #_callback]
  (gen-call :function ::register (meta &form) sender-ids))

(defmacro unregister
  "Unregisters the application from GCM.
   
     |callback| - A function called after the unregistration completes. Unregistration was successful if
                  'runtime.lastError' is not set."
  [#_callback]
  (gen-call :function ::unregister (meta &form)))

(defmacro send
  "Sends a message according to its contents.
   
     |message| - A message to send to the other party via GCM.
     |callback| - A function called after the message is successfully queued for sending. 'runtime.lastError' should
                  be checked, to ensure a message was sent without problems."
  [message #_callback]
  (gen-call :function ::send (meta &form) message))

; -- events ---------------------------------------------------------------------------------------------------------

(defmacro tap-on-message-events
  "Fired when a message is received through GCM."
  [channel]
  (gen-call :event ::on-message (meta &form) channel))

(defmacro tap-on-messages-deleted-events
  "Fired when a GCM server had to delete messages sent by an app server to the application. See Messages deleted event
   section of Cloud Messaging documentation for details on handling this event."
  [channel]
  (gen-call :event ::on-messages-deleted (meta &form) channel))

(defmacro tap-on-send-error-events
  "Fired when it was not possible to send a message to the GCM server."
  [channel]
  (gen-call :event ::on-send-error (meta &form) channel))

; -- convenience ----------------------------------------------------------------------------------------------------

(defmacro tap-all-events [chan]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (gen-tap-all-call static-config api-table (meta &form) config chan)))

; -------------------------------------------------------------------------------------------------------------------
; -- API TABLE ------------------------------------------------------------------------------------------------------
; -------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.gcm",
   :since "35",
   :properties [{:id ::max-message-size, :name "MAX_MESSAGE_SIZE", :return-type "unknown-type"}],
   :functions
   [{:id ::register,
     :name "register",
     :callback? true,
     :params
     [{:name "sender-ids", :type "[array-of-strings]"}
      {:name "callback", :type :callback, :callback {:params [{:name "registration-id", :type "string"}]}}]}
    {:id ::unregister, :name "unregister", :callback? true, :params [{:name "callback", :type :callback}]}
    {:id ::send,
     :name "send",
     :callback? true,
     :params
     [{:name "message", :type "object"}
      {:name "callback", :type :callback, :callback {:params [{:name "message-id", :type "string"}]}}]}],
   :events
   [{:id ::on-message, :name "onMessage", :params [{:name "message", :type "object"}]}
    {:id ::on-messages-deleted, :name "onMessagesDeleted"}
    {:id ::on-send-error, :name "onSendError", :params [{:name "error", :type "object"}]}]})

; -- helpers --------------------------------------------------------------------------------------------------------

; code generation for native API wrapper
(defmacro gen-wrap [kind item-id config & args]
  (let [static-config (get-static-config)]
    (apply gen-wrap-from-table static-config api-table kind item-id config args)))

; code generation for API call-site
(defn gen-call [kind item src-info & args]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (apply gen-call-from-table static-config api-table kind item src-info config args)))