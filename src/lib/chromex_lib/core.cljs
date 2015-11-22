(ns chromex-lib.core
  (:require [goog.object]
            [chromex-lib.config :as config]
            [chromex-lib.marshalling]
            [chromex-lib.chrome-event-subscription]
            [chromex-lib.chrome-event-channel]
            [chromex-lib.chrome-port]))

(def get-active-config config/get-active-config)
(def set-active-config! config/set-active-config!)