(ns chromex.enterprise.platform-keys (:require-macros [chromex.enterprise.platform-keys :refer [gen-wrap]])
    (:require [chromex-lib.core]))

; -- functions ------------------------------------------------------------------------------------------------------

(defn get-tokens* [config]
  (gen-wrap :function ::get-tokens config))

(defn get-certificates* [config token-id]
  (gen-wrap :function ::get-certificates config token-id))

(defn import-certificate* [config token-id certificate]
  (gen-wrap :function ::import-certificate config token-id certificate))

(defn remove-certificate* [config token-id certificate]
  (gen-wrap :function ::remove-certificate config token-id certificate))

