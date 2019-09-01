(ns transit-dashboard.local-storage
  (:refer-clojure :exclude [assoc! get select-keys])
  (:require [re-frame.core :as rf]))

(def storage js/window.localStorage)

;; individual accessors

(defn get [storage k]
  {:pre [(string? k)]}
  (.getItem storage k))

(defn assoc! [storage k v]
  {:pre [(string? k)
         (string? v)]}
  (.setItem storage k v))

;; collective accessors

;; (defn select-keys [storage ks]
;;   (into {} (for [k ks] [k (get storage k)])))

(defn merge! [storage m]
  {:pre [(map? m)]}
  (doseq [[k v] m] (assoc! storage k v)))

;; re-frame interface
;; TODO: revisit this if it doesn't feel quite right

(rf/reg-cofx
 ::get
 (fn [cofx k]
   (assoc cofx ::val (get storage k))))

(rf/reg-fx
 ::merge
 (fn [m]
   (merge! storage m)))
