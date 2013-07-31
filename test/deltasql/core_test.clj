(ns deltasql.core-test
  (:require [clojure.test :refer :all]
            [deltasql.core :refer :all]
            [clojure.java.shell :as shell]))

(defn run-diff [old, new]
  ((shell/sh "diff" "-u" old new) :out))

(deftest transform-test
  (let [output (run-diff "test/samples/sql/create.sql" "test/samples/sql/create-altered.sql"),
        diff-string (transform (clojure.string/split output #"\n"))]
    (testing (is (= diff-string "CREATE TABLE foo ( bar text, baz text );")))))
