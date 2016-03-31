(ns chromex.app.display-source
  "The chrome.displaySource API creates a Display
   session using WebMediaStreamTrack as sources.

     * available since Chrome 51
     * https://developer.chrome.com/apps/displaySource"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex.wrapgen :refer [gen-wrap-from-table]]
            [chromex.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro get-available-sinks
  "Queries the list of the currently available Display sinks.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [result] where:

     |result| - https://developer.chrome.com/apps/displaySource#property-callback-result.

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/displaySource#method-getAvailableSinks."
  ([] (gen-call :function ::get-available-sinks &form)))

(defmacro request-authentication
  "Queries authentication data from the sink device.

     |sink-id| - Id of the sink

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [result] where:

     |result| - https://developer.chrome.com/apps/displaySource#property-callback-result.

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/displaySource#method-requestAuthentication."
  ([sink-id] (gen-call :function ::request-authentication &form sink-id)))

(defmacro start-session
  "Creates a Display session using the provided StartSessionInfo instance. The input argument fields must be initialized as
   described below: The |sinkId|  must be a valid id of a sink (obtained via ‘getAvailableSinks’).The |audioTrack| or
   |videoTrack| must be of type MediaStreamTrack. Either |audioTrack| or |videoTrack| can be null but not both. This means
   creating a session with only audio or video.The |authenticationInfo| can be null if no additional authentication data are
   required by the sink; otherwise its |data| field must contain the required authentication data (e.g. PIN value) and its
   |method| field must be the same as one obtained from ‘requestAuthentication’.

     |session-info| - https://developer.chrome.com/apps/displaySource#property-startSession-sessionInfo.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/displaySource#method-startSession."
  ([session-info] (gen-call :function ::start-session &form session-info)))

(defmacro terminate-session
  "Terminates the active Display session.

     |sink-id| - Id of the connected sink.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/displaySource#method-terminateSession."
  ([sink-id] (gen-call :function ::terminate-session &form sink-id)))

; -- events -----------------------------------------------------------------------------------------------------------------
;
; docs: https://github.com/binaryage/chromex/#tapping-events

(defmacro tap-on-sinks-updated-events
  "Event fired when the available sinks are modified (either their amount or properties) |sinks| the list of all currently
   available sinks

   Events will be put on the |channel| with signature [::on-sinks-updated [sinks]] where:

     |sinks| - https://developer.chrome.com/apps/displaySource#property-onSinksUpdated-sinks.

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call.

   https://developer.chrome.com/apps/displaySource#event-onSinksUpdated."
  ([channel & args] (apply gen-call :event ::on-sinks-updated &form channel args)))

(defmacro tap-on-session-terminated-events
  "Event fired when the Display session is terminated. |sinkId| Id of the peer sink

   Events will be put on the |channel| with signature [::on-session-terminated [sink-id]] where:

     |sink-id| - https://developer.chrome.com/apps/displaySource#property-onSessionTerminated-sinkId.

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call.

   https://developer.chrome.com/apps/displaySource#event-onSessionTerminated."
  ([channel & args] (apply gen-call :event ::on-session-terminated &form channel args)))

(defmacro tap-on-session-error-occured-events
  "Event fired when an error occurs. |sinkId| Id of the peer sink |errorInfo| error description

   Events will be put on the |channel| with signature [::on-session-error-occured [sink-id error-info]] where:

     |sink-id| - https://developer.chrome.com/apps/displaySource#property-onSessionErrorOccured-sinkId.
     |error-info| - https://developer.chrome.com/apps/displaySource#property-onSessionErrorOccured-errorInfo.

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call.

   https://developer.chrome.com/apps/displaySource#event-onSessionErrorOccured."
  ([channel & args] (apply gen-call :event ::on-session-error-occured &form channel args)))

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
  {:namespace "chrome.displaySource",
   :since "51",
   :functions
   [{:id ::get-available-sinks,
     :name "getAvailableSinks",
     :callback? true,
     :params
     [{:name "callback",
       :type :callback,
       :callback {:params [{:name "result", :type "[array-of-displaySource.SinkInfos]"}]}}]}
    {:id ::request-authentication,
     :name "requestAuthentication",
     :callback? true,
     :params
     [{:name "sink-id", :type "integer"}
      {:name "callback",
       :type :callback,
       :callback {:params [{:name "result", :type "displaySource.AuthenticationInfo"}]}}]}
    {:id ::start-session,
     :name "startSession",
     :callback? true,
     :params [{:name "session-info", :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::terminate-session,
     :name "terminateSession",
     :callback? true,
     :params [{:name "sink-id", :type "integer"} {:name "callback", :optional? true, :type :callback}]}],
   :events
   [{:id ::on-sinks-updated,
     :name "onSinksUpdated",
     :params [{:name "sinks", :type "[array-of-displaySource.SinkInfos]"}]}
    {:id ::on-session-terminated, :name "onSessionTerminated", :params [{:name "sink-id", :type "integer"}]}
    {:id ::on-session-error-occured,
     :name "onSessionErrorOccured",
     :params [{:name "sink-id", :type "integer"} {:name "error-info", :type "object"}]}]})

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