(ns transit-dashboard.bart
  ;; (:require-macros [cljs.core.async.macros :refer [go]])
  ;; (:require [cljs-http.client :as http]
  ;;           [cljs.core.async :refer [<!]]
  ;;           [clojure.data.xml :as xml]
  ;;           [clojure.string :as str])
  (:require [ajax.core :as ajax])
  )

(def base-url "https://api.bart.gov/api/")
;; TODO: figure out how this will work in production
;;(def api-key "SOMETIHGN")
(def api-key "QXM2-5HI6-9N5T-DWE9")

(defn request
  "Prepare a request map, suitable for use with either :http-xhrio effects or
  directly with cljs-ajax."
  [endpoint & keyvals]
  (merge {:method :get
          :url "https://api.bart.gov/api/something"
          :timeout 10000
          :response-format (ajax/json-response-format)
          }
         (apply hash-map keyvals)))

;; (defn extract-xml-error [s]
;;   (try
;;     (let [doc (xml/parse-str s :raw true)]
;;       (when-let [err-el (.querySelector doc "error > details")]
;;         (let [message (->> err-el
;;                            (xml/element-data)
;;                            (:content)
;;                            (apply str))]
;;           (ex-info message {:message message}))))
;;     (catch ExceptionInfo e
;;       e)))

;; (defn extract-error [resp]
;;   ;; API returns errors as XML even though we asked for JSON.
;;   (let [mime-type (-> resp
;;                       (get-in [:headers "content-type"])
;;                       (str/split ";")
;;                       (first))]
;;     (when (= mime-type "text/xml")
;;       (extract-xml-error (:body resp)))))

;; (defn fetch
;;   ([endpoint-path cmd]
;;    (fetch endpoint-path cmd nil))
;;   ([endpoint-path cmd opts]
;;    (go
;;      (let [resp (<! (http/get (str base-url endpoint-path)
;;                               {:with-credentials? false
;;                                :query-params (merge {:key api-key
;;                                                      :cmd cmd
;;                                                      :json "y"}
;;                                                     opts)}))]
;;        (or (extract-error resp) (get-in resp [:body :root]))))))

;; (defn stations []
;;   (go (get-in (<! (fetch "stn.aspx" "stns")) [:stations :station])))

;; (defn departures [station-id]
;;   (fetch "etd.aspx" "etd" {:orig station-id}))
