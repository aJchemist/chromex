(ns chromex.ext.input.ime
  "Use the chrome.input.ime API to implement a custom IME for Chrome OS. This allows your extension to handle keystrokes, set
   the composition, and manage the candidate window.
   
     * available since Chrome 21
     * https://developer.chrome.com/extensions/input.ime"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex.wrapgen :refer [gen-wrap-from-table]]
            [chromex.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro set-composition
  "Set the current composition. If this extension does not own the active IME, this fails.
   
     |callback| - Called when the operation completes with a boolean indicating if the text was accepted or not. On failure,
                  chrome.runtime.lastError is set.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([parameters #_callback] (gen-call :function ::set-composition &form parameters)))

(defmacro clear-composition
  "Clear the current composition. If this extension does not own the active IME, this fails.
   
     |callback| - Called when the operation completes with a boolean indicating if the text was accepted or not. On failure,
                  chrome.runtime.lastError is set.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([parameters #_callback] (gen-call :function ::clear-composition &form parameters)))

(defmacro commit-text
  "Commits the provided text to the current input.
   
     |callback| - Called when the operation completes with a boolean indicating if the text was accepted or not. On failure,
                  chrome.runtime.lastError is set.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([parameters #_callback] (gen-call :function ::commit-text &form parameters)))

(defmacro send-key-events
  "Sends the key events.  This function is expected to be used by virtual keyboards.  When key(s) on a virtual keyboard is
   pressed by a user, this function is used to propagate that event to the system.
   
     |callback| - Called when the operation completes.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([parameters #_callback] (gen-call :function ::send-key-events &form parameters)))

(defmacro hide-input-view
  "Hides the input view window, which is popped up automatically by system. If the input view window is already hidden, this
   function will do nothing."
  ([] (gen-call :function ::hide-input-view &form)))

(defmacro set-candidate-window-properties
  "Sets the properties of the candidate window. This fails if the extension doesn't own the active IME
   
     |callback| - Called when the operation completes.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([parameters #_callback] (gen-call :function ::set-candidate-window-properties &form parameters)))

(defmacro set-candidates
  "Sets the current candidate list. This fails if this extension doesn't own the active IME
   
     |callback| - Called when the operation completes.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([parameters #_callback] (gen-call :function ::set-candidates &form parameters)))

(defmacro set-cursor-position
  "Set the position of the cursor in the candidate window. This is a no-op if this extension does not own the active IME.
   
     |callback| - Called when the operation completes
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([parameters #_callback] (gen-call :function ::set-cursor-position &form parameters)))

(defmacro set-menu-items
  "Adds the provided menu items to the language menu when this IME is active.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([parameters #_callback] (gen-call :function ::set-menu-items &form parameters)))

(defmacro update-menu-items
  "Updates the state of the MenuItems specified
   
     |callback| - Called when the operation completes
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([parameters #_callback] (gen-call :function ::update-menu-items &form parameters)))

(defmacro delete-surrounding-text
  "Deletes the text around the caret.
   
     |callback| - Called when the operation completes.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([parameters #_callback] (gen-call :function ::delete-surrounding-text &form parameters)))

(defmacro key-event-handled
  "Indicates that the key event received by onKeyEvent is handled.  This should only be called if the onKeyEvent listener is
   asynchronous.
   
     |requestId| - Request id of the event that was handled.  This should come from keyEvent.requestId
     |response| - True if the keystroke was handled, false if not"
  ([request-id response] (gen-call :function ::key-event-handled &form request-id response)))

(defmacro create-window
  "Creates IME window.
   
     |options| - The options of the newly created IME window.
     |callback| - Called when the operation completes.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([options #_callback] (gen-call :function ::create-window &form options)))

(defmacro activate
  "Activates the IME extension so that it can receive events.
   
     |callback| - Called when the operation completes.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::activate &form)))

(defmacro deactivate
  "Deactivates the IME extension so that it cannot receive events.
   
     |callback| - Called when the operation completes.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::deactivate &form)))

; -- events -----------------------------------------------------------------------------------------------------------------
;
; docs: https://github.com/binaryage/chromex/#tapping-events

(defmacro tap-on-activate-events
  "This event is sent when an IME is activated. It signals that the IME will be receiving onKeyPress events.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-activate &form channel args)))

(defmacro tap-on-deactivated-events
  "This event is sent when an IME is deactivated. It signals that the IME will no longer be receiving onKeyPress events.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-deactivated &form channel args)))

(defmacro tap-on-focus-events
  "This event is sent when focus enters a text box. It is sent to all extensions that are listening to this event, and enabled
   by the user.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-focus &form channel args)))

(defmacro tap-on-blur-events
  "This event is sent when focus leaves a text box. It is sent to all extensions that are listening to this event, and enabled
   by the user.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-blur &form channel args)))

(defmacro tap-on-input-context-update-events
  "This event is sent when the properties of the current InputContext change, such as the the type. It is sent to all
   extensions that are listening to this event, and enabled by the user.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-input-context-update &form channel args)))

(defmacro tap-on-key-event-events
  "This event is sent if this extension owns the active IME.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-key-event &form channel args)))

(defmacro tap-on-candidate-clicked-events
  "This event is sent if this extension owns the active IME.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-candidate-clicked &form channel args)))

(defmacro tap-on-menu-item-activated-events
  "Called when the user selects a menu item
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-menu-item-activated &form channel args)))

(defmacro tap-on-surrounding-text-changed-events
  "Called when the editable string around caret is changed or when the caret position is moved. The text length is limited to
   100 characters for each back and forth direction.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-surrounding-text-changed &form channel args)))

(defmacro tap-on-reset-events
  "This event is sent when chrome terminates ongoing text input session.
   Events will be put on the |channel|.
   
   Note: |args| will be passed as additional parameters into Chrome event's .addListener call."
  ([channel & args] (apply gen-call :event ::on-reset &form channel args)))

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
  {:namespace "chrome.input.ime",
   :since "21",
   :functions
   [{:id ::set-composition,
     :name "setComposition",
     :callback? true,
     :params
     [{:name "parameters", :type "object"}
      {:name "callback", :optional? true, :type :callback, :callback {:params [{:name "success", :type "boolean"}]}}]}
    {:id ::clear-composition,
     :name "clearComposition",
     :callback? true,
     :params
     [{:name "parameters", :type "object"}
      {:name "callback", :optional? true, :type :callback, :callback {:params [{:name "success", :type "boolean"}]}}]}
    {:id ::commit-text,
     :name "commitText",
     :callback? true,
     :params
     [{:name "parameters", :type "object"}
      {:name "callback", :optional? true, :type :callback, :callback {:params [{:name "success", :type "boolean"}]}}]}
    {:id ::send-key-events,
     :name "sendKeyEvents",
     :since "33",
     :callback? true,
     :params [{:name "parameters", :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::hide-input-view, :name "hideInputView", :since "34"}
    {:id ::set-candidate-window-properties,
     :name "setCandidateWindowProperties",
     :callback? true,
     :params
     [{:name "parameters", :type "object"}
      {:name "callback", :optional? true, :type :callback, :callback {:params [{:name "success", :type "boolean"}]}}]}
    {:id ::set-candidates,
     :name "setCandidates",
     :callback? true,
     :params
     [{:name "parameters", :type "object"}
      {:name "callback", :optional? true, :type :callback, :callback {:params [{:name "success", :type "boolean"}]}}]}
    {:id ::set-cursor-position,
     :name "setCursorPosition",
     :callback? true,
     :params
     [{:name "parameters", :type "object"}
      {:name "callback", :optional? true, :type :callback, :callback {:params [{:name "success", :type "boolean"}]}}]}
    {:id ::set-menu-items,
     :name "setMenuItems",
     :callback? true,
     :params [{:name "parameters", :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::update-menu-items,
     :name "updateMenuItems",
     :callback? true,
     :params [{:name "parameters", :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::delete-surrounding-text,
     :name "deleteSurroundingText",
     :since "27",
     :callback? true,
     :params [{:name "parameters", :type "object"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::key-event-handled,
     :name "keyEventHandled",
     :since "25",
     :params [{:name "request-id", :type "string"} {:name "response", :type "boolean"}]}
    {:id ::create-window,
     :name "createWindow",
     :since "master",
     :callback? true,
     :params
     [{:name "options", :type "input.ime.CreateWindowOptions"}
      {:name "callback", :type :callback, :callback {:params [{:name "window-object", :type "Window"}]}}]}
    {:id ::activate,
     :name "activate",
     :since "master",
     :callback? true,
     :params [{:name "callback", :optional? true, :type :callback}]}
    {:id ::deactivate,
     :name "deactivate",
     :since "master",
     :callback? true,
     :params [{:name "callback", :optional? true, :type :callback}]}],
   :events
   [{:id ::on-activate,
     :name "onActivate",
     :params [{:name "engine-id", :type "string"} {:name "screen", :type "input.ime.ScreenType"}]}
    {:id ::on-deactivated, :name "onDeactivated", :params [{:name "engine-id", :type "string"}]}
    {:id ::on-focus, :name "onFocus", :params [{:name "context", :type "input.ime.InputContext"}]}
    {:id ::on-blur, :name "onBlur", :params [{:name "context-id", :type "integer"}]}
    {:id ::on-input-context-update,
     :name "onInputContextUpdate",
     :params [{:name "context", :type "input.ime.InputContext"}]}
    {:id ::on-key-event,
     :name "onKeyEvent",
     :params [{:name "engine-id", :type "string"} {:name "key-data", :type "input.ime.KeyboardEvent"}]}
    {:id ::on-candidate-clicked,
     :name "onCandidateClicked",
     :params
     [{:name "engine-id", :type "string"}
      {:name "candidate-id", :type "integer"}
      {:name "button", :type "input.ime.MouseButton"}]}
    {:id ::on-menu-item-activated,
     :name "onMenuItemActivated",
     :params [{:name "engine-id", :type "string"} {:name "name", :type "string"}]}
    {:id ::on-surrounding-text-changed,
     :name "onSurroundingTextChanged",
     :since "27",
     :params [{:name "engine-id", :type "string"} {:name "surrounding-info", :type "object"}]}
    {:id ::on-reset, :name "onReset", :since "29", :params [{:name "engine-id", :type "string"}]}]})

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