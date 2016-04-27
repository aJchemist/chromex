(ns chromex.app.audio-modem
  "Use the chrome.audio_modem API
   to transmit and receive short tokens over audio.

     * available since Chrome 52
     * https://developer.chrome.com/apps/audioModem"

  (:refer-clojure :only [defmacro defn apply declare meta let partial])
  (:require [chromex.wrapgen :refer [gen-wrap-helper]]
            [chromex.callgen :refer [gen-call-helper gen-tap-all-events-call]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro transmit
  "Transmit a token. Only one can be transmitted at a time. Transmission of any previous tokens (by this app) will stop.

     |params| - https://developer.chrome.com/apps/audioModem#property-transmit-params.
     |token| - https://developer.chrome.com/apps/audioModem#property-transmit-token.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [status] where:

     |status| - https://developer.chrome.com/apps/audioModem#property-callback-status.

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/audioModem#method-transmit."
  ([params token] (gen-call :function ::transmit &form params token)))

(defmacro stop-transmit
  "Stop any active transmission on the specified band.

     |band| - https://developer.chrome.com/apps/audioModem#property-stopTransmit-band.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [status] where:

     |status| - https://developer.chrome.com/apps/audioModem#property-callback-status.

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/audioModem#method-stopTransmit."
  ([band] (gen-call :function ::stop-transmit &form band)))

(defmacro receive
  "Start listening for audio tokens. For now, only one app will be able to listen at a time.

     |params| - https://developer.chrome.com/apps/audioModem#property-receive-params.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [status] where:

     |status| - https://developer.chrome.com/apps/audioModem#property-callback-status.

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/audioModem#method-receive."
  ([params] (gen-call :function ::receive &form params)))

(defmacro stop-receive
  "Stop any active listening on the specified band.

     |band| - https://developer.chrome.com/apps/audioModem#property-stopReceive-band.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [status] where:

     |status| - https://developer.chrome.com/apps/audioModem#property-callback-status.

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/audioModem#method-stopReceive."
  ([band] (gen-call :function ::stop-receive &form band)))

; -- events -----------------------------------------------------------------------------------------------------------------
;
; docs: https://github.com/binaryage/chromex/#tapping-events

(defmacro tap-on-received-events
  "Audio tokens have been received.

   Events will be put on the |channel| with signature [::on-received [tokens]] where:

     |tokens| - https://developer.chrome.com/apps/audioModem#property-onReceived-tokens.

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call.

   https://developer.chrome.com/apps/audioModem#event-onReceived."
  ([channel & args] (apply gen-call :event ::on-received &form channel args)))

(defmacro tap-on-transmit-fail-events
  "Transmit could not be confirmed. The speaker volume might be too low.

   Events will be put on the |channel| with signature [::on-transmit-fail [band]] where:

     |band| - https://developer.chrome.com/apps/audioModem#property-onTransmitFail-band.

   Note: |args| will be passed as additional parameters into Chrome event's .addListener call.

   https://developer.chrome.com/apps/audioModem#event-onTransmitFail."
  ([channel & args] (apply gen-call :event ::on-transmit-fail &form channel args)))

; -- convenience ------------------------------------------------------------------------------------------------------------

(defmacro tap-all-events
  "Taps all valid non-deprecated events in chromex.app.audio-modem namespace."
  [chan]
  (gen-tap-all-events-call api-table (meta &form) chan))

; ---------------------------------------------------------------------------------------------------------------------------
; -- API TABLE --------------------------------------------------------------------------------------------------------------
; ---------------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.audioModem",
   :since "52",
   :functions
   [{:id ::transmit,
     :name "transmit",
     :callback? true,
     :params
     [{:name "params", :type "audioModem.RequestParams"}
      {:name "token", :type "ArrayBuffer"}
      {:name "callback", :type :callback, :callback {:params [{:name "status", :type "audioModem.Status"}]}}]}
    {:id ::stop-transmit,
     :name "stopTransmit",
     :callback? true,
     :params
     [{:name "band", :type "audioModem.Audioband"}
      {:name "callback", :type :callback, :callback {:params [{:name "status", :type "audioModem.Status"}]}}]}
    {:id ::receive,
     :name "receive",
     :callback? true,
     :params
     [{:name "params", :type "audioModem.RequestParams"}
      {:name "callback", :type :callback, :callback {:params [{:name "status", :type "audioModem.Status"}]}}]}
    {:id ::stop-receive,
     :name "stopReceive",
     :callback? true,
     :params
     [{:name "band", :type "audioModem.Audioband"}
      {:name "callback", :type :callback, :callback {:params [{:name "status", :type "audioModem.Status"}]}}]}],
   :events
   [{:id ::on-received, :name "onReceived", :params [{:name "tokens", :type "[array-of-objects]"}]}
    {:id ::on-transmit-fail, :name "onTransmitFail", :params [{:name "band", :type "audioModem.Audioband"}]}]})

; -- helpers ----------------------------------------------------------------------------------------------------------------

; code generation for native API wrapper
(defmacro gen-wrap [kind item-id config & args]
  (apply gen-wrap-helper api-table kind item-id config args))

; code generation for API call-site
(def gen-call (partial gen-call-helper api-table))