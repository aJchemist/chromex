(ns chromex.app.file-system-provider (:require-macros [chromex.app.file-system-provider :refer [gen-wrap]])
    (:require [chromex.core]))

; -- functions --------------------------------------------------------------------------------------------------------------

(defn mount* [config options]
  (gen-wrap :function ::mount config options))

(defn unmount* [config options]
  (gen-wrap :function ::unmount config options))

(defn get-all* [config]
  (gen-wrap :function ::get-all config))

(defn get* [config file-system-id]
  (gen-wrap :function ::get config file-system-id))

(defn notify* [config options]
  (gen-wrap :function ::notify config options))

; -- events -----------------------------------------------------------------------------------------------------------------

(defn on-unmount-requested* [config channel & args]
  (gen-wrap :event ::on-unmount-requested config channel args))

(defn on-get-metadata-requested* [config channel & args]
  (gen-wrap :event ::on-get-metadata-requested config channel args))

(defn on-get-actions-requested* [config channel & args]
  (gen-wrap :event ::on-get-actions-requested config channel args))

(defn on-read-directory-requested* [config channel & args]
  (gen-wrap :event ::on-read-directory-requested config channel args))

(defn on-open-file-requested* [config channel & args]
  (gen-wrap :event ::on-open-file-requested config channel args))

(defn on-close-file-requested* [config channel & args]
  (gen-wrap :event ::on-close-file-requested config channel args))

(defn on-read-file-requested* [config channel & args]
  (gen-wrap :event ::on-read-file-requested config channel args))

(defn on-create-directory-requested* [config channel & args]
  (gen-wrap :event ::on-create-directory-requested config channel args))

(defn on-delete-entry-requested* [config channel & args]
  (gen-wrap :event ::on-delete-entry-requested config channel args))

(defn on-create-file-requested* [config channel & args]
  (gen-wrap :event ::on-create-file-requested config channel args))

(defn on-copy-entry-requested* [config channel & args]
  (gen-wrap :event ::on-copy-entry-requested config channel args))

(defn on-move-entry-requested* [config channel & args]
  (gen-wrap :event ::on-move-entry-requested config channel args))

(defn on-truncate-requested* [config channel & args]
  (gen-wrap :event ::on-truncate-requested config channel args))

(defn on-write-file-requested* [config channel & args]
  (gen-wrap :event ::on-write-file-requested config channel args))

(defn on-abort-requested* [config channel & args]
  (gen-wrap :event ::on-abort-requested config channel args))

(defn on-configure-requested* [config channel & args]
  (gen-wrap :event ::on-configure-requested config channel args))

(defn on-mount-requested* [config channel & args]
  (gen-wrap :event ::on-mount-requested config channel args))

(defn on-add-watcher-requested* [config channel & args]
  (gen-wrap :event ::on-add-watcher-requested config channel args))

(defn on-remove-watcher-requested* [config channel & args]
  (gen-wrap :event ::on-remove-watcher-requested config channel args))

(defn on-execute-action-requested* [config channel & args]
  (gen-wrap :event ::on-execute-action-requested config channel args))

