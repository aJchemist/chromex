(ns chromex.ext.wallpaper (:require-macros [chromex.ext.wallpaper :refer [gen-wrap]])
    (:require [chromex-lib.core]))

; -- functions --------------------------------------------------------------------------------------------------------------

(defn set-wallpaper* [config details]
  (gen-wrap :function ::set-wallpaper config details))

