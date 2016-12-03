(def electron-version "1.4.10")
(def figwheel-version "0.5.8")

(defproject {{name}} "0.1.0-SNAPSHOT"
  :description   "FIXME: write description"
  :url           "http://example.com/FIXME"
  :license       {:name "MIT License"
                  :url  "http://opensource.org/licenses/MIT"}

  :dependencies  [[cljsjs/bootstrap "3.3.6-1"]
                  [figwheel ~figwheel-version]
                  [org.clojure/clojure "1.8.0"]
                  [org.clojure/clojurescript "1.9.293"]
                  [org.webjars/bootstrap "3.3.6"]
                  [reagent "0.6.0"]
                  [ring/ring-core "1.5.0"]]

  :plugins       [[lein-cljsbuild "1.1.4"]
                  [lein-externs "0.1.6"]
                  [lein-figwheel ~figwheel-version]
                  [lein-npm "0.6.2"]
                  [lein-shell "0.5.0"]]

  :npm           {:devDependencies [[electron-prebuilt ~electron-version]
                                    [electron-packager "^8.3.0"]
                                    [closurecompiler-externs "^1.0.4"]
                                    [ws "^1.1.1"]]}

  :source-paths  ["src"]

  :clean-targets ^{:protect false} [:target-path
                                    "releases"
                                    [:cljsbuild :builds :main :compiler :output-to]
                                    [:cljsbuild :builds :main :compiler :output-dir]
                                    [:cljsbuild :builds :front :compiler :output-to]
                                    [:cljsbuild :builds :front :compiler :output-dir]
                                    "app/js/externs_main.js"
                                    "app/js/externs_front.js"]

  :figwheel      {:nrepl-port       7888
                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]
                  :builds-to-start  [:front]}

  :cljsbuild     {:builds
                  {:main
                   {:source-paths ["src" "src_main"]
                    :figwheel     {:on-jsload {{root-ns}}.main.init/on-figwheel-reload!}
                    :compiler     {:main           {{root-ns}}.main.init
                                   :output-to      "app/js/main.js"
                                   :output-dir     "app/js/out_main"
                                   :asset-path     "app/js/out_main"
                                   :externs        ["app/js/externs_main.js"]
                                   :target         :nodejs
                                   :optimizations  :none}}
                   :front
                   {:source-paths ["src" "src_front"]
                    :figwheel     {:on-jsload {{root-ns}}.init/start-front!}
                    :compiler     {:main           {{root-ns}}.init
                                   :output-to      "app/js/front.js"
                                   :output-dir     "app/js/out_front"
                                   :asset-path     "js/out_front"
                                   :externs        ["app/js/externs_front.js"]
                                   :optimizations  :none}}}}

  :profiles      {:dev  {:source-paths ["env/dev/src"]
                         :dependencies [[com.cemerick/piggieback "0.2.1"]
                                        [figwheel-sidecar ~figwheel-version]]
                         :cljsbuild    {:builds
                                        {:main  {:source-paths ["env/dev/src" "env/dev/src_main"]}
                                         :front {:source-paths ["env/dev/src" "env/dev/src_front"]}}}}

                  :prod {:source-paths ["env/prod/src"]
                         :cljsbuild    {:builds
                                        {:main  {:source-paths ["env/prod/src" "env/prod/src_main"]
                                                 :compiler     ^:replace
                                                               {:output-to      "app/js/main.js"
                                                                :externs        ["app/js/externs_main.js"]
                                                                :optimizations  :advanced
                                                                :target         :nodejs
                                                                :pretty-print   false}}
                                         :front {:source-paths ["env/prod/src" "env/prod/src_front"]
                                                 :compiler     ^:replace
                                                               {:output-to      "app/js/front.js"
                                                                :externs        ["app/js/externs_front.js"]
                                                                :optimizations  :advanced
                                                                :pretty-print   false}}}}}}


  :aliases       {"build-main"  ["do"
                                 ["externs" "main" "app/js/externs_main.js"]
                                 ["cljsbuild" "once" "main"]]
                  "build-front" ["do"
                                 ["externs" "front" "app/js/externs_front.js"]
                                 ["cljsbuild" "once" "front"]]
                  "build"       ["do" "build-main" "build-front"]
                  "run"         ["do"
                                 ["shell" "./node_modules/.bin/electron" "app"]]
                  "package"     ["do"
                                 "clean"
                                 ["with-profile" "prod" "build"]
                                 ["with-profile" "prod" "shell" "./node_modules/.bin/electron-packager"
                                  "app" :project/name
                                  ~(str "--version=" electron-version)
                                  "--asar"
                                  "--out=releases"
                                  "--overwrite"]]}
  )
