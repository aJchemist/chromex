(ns chromex.ext.input-method-private (:require-macros [chromex.ext.input-method-private :refer [gen-wrap]])
    (:require [chromex-lib.core]))

; -- functions --------------------------------------------------------------------------------------------------------------

(defn get-input-method-config* [config]
  (gen-wrap :function ::get-input-method-config config))

(defn get-input-methods* [config]
  (gen-wrap :function ::get-input-methods config))

(defn get-current-input-method* [config]
  (gen-wrap :function ::get-current-input-method config))

(defn set-current-input-method* [config input-method-id]
  (gen-wrap :function ::set-current-input-method config input-method-id))

(defn fetch-all-dictionary-words* [config]
  (gen-wrap :function ::fetch-all-dictionary-words config))

(defn add-word-to-dictionary* [config word]
  (gen-wrap :function ::add-word-to-dictionary config word))

(defn get-encrypt-sync-enabled* [config]
  (gen-wrap :function ::get-encrypt-sync-enabled config))

; -- events -----------------------------------------------------------------------------------------------------------------

(defn on-changed* [config channel & args]
  (gen-wrap :event ::on-changed config channel args))
(defn on-composition-bounds-changed* [config channel & args]
  (gen-wrap :event ::on-composition-bounds-changed config channel args))
(defn on-dictionary-loaded* [config channel & args]
  (gen-wrap :event ::on-dictionary-loaded config channel args))
(defn on-dictionary-changed* [config channel & args]
  (gen-wrap :event ::on-dictionary-changed config channel args))

