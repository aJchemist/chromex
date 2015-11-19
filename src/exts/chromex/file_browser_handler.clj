(ns chromex.file-browser-handler
  "Use the chrome.fileBrowserHandler API to extend the Chrome OS file browser. For example, you can use this API to
   enable users to upload files to your website.
   
     * available since Chrome 12
     * https://developer.chrome.com/extensions/fileBrowserHandler"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex-lib.apigen :refer [gen-wrap-from-table]]
            [chromex-lib.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex-lib.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions ------------------------------------------------------------------------------------------------------

(defmacro select-file
  "Prompts user to select file path under which file should be saved. When the file is selected, file access
   permission required to use the file (read, write and create) are granted to the caller. The file will not actually
   get created during the function call, so function caller must ensure its existence before using it. The function
   has to be invoked with a user gesture.
   
     |selectionParams| - Parameters that will be used while selecting the file.
     |callback| - Function called upon completion."
  [selection-params #_callback]
  (gen-call :function ::select-file (meta &form) selection-params))

; -- events ---------------------------------------------------------------------------------------------------------

(defmacro tap-on-execute
  "Fired when file system action is executed from ChromeOS file browser."
  [channel]
  (gen-call :event ::on-execute (meta &form) channel))

; -- convenience ----------------------------------------------------------------------------------------------------

(defmacro tap-all [chan]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (gen-tap-all-call static-config api-table (meta &form) config chan)))

; -------------------------------------------------------------------------------------------------------------------
; -- API TABLE ------------------------------------------------------------------------------------------------------
; -------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.fileBrowserHandler",
   :since "12",
   :functions
   [{:id ::select-file,
     :name "selectFile",
     :since "21",
     :callback? true,
     :params
     [{:name "selection-params", :type "object"}
      {:name "callback", :type :callback, :callback {:params [{:name "result", :type "object"}]}}]}],
   :events
   [{:id ::on-execute,
     :name "onExecute",
     :params
     [{:name "id", :type "string"}
      {:name "details", :type "fileBrowserHandler.FileHandlerExecuteEventDetails"}]}]})

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