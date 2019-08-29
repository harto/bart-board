(defproject transit-dashboard "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"]
                 [org.clojure/data.xml "0.2.0-alpha6"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.8"]
                 [day8.re-frame/http-fx "0.1.6"]
                 ;[cljs-http "0.1.46"]
                 ]

  :min-lein-version "2.5.3"
  :source-paths ["src/clj" "src/cljs"]
  :resource-paths ["resources" "target/public"]
  :clean-targets ^{:protect false} ["target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles {:dev {:dependencies [[com.bhauman/figwheel-main "0.2.3"]
                                  [org.clojure/core.async "0.4.500"]]}}

  :aliases {"fig:dev"      ["trampoline" "run" "-m" "figwheel.main" "-b" "dev" "-r"]
            "fig:dev:once" ["trampoline" "run" "-m" "figwheel.main" "-bo" "dev"]})
