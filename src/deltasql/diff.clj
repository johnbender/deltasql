(ns deltasql.diff
  (:require [deltasql.utils :as utils]
            [clojure.core.reducers :as reducers])
  (:import (difflib DiffUtils)))


(defn parse [diff]
  (DiffUtils/parseUnifiedDiff diff))

(defn unpatch [patch, file]
  (reducers/fold str (DiffUtils/unpatch (utils/file-lines file) patch)))

(defn paths [patch]
  (map #(nth % 1) (map #(clojure.string/split % #"\s") (take 2 patch))))
