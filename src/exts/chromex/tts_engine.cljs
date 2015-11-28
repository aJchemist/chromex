(ns chromex.tts-engine (:require-macros [chromex.tts-engine :refer [gen-wrap]])
    (:require [chromex-lib.core]))

; -- events -----------------------------------------------------------------------------------------------------------------

(defn on-speak* [config channel & args]
  (gen-wrap :event ::on-speak config channel args))
(defn on-stop* [config channel & args]
  (gen-wrap :event ::on-stop config channel args))
(defn on-pause* [config channel & args]
  (gen-wrap :event ::on-pause config channel args))
(defn on-resume* [config channel & args]
  (gen-wrap :event ::on-resume config channel args))

