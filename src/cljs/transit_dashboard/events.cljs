(ns transit-dashboard.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.http-fx]  ;; auto-register :http-xhrio effect handler
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
    ;; TODO: might be nice if something did this unpacking automatically
    (let [stations (get-in resp [:root :stations :station])]
      ;; TODO: normalize response resources?
      ;; TODO: is there a built-in (e.g. index-by)?
      (assoc db :stations (into {} (for [s stations] [(:abbr s) s]))))))

(rf/reg-event-db :fetch-stations-failure
  (fn [db [_ result]]
    (.log js/console (clj->js result)) ; FIXME remove
    (dissoc db :stations)))

;; TODO: update to fetch latest departure data
(rf/reg-event-db :select-station
  (fn [db [_ station-id]]
    (assoc db :selected-station-id station-id)))
