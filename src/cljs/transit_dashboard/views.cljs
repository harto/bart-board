(ns transit-dashboard.views
  (:require [re-frame.core :as rf]
            [transit-dashboard.events :as events]
            [transit-dashboard.subs :as subs]))

(defn station-selector []
  (let [stations @(rf/subscribe [:stations])]
    [:select
     {:on-change #(rf/dispatch [::events/select-station (.-value (.-target %))])}
     (for [station stations]
       [:option {:key (:id station)
                 :value (:id station)}
        (:name station)])]))

(defn main-panel []
  (let [station @(rf/subscribe [:selected-station])]
    [:div
     [:h1 "Bart board"]
     [:h2 (if station
            (:name station)
            "<no station selected>")]
     [station-selector]
     ]))
