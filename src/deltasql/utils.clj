(ns deltasql.utils
  (:require [clojure.java.io :as io]))

(defn file-lines [file-name]
  (with-open [reader (io/reader file-name)]
    (doall (line-seq reader))))
