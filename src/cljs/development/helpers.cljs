(ns development.helpers
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<! chan put!]]
            [ajax.core :as ajax]))

(defn fetch [req]
  (let [c (chan)]
    (ajax/ajax-request (merge req {:handler (fn [e]
                                              (put! c e))}))
    c))

(comment
  (require '[transit-dashboard.bart :as bart])
  (go (println (<! (fetch (bart/request :stations))))))
