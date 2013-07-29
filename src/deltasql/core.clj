(ns deltasql.core)
(import '(com.akiban.sql.parser SQLParser StatementNode))

(defn parse [sql]
  (let [parser (new SQLParser),
        statement (.parseStatement parser sql)]
    (.treePrint statement)))
