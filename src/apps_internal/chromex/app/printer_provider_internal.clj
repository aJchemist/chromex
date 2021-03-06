(ns chromex.app.printer-provider-internal
  "printerProviderInternal
   Internal API used to run callbacks passed to chrome.printerProvider API
   events.
   When dispatching a chrome.printerProvider API event, its arguments will be
   massaged in custom bindings so a callback is added. The callback uses
   chrome.printerProviderInternal API to report the event results.
   In order to identify the event for which the callback is called, the event
   is internally dispatched having a requestId argument (which is removed from
   the argument list before the event actually reaches the event listeners). The
   requestId is forwarded to the chrome.printerProviderInternal API functions.

     * available since Chrome 44"

  (:refer-clojure :only [defmacro defn apply declare meta let partial])
  (:require [chromex.wrapgen :refer [gen-wrap-helper]]
            [chromex.callgen :refer [gen-call-helper gen-tap-all-events-call]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro report-printers
  "Runs callback to printerProvider.onGetPrintersRequested event.

     |request-id| - Parameter identifying the event instance for which the     callback is run.
     |printers| - List of printers reported by the extension."
  ([request-id printers] (gen-call :function ::report-printers &form request-id printers))
  ([request-id] `(report-printers ~request-id :omit)))

(defmacro report-usb-printer-info
  "Runs callback to printerProvider.onUsbAccessGranted event.

     |request-id| - Parameter identifying the event instance for which the     callback is run.
     |printer-info| - Printer information reported by the extension."
  ([request-id printer-info] (gen-call :function ::report-usb-printer-info &form request-id printer-info))
  ([request-id] `(report-usb-printer-info ~request-id :omit)))

(defmacro report-printer-capability
  "Runs callback to printerProvider.onGetCapabilityRequested event.

     |request-id| - ?
     |capability| - ?"
  ([request-id capability] (gen-call :function ::report-printer-capability &form request-id capability))
  ([request-id] `(report-printer-capability ~request-id :omit)))

(defmacro report-print-result
  "Runs callback to printerProvider.onPrintRequested event.

     |request-id| - ?
     |error| - The requested print job result."
  ([request-id error] (gen-call :function ::report-print-result &form request-id error))
  ([request-id] `(report-print-result ~request-id :omit)))

(defmacro get-print-data
  "Gets information needed to create a print data blob for a print request. The blob will be dispatched to the extension via
   printerProvider.onPrintRequested event.

     |request-id| - The request id for the print request for which data is     needed.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [blob-info] where:

     |blob-info| - ?

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error."
  ([request-id] (gen-call :function ::get-print-data &form request-id)))

; -- convenience ------------------------------------------------------------------------------------------------------------

(defmacro tap-all-events
  "Taps all valid non-deprecated events in chromex.app.printer-provider-internal namespace."
  [chan]
  (gen-tap-all-events-call api-table (meta &form) chan))

; ---------------------------------------------------------------------------------------------------------------------------
; -- API TABLE --------------------------------------------------------------------------------------------------------------
; ---------------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.printerProviderInternal",
   :since "44",
   :functions
   [{:id ::report-printers,
     :name "reportPrinters",
     :params
     [{:name "request-id", :type "integer"}
      {:name "printers", :optional? true, :type "[array-of-printerProvider.PrinterInfos]"}]}
    {:id ::report-usb-printer-info,
     :name "reportUsbPrinterInfo",
     :since "45",
     :params
     [{:name "request-id", :type "integer"}
      {:name "printer-info", :optional? true, :type "printerProvider.PrinterInfo"}]}
    {:id ::report-printer-capability,
     :name "reportPrinterCapability",
     :params [{:name "request-id", :type "integer"} {:name "capability", :optional? true, :type "object"}]}
    {:id ::report-print-result,
     :name "reportPrintResult",
     :params [{:name "request-id", :type "integer"} {:name "error", :optional? true, :type "unknown-type"}]}
    {:id ::get-print-data,
     :name "getPrintData",
     :callback? true,
     :params
     [{:name "request-id", :type "integer"}
      {:name "callback", :type :callback, :callback {:params [{:name "blob-info", :type "object"}]}}]}]})

; -- helpers ----------------------------------------------------------------------------------------------------------------

; code generation for native API wrapper
(defmacro gen-wrap [kind item-id config & args]
  (apply gen-wrap-helper api-table kind item-id config args))

; code generation for API call-site
(def gen-call (partial gen-call-helper api-table))