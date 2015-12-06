(ns chromex.app.certificate-provider-internal (:require-macros [chromex.app.certificate-provider-internal :refer [gen-wrap]])
    (:require [chromex-lib.core]))

; -- functions --------------------------------------------------------------------------------------------------------------

(defn report-signature* [config request-id signature]
  (gen-wrap :function ::report-signature config request-id signature))

(defn report-certificates* [config request-id certificates]
  (gen-wrap :function ::report-certificates config request-id certificates))

