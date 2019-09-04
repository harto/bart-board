(ns bart-board.utils)

(defn key-by [seq k]
  (into {} (for [o seq] [(get o k) o])))
