(ns chromex.app.i18n
  "Use the chrome.i18n infrastructure to implement internationalization across your whole app or extension.

     * available since Chrome 5
     * https://developer.chrome.com/apps/i18n"

  (:refer-clojure :only [defmacro defn apply declare meta let partial])
  (:require [chromex.wrapgen :refer [gen-wrap-helper]]
            [chromex.callgen :refer [gen-call-helper gen-tap-all-events-call]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro get-accept-languages
  "Gets the accept-languages of the browser. This is different from the locale used by the browser; to get the locale, use
   'i18n.getUILanguage'.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [languages] where:

     |languages| - Array of LanguageCode

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/i18n#method-getAcceptLanguages."
  ([] (gen-call :function ::get-accept-languages &form)))

(defmacro get-message
  "Gets the localized string for the specified message. If the message is missing, this method returns an empty string ('').
   If the format of the getMessage() call is wrong &mdash; for example, messageName is not a string or the substitutions array
   has more than 9 elements &mdash; this method returns undefined.

     |message-name| - The name of the message, as specified in the messages.json file.
     |substitutions| - Up to 9 substitution strings, if the message requires any.

   https://developer.chrome.com/apps/i18n#method-getMessage."
  ([message-name substitutions] (gen-call :function ::get-message &form message-name substitutions))
  ([message-name] `(get-message ~message-name :omit)))

(defmacro get-ui-language
  "Gets the browser UI language of the browser. This is different from 'i18n.getAcceptLanguages' which returns the preferred
   user languages.

   https://developer.chrome.com/apps/i18n#method-getUILanguage."
  ([] (gen-call :function ::get-ui-language &form)))

(defmacro detect-language
  "Detects the language of the provided text using CLD.

     |text| - User input string to be translated.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [result] where:

     |result| - LanguageDetectionResult object that holds detected langugae reliability and array of DetectedLanguage

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/apps/i18n#method-detectLanguage."
  ([text] (gen-call :function ::detect-language &form text)))

; -- convenience ------------------------------------------------------------------------------------------------------------

(defmacro tap-all-events
  "Taps all valid non-deprecated events in chromex.app.i18n namespace."
  [chan]
  (gen-tap-all-events-call api-table (meta &form) chan))

; ---------------------------------------------------------------------------------------------------------------------------
; -- API TABLE --------------------------------------------------------------------------------------------------------------
; ---------------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.i18n",
   :since "5",
   :functions
   [{:id ::get-accept-languages,
     :name "getAcceptLanguages",
     :callback? true,
     :params
     [{:name "callback",
       :type :callback,
       :callback {:params [{:name "languages", :type "[array-of-i18n.LanguageCodes]"}]}}]}
    {:id ::get-message,
     :name "getMessage",
     :return-type "string",
     :params [{:name "message-name", :type "string"} {:name "substitutions", :optional? true, :type "any"}]}
    {:id ::get-ui-language, :name "getUILanguage", :since "35", :return-type "string"}
    {:id ::detect-language,
     :name "detectLanguage",
     :since "47",
     :callback? true,
     :params
     [{:name "text", :type "string"}
      {:name "callback", :type :callback, :callback {:params [{:name "result", :type "object"}]}}]}]})

; -- helpers ----------------------------------------------------------------------------------------------------------------

; code generation for native API wrapper
(defmacro gen-wrap [kind item-id config & args]
  (apply gen-wrap-helper api-table kind item-id config args))

; code generation for API call-site
(def gen-call (partial gen-call-helper api-table))