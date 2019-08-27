(ns transit-dashboard.development
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<! chan put!]]
            [ajax.core :as ajax]
            [transit-dashboard.bart :as bart]))

(defn fetch [req]
  (let [c (chan)]
    (ajax/ajax-request (merge req {:handler (fn [e]
                                              (put! c e))}))
    c))

(comment
  (go (println (<! (fetch (bart/departures :station-id "all"))))))
