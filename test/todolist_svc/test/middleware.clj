(ns todolist_svc.test.middleware
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [todolist_svc.middleware :refer :all]))

(deftest test-wrap-exception
  (testing "Exception Handling"
    (let [response ((wrap-exception-handler
                      #(throw (Exception. "Testing, 123")))
                    (request :get "/api"))]
      (is (= (response :status) 400))
      (is (instance? Exception (response :body))))))
