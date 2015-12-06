(ns chromex.ext.processes
  "Use the chrome.processes API to interact with the browser's processes.
   
     * available since Chrome 48
     * https://developer.chrome.com/extensions/processes"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex-lib.wrapgen :refer [gen-wrap-from-table]]
            [chromex-lib.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex-lib.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro terminate
  "Terminates the specified renderer process. Equivalent to visiting about:crash, but without changing the tab's URL.
   
     |processId| - The ID of the process to be terminated.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([process-id #_callback] (gen-call :function ::terminate &form process-id)))

(defmacro get-process-id-for-tab
  "Returns the ID of the renderer process for the specified tab.
   
     |tabId| - The ID of the tab for which the renderer process ID is to be returned.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([tab-id #_callback] (gen-call :function ::get-process-id-for-tab &form tab-id)))

(defmacro get-process-info
  "Retrieves the process information for each process ID specified.
   
     |processIds| - The list of process IDs or single process ID for which to return the process information. An empty list
                    indicates all processes are requested.
     |includeMemory| - True if detailed memory usage is required. Note, collecting memory usage information incurs extra CPU
                       usage and should only be queried for when needed.
     |callback| - Called when the processes information is collected.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([process-ids include-memory #_callback] (gen-call :function ::get-process-info &form process-ids include-memory)))

; -- events -----------------------------------------------------------------------------------------------------------------
;
; docs: https://github.com/binaryage/chromex/#tapping-events

(defmacro tap-on-updated-events
  "Fired each time the Task Manager updates its process statistics, providing the dictionary of updated Process objects,
   indexed by process ID.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-updated &form channel args)))
(defmacro tap-on-updated-with-memory-events
  "Fired each time the Task Manager updates its process statistics, providing the dictionary of updated Process objects,
   indexed by process ID. Identical to onUpdate, with the addition of memory usage details included in each Process object.
   Note, collecting memory usage information incurs extra CPU usage and should only be listened for when needed.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-updated-with-memory &form channel args)))
(defmacro tap-on-created-events
  "Fired each time a process is created, providing the corrseponding Process object.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-created &form channel args)))
(defmacro tap-on-unresponsive-events
  "Fired each time a process becomes unresponsive, providing the corrseponding Process object.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-unresponsive &form channel args)))
(defmacro tap-on-exited-events
  "Fired each time a process is terminated, providing the type of exit.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-exited &form channel args)))

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
  {:namespace "chrome.processes",
   :since "48",
   :functions
   [{:id ::terminate,
     :name "terminate",
     :callback? true,
     :params
     [{:name "process-id", :type "integer"}
      {:name "callback",
       :optional? true,
       :type :callback,
       :callback {:params [{:name "did-terminate", :type "boolean"}]}}]}
    {:id ::get-process-id-for-tab,
     :name "getProcessIdForTab",
     :callback? true,
     :params
     [{:name "tab-id", :type "integer"}
      {:name "callback", :type :callback, :callback {:params [{:name "process-id", :type "integer"}]}}]}
    {:id ::get-process-info,
     :name "getProcessInfo",
     :callback? true,
     :params
     [{:name "process-ids", :type "integer-or-[array-of-integers]"}
      {:name "include-memory", :type "boolean"}
      {:name "callback", :type :callback, :callback {:params [{:name "processes", :type "object"}]}}]}],
   :events
   [{:id ::on-updated, :name "onUpdated", :params [{:name "processes", :type "object"}]}
    {:id ::on-updated-with-memory, :name "onUpdatedWithMemory", :params [{:name "processes", :type "object"}]}
    {:id ::on-created, :name "onCreated", :params [{:name "process", :type "processes.Process"}]}
    {:id ::on-unresponsive, :name "onUnresponsive", :params [{:name "process", :type "processes.Process"}]}
    {:id ::on-exited,
     :name "onExited",
     :params
     [{:name "process-id", :type "integer"}
      {:name "exit-type", :type "integer"}
      {:name "exit-code", :type "integer"}]}]})

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