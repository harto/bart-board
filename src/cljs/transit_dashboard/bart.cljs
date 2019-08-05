(ns transit-dashboard.bart
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(def api-key "SOMETIHGN")

(defn fetch
  ([endpoint cmd]
   (fetch endpoint cmd nil))
  ([endpoint cmd opts]
   (http/get (str "https://api.bart.gov/api/" endpoint)
             {:with-credentials? false
              :query-params (merge {:key api-key
                                    :cmd cmd
                                    :json "y"}
                                   opts)})))

(defn stations []
  (go (get-in (<! (fetch "stn.aspx" "stns")) [:stations :station])))

(defn departures [station-id]
  (fetch "etd.aspx" "etd" {:orig station-id}))
