(ns bart-board.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub :stations
  (fn [db]
    (vals (:stations db))))

(rf/reg-sub :selected-station
  (fn [db]
    (get (:stations db) (:selected-station-id db))))

(rf/reg-sub :selected-station-departures
  (fn [db]
    (get-in (:departures db) [(:selected-station-id db) :etd])))

(rf/reg-sub :last-updated-at
  (fn [db]
    (:last-updated-at db)))
