(ns transit-dashboard.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.http-fx]  ;; auto-register effect handler
            [transit-dashboard.bart :as bart]
            [transit-dashboard.db :as db]))

(rf/reg-event-fx :initialize
  (fn [_ _]
    {:db db/default-db
     :http-xhrio (bart/request :stations
                               :on-success [:fetch-stations-success]
                               :on-failure [:fetch-stations-failure])}))

(rf/reg-event-db :fetch-stations-success
  (fn [db [_ resp]]
    (assoc db :stations resp)))

(rf/reg-event-db :fetch-stations-failure
  (fn [db [_ result]]
    (.log js/console (clj->js result)) ; FIXME remove
    (assoc db :stations [])))

(rf/reg-event-db :select-station
  (fn [db [_ station-id]]
    (assoc db :selected-station-id station-id)))
