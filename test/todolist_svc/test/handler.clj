(ns todolist_svc.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [todolist_svc.handler :refer :all]))

(deftest test-api-routes
  (testing "API Options"
    (let [response (api-routes (request :options "/api"))]
      (is (= (:status response) 200))
      (is (contains? (response :body) :version))))
  (testing "API Get"
    (let [response (api-routes (request :get "/api"))]
      (is (= (:status response) 405))
      (is (nil? (response :body)))))
  (testing "not found"
    (let [response (api-routes (request :get "/invalid"))]
      (is (= (response :status) 404)))))
