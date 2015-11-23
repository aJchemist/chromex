(ns chromex.language-settings-private
  "Use the chrome.languageSettingsPrivate API to get or change
   language and input method settings.
   
     * available since Chrome 47
     * https://developer.chrome.com/extensions/languageSettingsPrivate"

  (:refer-clojure :only [defmacro defn apply declare meta let])
  (:require [chromex-lib.wrapgen :refer [gen-wrap-from-table]]
            [chromex-lib.callgen :refer [gen-call-from-table gen-tap-all-call]]
            [chromex-lib.config :refer [get-static-config gen-active-config]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro get-language-list
  "Gets languages available for translate, spell checking, input and locale.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::get-language-list &form)))

(defmacro set-language-list
  "Sets the accepted languages, used to decide which languages to translate, generate the Accept-Language header, etc."
  ([language-codes] (gen-call :function ::set-language-list &form language-codes)))

(defmacro get-spellcheck-dictionary-statuses
  "Gets the current status of the chosen spell check dictionaries.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::get-spellcheck-dictionary-statuses &form)))

(defmacro get-spellcheck-words
  "Gets the custom spell check words, in sorted order.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::get-spellcheck-words &form)))

(defmacro add-spellcheck-word
  "Adds a word to the custom dictionary."
  ([word] (gen-call :function ::add-spellcheck-word &form word)))

(defmacro remove-spellcheck-word
  "Removes a word from the custom dictionary."
  ([word] (gen-call :function ::remove-spellcheck-word &form word)))

(defmacro get-translate-target-language
  "Gets the translate target language (in most cases, the display locale).
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::get-translate-target-language &form)))

(defmacro get-input-method-lists
  "Gets all supported input methods, including third-party IMEs. Chrome OS only.
   
   Note: Instead of passing a callback function, you receive a core.async channel as return value."
  ([#_callback] (gen-call :function ::get-input-method-lists &form)))

(defmacro add-input-method
  "Adds the input method to the current user's list of enabled input methods, enabling the input method for the current user.
   Chrome OS only."
  ([input-method-id] (gen-call :function ::add-input-method &form input-method-id)))

(defmacro remove-input-method
  "Removes the input method from the current user's list of enabled input methods, disabling the input method for the current
   user. Chrome OS only."
  ([input-method-id] (gen-call :function ::remove-input-method &form input-method-id)))

; -- events -----------------------------------------------------------------------------------------------------------------

(defmacro tap-on-spellcheck-dictionaries-changed-events
  "Called when the pref for the dictionaries used for spell checking changes or the status of one of the spell check
   dictionaries changes."
  ([channel] (gen-call :event ::on-spellcheck-dictionaries-changed &form channel)))

(defmacro tap-on-custom-dictionary-changed-events
  "Called when words are added to and/or removed from the custom spell check dictionary."
  ([channel] (gen-call :event ::on-custom-dictionary-changed &form channel)))

(defmacro tap-on-input-method-added-events
  "Called when an input method is added."
  ([channel] (gen-call :event ::on-input-method-added &form channel)))

(defmacro tap-on-input-method-removed-events
  "Called when an input method is removed."
  ([channel] (gen-call :event ::on-input-method-removed &form channel)))

; -- convenience ------------------------------------------------------------------------------------------------------------

(defmacro tap-all-events [chan]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (gen-tap-all-call static-config api-table (meta &form) config chan)))

; ---------------------------------------------------------------------------------------------------------------------------
; -- API TABLE --------------------------------------------------------------------------------------------------------------
; ---------------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.languageSettingsPrivate",
   :since "47",
   :functions
   [{:id ::get-language-list,
     :name "getLanguageList",
     :callback? true,
     :params
     [{:name "callback", :type :callback, :callback {:params [{:name "languages", :type "[array-of-objects]"}]}}]}
    {:id ::set-language-list, :name "setLanguageList", :params [{:name "language-codes", :type "[array-of-strings]"}]}
    {:id ::get-spellcheck-dictionary-statuses,
     :name "getSpellcheckDictionaryStatuses",
     :callback? true,
     :params
     [{:name "callback",
       :type :callback,
       :callback
       {:params [{:name "status", :type "[array-of-languageSettingsPrivate.SpellcheckDictionaryStatuss]"}]}}]}
    {:id ::get-spellcheck-words,
     :name "getSpellcheckWords",
     :callback? true,
     :params [{:name "callback", :type :callback, :callback {:params [{:name "words", :type "[array-of-strings]"}]}}]}
    {:id ::add-spellcheck-word, :name "addSpellcheckWord", :since "48", :params [{:name "word", :type "string"}]}
    {:id ::remove-spellcheck-word, :name "removeSpellcheckWord", :since "48", :params [{:name "word", :type "string"}]}
    {:id ::get-translate-target-language,
     :name "getTranslateTargetLanguage",
     :callback? true,
     :params [{:name "callback", :type :callback, :callback {:params [{:name "language-code", :type "string"}]}}]}
    {:id ::get-input-method-lists,
     :name "getInputMethodLists",
     :callback? true,
     :params [{:name "callback", :type :callback, :callback {:params [{:name "lists", :type "object"}]}}]}
    {:id ::add-input-method, :name "addInputMethod", :params [{:name "input-method-id", :type "string"}]}
    {:id ::remove-input-method, :name "removeInputMethod", :params [{:name "input-method-id", :type "string"}]}],
   :events
   [{:id ::on-spellcheck-dictionaries-changed,
     :name "onSpellcheckDictionariesChanged",
     :params [{:name "statuses", :type "[array-of-languageSettingsPrivate.SpellcheckDictionaryStatuss]"}]}
    {:id ::on-custom-dictionary-changed,
     :name "onCustomDictionaryChanged",
     :params [{:name "words-added", :type "[array-of-strings]"} {:name "words-removed", :type "[array-of-strings]"}]}
    {:id ::on-input-method-added, :name "onInputMethodAdded", :params [{:name "input-method-id", :type "string"}]}
    {:id ::on-input-method-removed, :name "onInputMethodRemoved", :params [{:name "input-method-id", :type "string"}]}]})

; -- helpers ----------------------------------------------------------------------------------------------------------------

; code generation for native API wrapper
(defmacro gen-wrap [kind item-id config & args]
  (let [static-config (get-static-config)]
    (apply gen-wrap-from-table static-config api-table kind item-id config args)))

; code generation for API call-site
(defn gen-call [kind item src-info & args]
  (let [static-config (get-static-config)
        config (gen-active-config static-config)]
    (apply gen-call-from-table static-config api-table kind item src-info config args)))