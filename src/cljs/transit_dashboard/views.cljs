(ns transit-dashboard.views
  (:require [re-frame.core :as rf]))

;; TODO: depend less directly on the API response format

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

(defn departures [dest]
  (let [color (:hexcolor (first (:estimate dest)))]
    [:div
     [:span {:style {:background-color color}}
      "-> " (:destination dest)]
     [:ul
      (for [est (:estimate dest)
            :let [mins (:minutes est)]]
        [:li {:key mins} (if (js/isNaN mins) mins (str mins " minutes"))])]]))

(defn destinations []
  (let [destinations @(rf/subscribe [:selected-station-departures])]
    [:div
     (for [dest (sort-by :name destinations)]
       ^{:key (:abbreviation dest)} [departures dest])]))

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
     [destinations]
     [last-updated-at]]))
