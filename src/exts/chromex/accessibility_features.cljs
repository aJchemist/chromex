(ns chromex.accessibility-features (:require-macros [chromex.accessibility-features :refer [gen-wrap]])
    (:require [chromex-lib.core]))

; -- properties -------------------------------------------------------------------------------------------------------------

(defn spoken-feedback* [config]
  (gen-wrap :property ::spoken-feedback config))

(defn large-cursor* [config]
  (gen-wrap :property ::large-cursor config))

(defn sticky-keys* [config]
  (gen-wrap :property ::sticky-keys config))

(defn high-contrast* [config]
  (gen-wrap :property ::high-contrast config))

(defn screen-magnifier* [config]
  (gen-wrap :property ::screen-magnifier config))

(defn autoclick* [config]
  (gen-wrap :property ::autoclick config))

(defn virtual-keyboard* [config]
  (gen-wrap :property ::virtual-keyboard config))

(defn animation-policy* [config]
  (gen-wrap :property ::animation-policy config))

