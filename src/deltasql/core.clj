(ns deltasql.core
  (:require [deltasql.utils :as utils]
            [deltasql.diff :as diff]
            [deltasql.sql :as sql]
            [clojure.core.reducers :as reducers]))

(defn transform
  "Accepts a diff, in list form, of some monotonic DDL statement and
  produces the equivalent non-monotonic DDL statments"
  [diff-lines]
  (let [patch (diff/parse diff-lines),
        ;; grab the second path in the unified iff for the new file
        new-file (nth (diff/paths diff-lines) 1),

        ;; TODO grab only the sql in the patch, ie up from
        ;; each chunk in the diff to a ';' and down to a ';'

        ;; undo the patch on the new-file to get the old-file
        old-sql (diff/unpatch patch new-file),

        ;; grab the contents of the new file
        new-sql (reducers/fold str (utils/file-lines new-file))]

    ;; parse the old sql
    ;; parse the new sql
    (sql/diff old-sql new-sql)
    ;; recurse with both trees and store differences
    ;; recognize the differences relative to the sql statement type
    ;; generate equivalent statments
    new-sql))
