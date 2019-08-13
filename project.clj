(defproject transit-dashboard "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"]
                 [org.clojure/data.xml "0.2.0-alpha6"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.8"]
                 [cljs-http "0.1.46"]]

  :min-lein-version "2.5.3"
  :source-paths ["src/clj" "src/cljs"]
  ;; :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles {:dev {:dependencies [[com.bhauman/figwheel-main "0.2.3"]]}}

  :aliases {"fig:dev"      ["trampoline" "run" "-m" "figwheel.main" "-b" "dev" "-r"]
            "fig:dev:once" ["trampoline" "run" "-m" "figwheel.main" "-bo" "dev"]})
