(ns chromex.hotword-private
  "The chrome.hotwordPrivate API allows extensions to access and
   mutate the preference for enabling hotword search. It also provides
   information on whether the hotword search is available. This API provides an
   event interface to transmit to the extension a signal that the preference fo
   hotword search has change.
   
   For an FYI, visit http://goo.gl/AyHbkH
   
     * available since Chrome 34
     * https://developer.chrome.com/extensions/hotwordPrivate"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex-lib.wrapgen :refer [gen-wrap-from-table]]
            [chromex-lib.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex-lib.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions ------------------------------------------------------------------------------------------------------

(defmacro set-enabled
  "Sets the current enabled state of hotword search. True: enable hotword search. False: disable hotword search.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([state #_callback] (gen-call :function ::set-enabled &form state)))

(defmacro get-status
  "Retrieves the current state of hotword search. The result is put into a StatusDetails object.
   
     |getOptionalFields| - If true, fills in fields tagged as optional in StatusDetails with valid values. These
                           fields are not valid by default since their current implementations may cause blocking
                           operations.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([get-optional-fields #_callback] (gen-call :function ::get-status &form get-optional-fields))
  ([] `(get-status :omit)))

(defmacro get-localized-strings
  "Retrieves a dictionary mapping names to localized resource strings.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::get-localized-strings &form)))

(defmacro set-audio-logging-enabled
  "Sets the current enabled state of audio logging in the extension. True: logging enabled. False: no logging.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([state #_callback] (gen-call :function ::set-audio-logging-enabled &form state)))

(defmacro set-hotword-always-on-search-enabled
  "Sets the current enabled state of hotword-always-on-search pref. True: enable hotword always on search. False:
   disable hotword always on search.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([state #_callback] (gen-call :function ::set-hotword-always-on-search-enabled &form state)))

(defmacro set-hotword-session-state
  "Sets the current state of the browser-requested hotword session.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([started #_callback] (gen-call :function ::set-hotword-session-state &form started)))

(defmacro notify-hotword-recognition
  "Notifies that a hotword has been recognized in the browser-requested hotword session.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([type log #_callback] (gen-call :function ::notify-hotword-recognition &form type log))
  ([type] `(notify-hotword-recognition ~type :omit)))

(defmacro get-launch-state
  "Retrieves the state that the Hotword Audio Verification app was launched in. The result is put into a LaunchState
   object.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::get-launch-state &form)))

(defmacro start-training
  "Starts the speaker model training.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::start-training &form)))

(defmacro finalize-speaker-model
  "Finalizess the speaker model.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::finalize-speaker-model &form)))

(defmacro notify-speaker-model-saved
  "Notifies that the speaker model has been saved.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::notify-speaker-model-saved &form)))

(defmacro stop-training
  "Stops the speaker model training.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::stop-training &form)))

(defmacro set-audio-history-enabled
  "Sets the audio history opt-in state.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([enabled #_callback] (gen-call :function ::set-audio-history-enabled &form enabled)))

(defmacro get-audio-history-enabled
  "Gets the audio history opt-in state.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::get-audio-history-enabled &form)))

(defmacro speaker-model-exists-result
  "Sends the result of whether a speaker model exists to the browser.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([exists #_callback] (gen-call :function ::speaker-model-exists-result &form exists)))

; -- events ---------------------------------------------------------------------------------------------------------

(defmacro tap-on-enabled-changed-events
  "Fired when the hotword detector enabled state should be changed. This can be from various sources, e.g. a pref
   change or training a speaker model."
  ([channel] (gen-call :event ::on-enabled-changed &form channel)))

(defmacro tap-on-hotword-session-requested-events
  "Fired when the browser wants to start a hotword session."
  ([channel] (gen-call :event ::on-hotword-session-requested &form channel)))

(defmacro tap-on-hotword-session-stopped-events
  "Fired when the browser wants to stop the requested hotword session."
  ([channel] (gen-call :event ::on-hotword-session-stopped &form channel)))

(defmacro tap-on-finalize-speaker-model-events
  "Fired when the speaker model should be finalized."
  ([channel] (gen-call :event ::on-finalize-speaker-model &form channel)))

(defmacro tap-on-speaker-model-saved-events
  "Fired when the speaker model has been saved."
  ([channel] (gen-call :event ::on-speaker-model-saved &form channel)))

(defmacro tap-on-hotword-triggered-events
  "Fired when a hotword has triggered."
  ([channel] (gen-call :event ::on-hotword-triggered &form channel)))

(defmacro tap-on-delete-speaker-model-events
  "Fired when the speaker model should be deleted."
  ([channel] (gen-call :event ::on-delete-speaker-model &form channel)))

(defmacro tap-on-speaker-model-exists-events
  "Fired when the browser wants to find out whether the speaker model exists."
  ([channel] (gen-call :event ::on-speaker-model-exists &form channel)))

(defmacro tap-on-microphone-state-changed-events
  "Fired when the microphone state changes."
  ([channel] (gen-call :event ::on-microphone-state-changed &form channel)))

; -- convenience ----------------------------------------------------------------------------------------------------

(defmacro tap-all-events [chan]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (gen-tap-all-call static-config api-table (meta &form) config chan)))

; -------------------------------------------------------------------------------------------------------------------
; -- API TABLE ------------------------------------------------------------------------------------------------------
; -------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.hotwordPrivate",
   :since "34",
   :functions
   [{:id ::set-enabled,
     :name "setEnabled",
     :callback? true,
     :params [{:name "state", :type "boolean"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::get-status,
     :name "getStatus",
     :callback? true,
     :params
     [{:name "get-optional-fields", :optional? true, :type "boolean"}
      {:name "callback", :type :callback, :callback {:params [{:name "result", :type "object"}]}}]}
    {:id ::get-localized-strings,
     :name "getLocalizedStrings",
     :callback? true,
     :params [{:name "callback", :type :callback, :callback {:params [{:name "result", :type "object"}]}}]}
    {:id ::set-audio-logging-enabled,
     :name "setAudioLoggingEnabled",
     :callback? true,
     :params [{:name "state", :type "boolean"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::set-hotword-always-on-search-enabled,
     :name "setHotwordAlwaysOnSearchEnabled",
     :callback? true,
     :params [{:name "state", :type "boolean"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::set-hotword-session-state,
     :name "setHotwordSessionState",
     :callback? true,
     :params [{:name "started", :type "boolean"} {:name "callback", :optional? true, :type :callback}]}
    {:id ::notify-hotword-recognition,
     :name "notifyHotwordRecognition",
     :callback? true,
     :params
     [{:name "type", :type "unknown-type"}
      {:name "log", :optional? true, :type "object"}
      {:name "callback", :optional? true, :type :callback}]}
    {:id ::get-launch-state,
     :name "getLaunchState",
     :callback? true,
     :params [{:name "callback", :type :callback, :callback {:params [{:name "result", :type "object"}]}}]}
    {:id ::start-training,
     :name "startTraining",
     :callback? true,
     :params [{:name "callback", :optional? true, :type :callback}]}
    {:id ::finalize-speaker-model,
     :name "finalizeSpeakerModel",
     :callback? true,
     :params [{:name "callback", :optional? true, :type :callback}]}
    {:id ::notify-speaker-model-saved,
     :name "notifySpeakerModelSaved",
     :callback? true,
     :params [{:name "callback", :optional? true, :type :callback}]}
    {:id ::stop-training,
     :name "stopTraining",
     :callback? true,
     :params [{:name "callback", :optional? true, :type :callback}]}
    {:id ::set-audio-history-enabled,
     :name "setAudioHistoryEnabled",
     :callback? true,
     :params
     [{:name "enabled", :type "boolean"}
      {:name "callback",
       :optional? true,
       :type :callback,
       :callback {:params [{:name "result", :type "hotwordPrivate.AudioHistoryState"}]}}]}
    {:id ::get-audio-history-enabled,
     :name "getAudioHistoryEnabled",
     :callback? true,
     :params
     [{:name "callback",
       :optional? true,
       :type :callback,
       :callback {:params [{:name "result", :type "hotwordPrivate.AudioHistoryState"}]}}]}
    {:id ::speaker-model-exists-result,
     :name "speakerModelExistsResult",
     :since "43",
     :callback? true,
     :params [{:name "exists", :type "boolean"} {:name "callback", :optional? true, :type :callback}]}],
   :events
   [{:id ::on-enabled-changed, :name "onEnabledChanged"}
    {:id ::on-hotword-session-requested, :name "onHotwordSessionRequested"}
    {:id ::on-hotword-session-stopped, :name "onHotwordSessionStopped"}
    {:id ::on-finalize-speaker-model, :name "onFinalizeSpeakerModel"}
    {:id ::on-speaker-model-saved, :name "onSpeakerModelSaved"}
    {:id ::on-hotword-triggered, :name "onHotwordTriggered"}
    {:id ::on-delete-speaker-model, :name "onDeleteSpeakerModel", :since "42"}
    {:id ::on-speaker-model-exists, :name "onSpeakerModelExists", :since "43"}
    {:id ::on-microphone-state-changed,
     :name "onMicrophoneStateChanged",
     :since "44",
     :params [{:name "enabled", :type "boolean"}]}]})

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