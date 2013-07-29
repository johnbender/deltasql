(ns deltasql.core-test
  (:require [clojure.test :refer :all]
            [deltasql.core :refer :all]))

(deftest a-test
  (parse "create table foo ( bar text )")
  (testing "FIXME, I fail."
    (is (= 1 1))))
