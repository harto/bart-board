(ns transit-dashboard.events
  (:require [re-frame.core :as rf]
            [transit-dashboard.db :as db]))

(rf/reg-event-db ::initialize-db
  (fn [_ _]
    db/default-db))

(rf/reg-event-db ::select-station
  (fn [db [_ station-id]]
    (assoc db :selected-station-id station-id)))
