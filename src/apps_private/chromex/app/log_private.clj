(ns chromex.app.log-private
  "Use chrome.logPrivate API to retrieve log information from multiple
   resources in a consistent format.

     * available since Chrome 30"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex.wrapgen :refer [gen-wrap-from-table]]
            [chromex.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro get-historical
  "Get the existing logs from ChromeOS system.

     |filter| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [res] where:

     |res| - ?"
  ([filter] (gen-call :function ::get-historical &form filter)))

(defmacro start-event-recorder
  "Start capturing events of specific type.

     |event-type| - ?
     |sink| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is []."
  ([event-type sink] (gen-call :function ::start-event-recorder &form event-type sink)))

(defmacro stop-event-recorder
  "Stop  capturing events of specific type.

     |event-type| - ?

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is []."
  ([event-type] (gen-call :function ::stop-event-recorder &form event-type)))

(defmacro dump-logs
  "Dump all system and captured events into a .tar.gz file. The archive file will contain following top level directories:
   /var/log/       ChromeOS system logs.   /home/chronos/user/log/       Session specific logs (chrome app logs).
   /home/chronos/user/log/apps/       Contains webapp specific logs including those collected with
   startEventRecorder(..., sink='file') call.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [logs] where:

     |logs| - ?"
  ([] (gen-call :function ::dump-logs &form)))

; -- events -----------------------------------------------------------------------------------------------------------------
;
; docs: https://github.com/binaryage/chromex/#tapping-events

(defmacro tap-on-captured-events-events
  "Receives events of type which is currently being captured.

   Events will be put on the |channel| with signature [::on-captured-events [entries]] where:

     |entries| - ?

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-captured-events &form channel args)))

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
  {:namespace "chrome.logPrivate",
   :since "30",
   :functions
   [{:id ::get-historical,
     :name "getHistorical",
     :callback? true,
     :params
     [{:name "filter", :type "logPrivate.Filter"}
      {:name "callback", :type :callback, :callback {:params [{:name "res", :type "object"}]}}]}
    {:id ::start-event-recorder,
     :name "startEventRecorder",
     :since "38",
     :callback? true,
     :params
     [{:name "event-type", :type "logPrivate.EventType"}
      {:name "sink", :type "unknown-type"}
      {:name "callback", :type :callback}]}
    {:id ::stop-event-recorder,
     :name "stopEventRecorder",
     :since "38",
     :callback? true,
     :params [{:name "event-type", :type "logPrivate.EventType"} {:name "callback", :type :callback}]}
    {:id ::dump-logs,
     :name "dumpLogs",
     :since "38",
     :callback? true,
     :params [{:name "callback", :type :callback, :callback {:params [{:name "logs", :type "FileEntry"}]}}]}],
   :events
   [{:id ::on-captured-events,
     :name "onCapturedEvents",
     :since "38",
     :params [{:name "entries", :type "[array-of-objects]"}]}]})

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