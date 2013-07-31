(ns deltasql.sql
  (:import (com.akiban.sql.parser SQLParser StatementNode Visitor)))

;; implement the Visitor interface from sql.parser
;; NOTE that tree here must be a ref to collect values over invocations to visit
(defn visitor [tree]
  (proxy [Visitor] []
    (visit [node]
      ;; open up the ref and add this node
      (dosync (ref-set tree (conj @tree node)))
      node)
    (visitChildrenFirst [node] true)
    (stopTraversal [] false)
    (skipChildren [node] false)))

(defn parse [sql]
  (let [parser (new SQLParser)]
    (.parseStatements parser sql)))

(defn stmnt-to-vector [statement]
  (let [tree (ref [])]
    ;; use the Vistor proxy defined above to traverse the
    ;; "ast" provided by the sql parsing and flatten the nodes into
    ;; a vectore. The ref is required because the proxy closes over
    ;; the vector and modifies it using method calls
    (.accept statement (visitor tree))

    ;; return the unwrapped vector
    @tree))

;; send a visitor down the tree and collect differences as needs be
(defn diff [old-sql new-sql]
  (let [old-stmnts (parse old-sql),
        new-stmnts (parse new-sql)

        ;; mapping the statments to a vector will allow for comparison
        ;; NOTE the thinking here is that the visitation in the tree will
        ;;      produce similar vectors for similar parsed sql statements
        old-tree (map stmnt-to-vector old-stmnts),
        new-tree (map stmnt-to-vector new-stmnts)]

    (println new-tree)
    (println old-tree)))
