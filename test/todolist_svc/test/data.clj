(ns todolist_svc.test.data
  (:use clojure.test
    todolist_svc.data
    [monger.core :only [connect! connect set-db! get-db]])
  (:require [clj-time.core :as time]))

(defn mongo-connection [f]
  (connect! { :host "localhost" :port 27017})
  (set-db! (monger.core/get-db "doitnow-test")))

(use-fixtures :once mongo-connection)

(defn- object-id? [id]
  (and
    (not (nil? id))''
    (string? id)
    (re-matches #"[0-9a-f]{24}" id)))

(deftest create-test-doit
  (testing "Create valid doit"
    (let [doit {:title "Newly Created Test DoIt"
                :description "A new test doit"
                :due (time/plus (time/now (time/weeks 2)))
                :priority 1}
          created (create-doit doit)]
      (is (map? created))
      (is (contains? created :_id))
      (is (contains? created :title))
      (is (contains? created :description))
      (is (contains? created :due))
      (is (contains? created :priority))
      (is (contains? created :created))
      (is (contains? created :modified))))
  (testing "Create invalid doit"
    (is (thrown? IllegalArgumentException (create-doit {})))))
