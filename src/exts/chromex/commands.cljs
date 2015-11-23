(ns chromex.commands (:require-macros [chromex.commands :refer [gen-wrap]])
    (:require [chromex-lib.core]))

; -- functions --------------------------------------------------------------------------------------------------------------

(defn get-all* [config]
  (gen-wrap :function ::get-all config))

; -- events -----------------------------------------------------------------------------------------------------------------

(defn on-command* [config channel]
  (gen-wrap :event ::on-command config channel))

