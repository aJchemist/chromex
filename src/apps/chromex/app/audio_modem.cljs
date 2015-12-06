(ns chromex.app.audio-modem (:require-macros [chromex.app.audio-modem :refer [gen-wrap]])
    (:require [chromex.core]))

; -- functions --------------------------------------------------------------------------------------------------------------

(defn transmit* [config params token]
  (gen-wrap :function ::transmit config params token))

(defn stop-transmit* [config band]
  (gen-wrap :function ::stop-transmit config band))

(defn receive* [config params]
  (gen-wrap :function ::receive config params))

(defn stop-receive* [config band]
  (gen-wrap :function ::stop-receive config band))

; -- events -----------------------------------------------------------------------------------------------------------------

(defn on-received* [config channel & args]
  (gen-wrap :event ::on-received config channel args))
(defn on-transmit-fail* [config channel & args]
  (gen-wrap :event ::on-transmit-fail config channel args))

