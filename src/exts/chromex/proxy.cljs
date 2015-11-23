(ns chromex.proxy (:require-macros [chromex.proxy :refer [gen-wrap]])
    (:require [chromex-lib.core]))

; -- properties -------------------------------------------------------------------------------------------------------------

(defn settings* [config]
  (gen-wrap :property ::settings config))

; -- events -----------------------------------------------------------------------------------------------------------------

(defn on-proxy-error* [config channel]
  (gen-wrap :event ::on-proxy-error config channel))

