(ns chromex.playground
  (:require-macros [chromex.playground :refer [gen-wrap]])
  (:require [chromex-lib.core]
            [chromex.test.marshalling]))

; -- properties -----------------------------------------------------------------------------------------------------

(defn some-prop* [config]
  (gen-wrap :property ::some-prop config))

; -- functions ------------------------------------------------------------------------------------------------------

(defn get-something* [config param1]
  (gen-wrap :function ::get-something config param1))

(defn do-something* [config param1]
  (gen-wrap :function ::do-something config param1))

; -- events ---------------------------------------------------------------------------------------------------------

(defn on-something* [config chan]
  (gen-wrap :event ::on-something config chan))