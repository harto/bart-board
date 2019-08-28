(ns transit-dashboard.views
  (:require [re-frame.core :as rf]))

(defn station-selector []
  (let [stations @(rf/subscribe [:stations])
        selected-station @(rf/subscribe [:selected-station])]
    [:select
     {:value (if selected-station (:abbr selected-station) "")
      :disabled (when-not stations "disabled")
      :on-change #(rf/dispatch [:select-station (.-value (.-target %))])}
     (for [station (sort-by :name stations)]
       [:option {:key (:abbr station)
                 :value (:abbr station)}
        (:name station)])]))

(defn departures []
  (let [departures @(rf/subscribe [:selected-station-departures])]
    [:div (str departures)]))

(defn last-updated-at []
  [:div (str "Last updated at " @(rf/subscribe [:last-updated-at]))])

(defn root []
  (let [station @(rf/subscribe [:selected-station])]
    [:div
     [:h1 "Bart board"]
     [:h2 (if station
            (:name station)
            "<no station selected>")]
     [station-selector]
     [departures]
     [last-updated-at]]))
