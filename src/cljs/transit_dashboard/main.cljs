(ns ^:figwheel-hooks transit-dashboard.main
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [transit-dashboard.views :as views]
            [transit-dashboard.config :as config]
            ;; register events and subs
            [transit-dashboard.events]
            [transit-dashboard.subs]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize])
  (dev-setup)
  (mount-root))

(defn ^:after-load rerender []
  (mount-root))

(when config/debug?
  (require 'transit-dashboard.development))
