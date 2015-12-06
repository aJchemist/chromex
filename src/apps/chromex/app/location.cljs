(ns chromex.app.location (:require-macros [chromex.app.location :refer [gen-wrap]])
    (:require [chromex.core]))

; -- functions --------------------------------------------------------------------------------------------------------------

(defn watch-location* [config name request-info]
  (gen-wrap :function ::watch-location config name request-info))

(defn clear-watch* [config name]
  (gen-wrap :function ::clear-watch config name))

; -- events -----------------------------------------------------------------------------------------------------------------

(defn on-location-update* [config channel & args]
  (gen-wrap :event ::on-location-update config channel args))
(defn on-location-error* [config channel & args]
  (gen-wrap :event ::on-location-error config channel args))

