(ns transit-dashboard.bart
  (:require [ajax.core :as ajax]
            [ajax.protocols]
            [clojure.data.xml :as xml]
            [clojure.string :as str]))

;; The shared API key provided at https://www.bart.gov/schedules/developers/api.
(def api-key "MW9S-E7SL-26DU-VV8V")

;; If the API receives invalid input, instead of generating an error response,
;; it returns HTTP 200 and an XML-encoded error (even though we ask for
;; JSON). We handle this with an ajax response interceptor.

(defn mime-type [resp]
  (-> (ajax.protocols/-get-all-headers resp)
      (get "content-type")
      (str/split ";")
      (first)))

(defn extract-error-message [s]
  (let [doc (xml/parse-str s :raw true)]
    (when-let [err-el (.querySelector doc "error > details")]
      (->> err-el
           (xml/element-data)
           (:content)
           (apply str)))))

(defn error-message [resp]
  (when (and (= (ajax.protocols/-status resp) 200)
             (= (mime-type resp) "text/xml"))
    (extract-error-message (ajax.protocols/-body resp))))

(defn xml-error-response-interceptor []
  (ajax/to-interceptor {:name "XML error response parser"
                        :response (fn [response]
                                    (if-let [err (error-message response)]
                                      ;; TODO: figure out if these values are correct
                                      (reduced [false {:status 400
                                                       :status-text err
                                                       :failure :error
                                                       :response response}])
                                      response))}))

;; (defn unnest-json-response-interceptor []
;;   (ajax/to-interceptor {:name "JSON response unnester"
;;                         :response (fn [x]
;;                                     (println "oh look: " x)
;;                                     x)}))

(defn request
  "Prepare a request map, suitable for use with either :http-xhrio effects or
  directly with cljs-ajax."
  [{:keys [uri on-success on-failure params]}]
  {:method :get
   :uri uri
   :params (merge params {:key api-key :json "y"})
   :response-format (ajax/json-response-format {:keywords? true})
   :interceptors [(xml-error-response-interceptor)]
   :on-success on-success
   :on-failure on-failure})

;;

(defn stations [& {:as opts}]
  (request (merge opts
                  {:uri "https://api.bart.gov/api/stn.aspx"
                   :params {:cmd "stns"}})))

(defn departures [& {:as opts}]
  (request (merge opts
                  {:uri "https://api.bart.gov/api/etd.aspx"
                   :params {:cmd "etd" :orig "all"}})))
