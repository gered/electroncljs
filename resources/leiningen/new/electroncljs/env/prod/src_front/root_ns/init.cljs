(ns {{root-ns}}.init
  (:require
    [{{root-ns}}.core :as core]))

(enable-console-print!)

(defn start-front!
  []
  (core/init!))

(start-front!)
