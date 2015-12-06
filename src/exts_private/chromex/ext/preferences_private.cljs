(ns chromex.ext.preferences-private (:require-macros [chromex.ext.preferences-private :refer [gen-wrap]])
    (:require [chromex.core]))

; -- properties -------------------------------------------------------------------------------------------------------------

(defn easy-unlock-proximity-required* [config]
  (gen-wrap :property ::easy-unlock-proximity-required config))

(defn google-geolocation-access-enabled* [config]
  (gen-wrap :property ::google-geolocation-access-enabled config))

(defn data-reduction-update-daily-lengths* [config]
  (gen-wrap :property ::data-reduction-update-daily-lengths config))

; -- functions --------------------------------------------------------------------------------------------------------------

(defn get-sync-categories-without-passphrase* [config]
  (gen-wrap :function ::get-sync-categories-without-passphrase config))

