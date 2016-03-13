(ns chromex.app.web-view-internal
  "  * available since Chrome 38
     * https://developer.chrome.com/extensions/webViewInternal"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex.wrapgen :refer [gen-wrap-from-table]]
            [chromex.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro execute-script
  "Injects JavaScript code into a  page.
   
     |instanceId| - The instance ID of the guest  process.
     |src| - The src of the guest  tag.
     |details| - Details of the script or CSS to inject. Either the code or the file property must be set, but both may not
                 be set at the same time.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [result] where:
   
     |result| - The result of the script in every injected frame.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-executeScript."
  ([instance-id src details #_callback] (gen-call :function ::execute-script &form instance-id src details)))

(defmacro insert-css
  "Injects CSS into a  page. For details, see the programmatic injection section of the content scripts doc.
   
     |instanceId| - The instance ID of the guest  process.
     |src| - The src of the guest  tag.
     |details| - Details of the script or CSS to inject. Either the code or the file property must be set, but both may not
                 be set at the same time.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].
   
   See https://developer.chrome.com/extensions/webViewInternal#method-insertCSS."
  ([instance-id src details #_callback] (gen-call :function ::insert-css &form instance-id src details)))

(defmacro add-content-scripts
  "Adds content scripts into a  page. For details, see the programmatic injection section of the content scripts doc.
   
     |instanceId| - The instance ID of the guest  process.
     |contentScriptList| - Details of the content scripts to add.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-addContentScripts."
  ([instance-id content-script-list] (gen-call :function ::add-content-scripts &form instance-id content-script-list)))

(defmacro remove-content-scripts
  "Removes specified content scripts from a  page. For details, see the programmatic injection section of the content scripts
   doc.
   
     |instanceId| - The instance ID of the guest  process.
     |scriptNameList| - A list of names of content scripts that will be removed. If the list is empty, all the content
                        scripts added to the  page will be removed.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-removeContentScripts."
  ([instance-id script-name-list] (gen-call :function ::remove-content-scripts &form instance-id script-name-list))
  ([instance-id] `(remove-content-scripts ~instance-id :omit)))

(defmacro set-zoom
  "  |instanceId| - The instance ID of the guest  process.
     |zoomFactor| - The new zoom factor.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].
   
   See https://developer.chrome.com/extensions/webViewInternal#method-setZoom."
  ([instance-id zoom-factor #_callback] (gen-call :function ::set-zoom &form instance-id zoom-factor)))

(defmacro get-zoom
  "  |instanceId| - The instance ID of the guest  process.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [zoomFactor] where:
   
     |zoomFactor| - The current zoom factor.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-getZoom."
  ([instance-id #_callback] (gen-call :function ::get-zoom &form instance-id)))

(defmacro set-zoom-mode
  "Sets the zoom mode of the webview.
   
     |instanceId| - The instance ID of the guest  process.
     |ZoomMode| - Defines the how zooming is handled in the webview.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].
   
   See https://developer.chrome.com/extensions/webViewInternal#method-setZoomMode."
  ([instance-id zoom-mode #_callback] (gen-call :function ::set-zoom-mode &form instance-id zoom-mode)))

(defmacro get-zoom-mode
  "Gets the current zoom mode.
   
     |instanceId| - The instance ID of the guest  process.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [ZoomMode] where:
   
     |ZoomMode| - Defines the how zooming is handled in the webview.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-getZoomMode."
  ([instance-id #_callback] (gen-call :function ::get-zoom-mode &form instance-id)))

(defmacro find
  "Initiates a find-in-page request.
   
     |instanceId| - The instance ID of the guest  process.
     |searchText| - The string to find in the page.
     |options| - See https://developer.chrome.com/extensions/webViewInternal#property-find-options.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [results] where:
   
     |results| - See https://developer.chrome.com/extensions/webViewInternal#property-callback-results.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-find."
  ([instance-id search-text options #_callback] (gen-call :function ::find &form instance-id search-text options))
  ([instance-id search-text] `(find ~instance-id ~search-text :omit)))

(defmacro stop-finding
  "Ends the current find session (clearing all highlighting) and cancels all find requests in progress.
   
     |instanceId| - The instance ID of the guest  process.
     |action| - Determines what to do with the active match after the find session has ended. 'clear' will clear the
                highlighting over the active match; 'keep' will keep the active match highlighted; 'activate' will keep the
                active match highlighted and simulate a user click on that match.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-stopFinding."
  ([instance-id action] (gen-call :function ::stop-finding &form instance-id action))
  ([instance-id] `(stop-finding ~instance-id :omit)))

(defmacro load-data-with-base-url
  "Loads a data URL with a specified base URL used for relative links. Optionally, a virtual URL can be provided to be shown
   to the user instead of the data URL.
   
     |instanceId| - The instance ID of the guest  process.
     |dataUrl| - The data URL to load.
     |baseUrl| - The base URL that will be used for relative links.
     |virtualUrl| - The URL that will be displayed to the user.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].
   
   See https://developer.chrome.com/extensions/webViewInternal#method-loadDataWithBaseUrl."
  ([instance-id data-url base-url virtual-url #_callback] (gen-call :function ::load-data-with-base-url &form instance-id data-url base-url virtual-url))
  ([instance-id data-url base-url] `(load-data-with-base-url ~instance-id ~data-url ~base-url :omit)))

(defmacro go
  "  |instanceId| - See https://developer.chrome.com/extensions/webViewInternal#property-go-instanceId.
     |relativeIndex| - See https://developer.chrome.com/extensions/webViewInternal#property-go-relativeIndex.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [success] where:
   
     |success| - Indicates whether the navigation was successful.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-go."
  ([instance-id relative-index #_callback] (gen-call :function ::go &form instance-id relative-index)))

(defmacro override-user-agent
  "  |instanceId| - See https://developer.chrome.com/extensions/webViewInternal#property-overrideUserAgent-instanceId.
     |userAgentOverride| - See
                           https://developer.chrome.com/extensions/webViewInternal#property-overrideUserAgent-userAgentOverri
                           de.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-overrideUserAgent."
  ([instance-id user-agent-override] (gen-call :function ::override-user-agent &form instance-id user-agent-override)))

(defmacro reload
  "  |instanceId| - See https://developer.chrome.com/extensions/webViewInternal#property-reload-instanceId.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-reload."
  ([instance-id] (gen-call :function ::reload &form instance-id)))

(defmacro set-allow-transparency
  "  |instanceId| - See https://developer.chrome.com/extensions/webViewInternal#property-setAllowTransparency-instanceId.
     |allow| - See https://developer.chrome.com/extensions/webViewInternal#property-setAllowTransparency-allow.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-setAllowTransparency."
  ([instance-id allow] (gen-call :function ::set-allow-transparency &form instance-id allow)))

(defmacro set-allow-scaling
  "  |instanceId| - See https://developer.chrome.com/extensions/webViewInternal#property-setAllowScaling-instanceId.
     |allow| - See https://developer.chrome.com/extensions/webViewInternal#property-setAllowScaling-allow.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-setAllowScaling."
  ([instance-id allow] (gen-call :function ::set-allow-scaling &form instance-id allow)))

(defmacro set-name
  "  |instanceId| - See https://developer.chrome.com/extensions/webViewInternal#property-setName-instanceId.
     |frameName| - See https://developer.chrome.com/extensions/webViewInternal#property-setName-frameName.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-setName."
  ([instance-id frame-name] (gen-call :function ::set-name &form instance-id frame-name)))

(defmacro set-permission
  "  |instanceId| - See https://developer.chrome.com/extensions/webViewInternal#property-setPermission-instanceId.
     |requestId| - See https://developer.chrome.com/extensions/webViewInternal#property-setPermission-requestId.
     |action| - See https://developer.chrome.com/extensions/webViewInternal#property-setPermission-action.
     |userInput| - See https://developer.chrome.com/extensions/webViewInternal#property-setPermission-userInput.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [allowed] where:
   
     |allowed| - See https://developer.chrome.com/extensions/webViewInternal#property-callback-allowed.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-setPermission."
  ([instance-id request-id action user-input #_callback] (gen-call :function ::set-permission &form instance-id request-id action user-input))
  ([instance-id request-id action] `(set-permission ~instance-id ~request-id ~action :omit)))

(defmacro navigate
  "  |instanceId| - See https://developer.chrome.com/extensions/webViewInternal#property-navigate-instanceId.
     |src| - See https://developer.chrome.com/extensions/webViewInternal#property-navigate-src.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-navigate."
  ([instance-id src] (gen-call :function ::navigate &form instance-id src)))

(defmacro stop
  "  |instanceId| - See https://developer.chrome.com/extensions/webViewInternal#property-stop-instanceId.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-stop."
  ([instance-id] (gen-call :function ::stop &form instance-id)))

(defmacro terminate
  "  |instanceId| - See https://developer.chrome.com/extensions/webViewInternal#property-terminate-instanceId.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-terminate."
  ([instance-id] (gen-call :function ::terminate &form instance-id)))

(defmacro capture-visible-region
  "foo
   
     |instanceId| - The instance ID of the guest  process.
     |options| - Details about the format and quality of an image.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [dataUrl] where:
   
     |dataUrl| - A data URL which encodes an image of the visible area of the captured tab. May be assigned to the 'src'
                 property of an HTML Image element for display.
   
   See https://developer.chrome.com/extensions/webViewInternal#method-captureVisibleRegion."
  ([instance-id options #_callback] (gen-call :function ::capture-visible-region &form instance-id options))
  ([instance-id] `(capture-visible-region ~instance-id :omit)))

(defmacro clear-data
  "Clears various types of browsing data stored in a storage partition of a .
   
     |instanceId| - The instance ID of the guest  process.
     |options| - Options that determine exactly what data will be removed.
     |dataToRemove| - A set of data types. Missing data types are interpreted as false.
   
   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [].
   
   See https://developer.chrome.com/extensions/webViewInternal#method-clearData."
  ([instance-id options data-to-remove #_callback] (gen-call :function ::clear-data &form instance-id options data-to-remove)))

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
  {:namespace "chrome.webViewInternal",
   :since "38",
   :functions
   [{:id ::execute-script,
     :name "executeScript",
     :callback? true,
     :params
     [{:name "instance-id", :type "integer"}
      {:name "src", :type "string"}
      {:name "details", :type "object"}
      {:name "callback",
       :optional? true,
       :type :callback,
       :callback {:params [{:name "result", :optional? true, :type "[array-of-anys]"}]}}]}
    {:id ::insert-css,
     :name "insertCSS",
     :callback? true,
     :params
     [{:name "instance-id", :type "integer"}
      {:name "src", :type "string"}
      {:name "details", :type "object"}
      {:name "callback", :optional? true, :type :callback}]}
    {:id ::add-content-scripts,
     :name "addContentScripts",
     :since "44",
     :params [{:name "instance-id", :type "integer"} {:name "content-script-list", :type "[array-of-objects]"}]}
    {:id ::remove-content-scripts,
     :name "removeContentScripts",
     :since "44",
     :params
     [{:name "instance-id", :type "integer"} {:name "script-name-list", :optional? true, :type "[array-of-strings]"}]}
    {:id ::set-zoom,
     :name "setZoom",
     :callback? true,
     :params
     [{:name "instance-id", :type "integer"}
      {:name "zoom-factor", :type "double"}
      {:name "callback", :optional? true, :type :callback}]}
    {:id ::get-zoom,
     :name "getZoom",
     :callback? true,
     :params
     [{:name "instance-id", :type "integer"}
      {:name "callback", :type :callback, :callback {:params [{:name "zoom-factor", :type "double"}]}}]}
    {:id ::set-zoom-mode,
     :name "setZoomMode",
     :since "43",
     :callback? true,
     :params
     [{:name "instance-id", :type "integer"}
      {:name "zoom-mode", :type "unknown-type"}
      {:name "callback", :optional? true, :type :callback}]}
    {:id ::get-zoom-mode,
     :name "getZoomMode",
     :since "43",
     :callback? true,
     :params
     [{:name "instance-id", :type "integer"}
      {:name "callback", :type :callback, :callback {:params [{:name "zoom-mode", :type "unknown-type"}]}}]}
    {:id ::find,
     :name "find",
     :callback? true,
     :params
     [{:name "instance-id", :type "integer"}
      {:name "search-text", :type "string"}
      {:name "options", :optional? true, :type "object"}
      {:name "callback",
       :optional? true,
       :type :callback,
       :callback {:params [{:name "results", :optional? true, :type "object"}]}}]}
    {:id ::stop-finding,
     :name "stopFinding",
     :params [{:name "instance-id", :type "integer"} {:name "action", :optional? true, :type "unknown-type"}]}
    {:id ::load-data-with-base-url,
     :name "loadDataWithBaseUrl",
     :since "40",
     :callback? true,
     :params
     [{:name "instance-id", :type "integer"}
      {:name "data-url", :type "string"}
      {:name "base-url", :type "string"}
      {:name "virtual-url", :optional? true, :type "string"}
      {:name "callback", :type :callback}]}
    {:id ::go,
     :name "go",
     :callback? true,
     :params
     [{:name "instance-id", :type "integer"}
      {:name "relative-index", :type "integer"}
      {:name "callback", :optional? true, :type :callback, :callback {:params [{:name "success", :type "boolean"}]}}]}
    {:id ::override-user-agent,
     :name "overrideUserAgent",
     :params [{:name "instance-id", :type "integer"} {:name "user-agent-override", :type "string"}]}
    {:id ::reload, :name "reload", :params [{:name "instance-id", :type "integer"}]}
    {:id ::set-allow-transparency,
     :name "setAllowTransparency",
     :since "39",
     :params [{:name "instance-id", :type "integer"} {:name "allow", :type "boolean"}]}
    {:id ::set-allow-scaling,
     :name "setAllowScaling",
     :since "42",
     :params [{:name "instance-id", :type "integer"} {:name "allow", :type "boolean"}]}
    {:id ::set-name,
     :name "setName",
     :params [{:name "instance-id", :type "integer"} {:name "frame-name", :type "string"}]}
    {:id ::set-permission,
     :name "setPermission",
     :callback? true,
     :params
     [{:name "instance-id", :type "integer"}
      {:name "request-id", :type "integer"}
      {:name "action", :type "unknown-type"}
      {:name "user-input", :optional? true, :type "string"}
      {:name "callback", :optional? true, :type :callback, :callback {:params [{:name "allowed", :type "boolean"}]}}]}
    {:id ::navigate, :name "navigate", :params [{:name "instance-id", :type "integer"} {:name "src", :type "string"}]}
    {:id ::stop, :name "stop", :params [{:name "instance-id", :type "integer"}]}
    {:id ::terminate, :name "terminate", :params [{:name "instance-id", :type "integer"}]}
    {:id ::capture-visible-region,
     :name "captureVisibleRegion",
     :callback? true,
     :params
     [{:name "instance-id", :type "integer"}
      {:name "options", :optional? true, :type "object"}
      {:name "callback", :type :callback, :callback {:params [{:name "data-url", :type "string"}]}}]}
    {:id ::clear-data,
     :name "clearData",
     :callback? true,
     :params
     [{:name "instance-id", :type "integer"}
      {:name "options", :type "object"}
      {:name "data-to-remove", :type "object"}
      {:name "callback", :optional? true, :type :callback}]}]})

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