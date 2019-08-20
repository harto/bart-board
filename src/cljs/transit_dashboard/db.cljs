(ns transit-dashboard.db)

(def default-db
  {:last-updated-at nil
   :selected-station-id nil
   :stations {"dummy-1" {:id "dummy-1" :name "Dummy 1"}
              "dummy-2" {:id "dummy-2" :name "Dummy 2"}}})
