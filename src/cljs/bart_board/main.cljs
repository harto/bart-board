(ns ^:figwheel-hooks bart-board.main
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [bart-board.views :as views]
            ;; register events and subs
            [bart-board.events]
            [bart-board.subs]))

(defn dev-setup []
  (when ^boolean goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/root]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize])
  (dev-setup)
  (mount-root))

(defn ^:after-load rerender []
  (mount-root))
