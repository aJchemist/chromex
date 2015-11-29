(ns chromex.identity-private (:require-macros [chromex.identity-private :refer [gen-wrap]])
    (:require [chromex-lib.core]))

; -- events -----------------------------------------------------------------------------------------------------------------

(defn on-web-flow-request* [config channel & args]
  (gen-wrap :event ::on-web-flow-request config channel args))

