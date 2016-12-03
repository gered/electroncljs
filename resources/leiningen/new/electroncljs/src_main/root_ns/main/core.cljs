(ns {{root-ns}}.main.core
  (:require
    [cljs.nodejs :as nodejs]
    [{{root-ns}}.config :refer [config]]))

(def path (nodejs/require "path"))
(def electron (nodejs/require "electron"))
(def app (.-app electron))

(def BrowserWindow (.-BrowserWindow electron))

(defonce window (atom nil))

(defn on-window-closed
  []
  (reset! window nil))

(defn on-window-ready-to-show
  []
  (.show @window))

(defn on-process-error
  [error]
  (println "ERROR:" error))

(defn on-app-window-all-closed
  []
  (.quit app))

(defn on-app-quit
  [e exit-code]
  (println "Main process quitting. Exit code:" exit-code))

(defn on-app-ready
  [launch-info]
  (let [window-settings (merge
                          {:show false}
                          (:browser-window config))]
    (println "App ready.")
    (reset! window (BrowserWindow. (clj->js window-settings)))
    (.loadURL @window (str "file://" (.getAppPath app) "/index.html"))
    (.on @window "ready-to-show" on-window-ready-to-show)
    (.on @window "closed" on-window-closed)
    (if (get-in config [:dev-tools :auto-open?])
      (.openDevTools (.-webContents @window)
                     (clj->js {:mode (get-in config [:dev-tools :position] "right")})))))

(defn init!
  []
  (println "Main process started, app is starting up.")
  (.on nodejs/process "error" on-process-error)
  (.on app "window-all-closed" on-app-window-all-closed)
  (.on app "quit" on-app-quit)
  (.on app "ready" on-app-ready))
