(ns chromex.app.system.display (:require-macros [chromex.app.system.display :refer [gen-wrap]])
    (:require [chromex.core]))

; -- functions --------------------------------------------------------------------------------------------------------------

(defn get-info* [config]
  (gen-wrap :function ::get-info config))

(defn set-display-properties* [config id info]
  (gen-wrap :function ::set-display-properties config id info))

(defn enable-unified-desktop* [config enabled]
  (gen-wrap :function ::enable-unified-desktop config enabled))

(defn overscan-calibration-start* [config id]
  (gen-wrap :function ::overscan-calibration-start config id))

(defn overscan-calibration-adjust* [config id delta]
  (gen-wrap :function ::overscan-calibration-adjust config id delta))

(defn overscan-calibration-reset* [config id]
  (gen-wrap :function ::overscan-calibration-reset config id))

(defn overscan-calibration-complete* [config id]
  (gen-wrap :function ::overscan-calibration-complete config id))

; -- events -----------------------------------------------------------------------------------------------------------------

(defn on-display-changed* [config channel & args]
  (gen-wrap :event ::on-display-changed config channel args))

