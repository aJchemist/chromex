(ns chromex.ext.printer-provider (:require-macros [chromex.ext.printer-provider :refer [gen-wrap]])
    (:require [chromex.core]))

; -- events -----------------------------------------------------------------------------------------------------------------

(defn on-get-printers-requested* [config channel & args]
  (gen-wrap :event ::on-get-printers-requested config channel args))
(defn on-get-usb-printer-info-requested* [config channel & args]
  (gen-wrap :event ::on-get-usb-printer-info-requested config channel args))
(defn on-get-capability-requested* [config channel & args]
  (gen-wrap :event ::on-get-capability-requested config channel args))
(defn on-print-requested* [config channel & args]
  (gen-wrap :event ::on-print-requested config channel args))

