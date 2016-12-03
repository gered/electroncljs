(ns leiningen.new.electroncljs
  (:import
    [java.util Calendar GregorianCalendar])
  (:require
    [leiningen.core.main :as main]
    [leiningen.new.templates :as t]))

(def render (t/renderer "electroncljs"))

(defn electroncljs
  [name]
  (let [data {:name         name
              :sanitized    (t/sanitize name)
              :root-ns      (t/sanitize-ns name)
              :root-ns-path (t/name-to-path name)
              :year         (.get (GregorianCalendar.) (Calendar/YEAR))}]
    (main/info (str "Creating a new ClojureScript Electron project \"" name "\" ..."))
    (t/->files
      data
      "app/js"
      "app/img"
      ["app/css/app.css"                                   (render "app/css/app.css")]
      ["app/index.html"                                    (render "app/index.html" data)]
      ["app/package.json"                                  (render "app/package.json" data)]
      ["env/dev/src/{{root-ns-path}}/config.cljs"          (render "env/dev/src/root_ns/config.cljs" data)]
      ["env/dev/src/user.clj"                              (render "env/dev/src/user.clj" data)]
      ["env/dev/src_front/{{root-ns-path}}/init.cljs"      (render "env/dev/src_front/root_ns/init.cljs" data)]
      ["env/dev/src_main/{{root-ns-path}}/main/init.cljs"  (render "env/dev/src_main/root_ns/main/init.cljs" data)]

      ["env/prod/src/{{root-ns-path}}/config.cljs"         (render "env/prod/src/root_ns/config.cljs" data)]
      ["env/prod/src_front/{{root-ns-path}}/init.cljs"     (render "env/prod/src_front/root_ns/init.cljs" data)]
      ["env/prod/src_main/{{root-ns-path}}/main/init.cljs" (render "env/prod/src_main/root_ns/main/init.cljs" data)]
      "src/{{root-ns-path}}"
      ["src_front/{{root-ns-path}}/core.cljs"              (render "src_front/root_ns/core.cljs" data)]
      ["src_main/{{root-ns-path}}/main/core.cljs"          (render "src_main/root_ns/main/core.cljs" data)]
      [".gitignore"                                        (render ".gitignore")]
      ["LICENSE"                                           (render "LICENSE" data)]
      ["README.md"                                         (render "README.md" data)]
      ["project.clj"                                       (render "project.clj" data)])
    (main/info "------------------------------------------------------------------------")
    (main/info "Your new Electron project is ready! To get started:")
    (main/info "")
    (main/info "    $ cd" name)
    (main/info "    $ lein npm install")
    (main/info "")
    (main/info "You app's code is located in a few places:\n")
    (main/info "    /src            - code common to both main and renderer (front)")
    (main/info "    /src_front      - code for renderer process (UI)")
    (main/info "    /src_main       - code for main process (backend)")
    (main/info "")
    (main/info "Build profile-specific sources are located under /env")
    (main/info "Application code config map at:")
    (main/info (str "    /env/dev/src/" (:root-ns-path data) "/config.cljs"))
    (main/info (str "    /env/prod/src/" (:root-ns-path data) "/config.cljs"))
    (main/info "")
    (main/info "Electron app resources (HTML, CSS, JS, etc) are located under /app")
    (main/info "")
    (main/info "Build:              $ lein build")
    (main/info "Run:                $ lein run")
    (main/info "Rebuild:            $ lein clean && lein build")
    (main/info "Package:            $ lein package")
    (main/info "Figwheel: (do in order!)")
    (main/info "     (terminal #1)  $ lein figwheel")
    (main/info "     (terminal #2)  $ lein run")
    (main/info "(Figwheel REPL running on port 7888, run (cljs-repl) after connecting)")
    (main/info "------------------------------------------------------------------------")))
