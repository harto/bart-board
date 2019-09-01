(ns transit-dashboard.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.http-fx]  ;; auto-register :http-xhrio effect handler
            [transit-dashboard.bart :as bart]
            [transit-dashboard.db :as db]
            [transit-dashboard.utils :refer [key-by]]))

(rf/reg-event-fx :initialize
  (fn [_ _]
    {:db db/default-db
     :dispatch-n [[:fetch-stations]
                  [:repeatedly-fetch-departures]]}))

;; Stations

(rf/reg-event-fx :fetch-stations
  (fn [_ _]
    {:http-xhrio (bart/stations :on-success [:fetch-stations-success]
                                :on-failure [:fetch-stations-failure])}))

(rf/reg-event-db :fetch-stations-success
  (fn [db [_ resp]]
    ;; TODO: might be nice if something did this unpacking automatically
    (let [stations (get-in resp [:root :stations :station])]
      ;; TODO: normalize response resources?
      ;; TODO: is there a built-in (e.g. index-by)?
      (assoc db :stations (key-by stations :abbr)))))

;; FIXME: do something useful
(rf/reg-event-db :fetch-stations-failure
  (fn [db [_ result]]
    (.log js/console (clj->js result))  ; FIXME remove
    (dissoc db :stations)))

(rf/reg-event-db :select-station
  (fn [db [_ station-id]]
    (assoc db :selected-station-id station-id)))

;; Departures

(rf/reg-event-fx :repeatedly-fetch-departures
  (fn [_ _]
    {:dispatch [:fetch-departures]
     :dispatch-later [{:ms (* 60 1000) :dispatch [:repeatedly-fetch-departures]}]}))

(rf/reg-event-fx :fetch-departures
  (fn [_ _]
    {:http-xhrio (bart/departures :on-success [:fetch-departures-success]
                                  :on-failure [:fetch-departures-failure])}))

(rf/reg-event-db :fetch-departures-success
  (fn [db [_ resp]]
    (let [payload (:root resp)
          departures (:station payload)
          resp-time (bart/parse-time (:date payload) (:time payload))]
      (-> db
          (assoc :departures (key-by departures :abbr))
          (assoc :last-updated-at resp-time)))))

;; FIXME: do something useful
(rf/reg-event-db :fetch-departures-failure
  (fn [db [_ result]]
    (.log js/console (clj->js result))  ; FIXME remove
    (dissoc db :departures)))
