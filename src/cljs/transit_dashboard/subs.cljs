(ns transit-dashboard.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub :stations
  (fn [db]
    (vals (:stations db))))

(rf/reg-sub :selected-station
  (fn [db]
    (get (:stations db) (:selected-station-id db))))
