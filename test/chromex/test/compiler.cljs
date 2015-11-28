(ns chromex.test.compiler)

(defn should-match [[failures count compiler-output] match-string]
  (if (re-matches match-string compiler-output)
    [failures (inc count) compiler-output]
    (do
      (println "!!! expected compiler output not found:\n  " match-string)
      [(inc failures) (inc count) compiler-output])))

(defn ^:export warnings [compiler-output]
  (let [res (-> [0 0 compiler-output]
                (should-match #".*The API call to 'chrome.playground.someProp' is deprecated. use someOtherProp instead.*")
                (should-match #".*The API call to 'chrome.playground.onSomethingMissing' is deprecated. Use onSomethingElse instead.*"))
        [failures count] res]
    (println (str "Additionally ran " count " compiler output tests, " failures " failed."))
    failures))
