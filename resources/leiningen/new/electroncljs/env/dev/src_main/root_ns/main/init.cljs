(ns {{root-ns}}.main.init
  (:require
    [cljs.nodejs :as nodejs]
    [{{root-ns}}.main.core :as core]))

(nodejs/enable-util-print!)

(def on-figwheel-reload!
  (fn []
    (println "Figwheel initiated application reload.")
    (.relaunch core/app)
    (.exit core/app 0)))

(def start-electron!
  (fn []
    (reset! core/window nil)
    (core/init!)))

(set! *main-cli-fn* start-electron!)
