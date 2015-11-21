(ns chromex.cast.streaming.session
  "The chrome.cast.streaming.session API creates a Cast
   session using WebMediaStreamTrack as sources. The session is composed
   by RTP streams and a network transport.
   
   Calling this API will generate corresponding resources for use with
   chrome.cast.streaming.rtpStream and chrome.cast.streaming.udpTransport
   APIs.
   
   Valid resource IDs are positive and non-zero.
   
     * available since Chrome 48
     * https://developer.chrome.com/extensions/cast.streaming.session"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex-lib.wrapgen :refer [gen-wrap-from-table]]
            [chromex-lib.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex-lib.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions ------------------------------------------------------------------------------------------------------

(defmacro create
  "Creates a Cast session using the provided audio and video track as source. The tracks must be of type
   MediaStreamTrack. This will create two RTP streams and a UDP transport that builds the session. Either |audioTrack

   or |videoTrack| can be null but not both. This means creating a session with only audio or video. If a given track
   is null then the created stream ID will be null.
   
     |audioTrack| - the source audio track.
     |videoTrack| - the source video track.
     |callback| - Called when the sesion has been created."
  [audio-track video-track #_callback]
  (gen-call :function ::create (meta &form) audio-track video-track))

; -- convenience ----------------------------------------------------------------------------------------------------

(defmacro tap-all-events [chan]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (gen-tap-all-call static-config api-table (meta &form) config chan)))

; -------------------------------------------------------------------------------------------------------------------
; -- API TABLE ------------------------------------------------------------------------------------------------------
; -------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.cast.streaming.session",
   :since "48",
   :functions
   [{:id ::create,
     :name "create",
     :callback? true,
     :params
     [{:name "audio-track", :type "MediaStreamTrack"}
      {:name "video-track", :type "MediaStreamTrack"}
      {:name "callback",
       :type :callback,
       :callback
       {:params
        [{:name "audio-stream-id", :type "integer"}
         {:name "video-stream-id", :type "integer"}
         {:name "udp-transport-id", :type "integer"}]}}]}]})

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