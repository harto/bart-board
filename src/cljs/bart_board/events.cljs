(ns bart-board.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.http-fx]  ;; auto-register :http-xhrio effect handler
            [bart-board.api :as api]
            [bart-board.db :as db]
            [bart-board.local-storage :as local-storage]
            [bart-board.utils :refer [key-by]]))

(rf/reg-event-fx :initialize
  (fn [_ _]
    {:db db/default-db
     :dispatch-n [[:load-prefs]
                  [:fetch-stations]
                  [:repeatedly-fetch-departures]]}))

(rf/reg-event-fx :load-prefs
  [(rf/inject-cofx ::local-storage/get "bart-board.selected-station")]
  (fn [cofx _]
    (when-let [station-id (::local-storage/val cofx)]
      {:dispatch [:select-station station-id]})))

;; Stations

(rf/reg-event-fx :fetch-stations
  (fn [_ _]
    {:http-xhrio (api/stations :on-success [:fetch-stations-success]
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

(rf/reg-event-fx :select-station
  (fn [{:keys [db]} [_ station-id]]
    {:db (assoc db :selected-station-id station-id)
     ::local-storage/merge {"bart-board.selected-station" station-id}}))

;; Departures

(rf/reg-event-fx :repeatedly-fetch-departures
  (fn [_ _]
    {:dispatch [:fetch-departures]
     :dispatch-later [{:ms (* 60 1000) :dispatch [:repeatedly-fetch-departures]}]}))

(rf/reg-event-fx :fetch-departures
  (fn [_ _]
    {:http-xhrio (api/departures :on-success [:fetch-departures-success]
                                 :on-failure [:fetch-departures-failure])}))

(rf/reg-event-db :fetch-departures-success
  (fn [db [_ resp]]
    (let [payload (:root resp)
          departures (:station payload)
          resp-time (api/parse-time (:date payload) (:time payload))]
      (-> db
          (assoc :departures (key-by departures :abbr))
          (assoc :last-updated-at resp-time)))))

;; FIXME: do something useful
(rf/reg-event-db :fetch-departures-failure
  (fn [db [_ result]]
    (.log js/console (clj->js result))  ; FIXME remove
    (dissoc db :departures)))
