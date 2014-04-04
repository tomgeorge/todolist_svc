(ns todolist_svc.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.format-response :refer [wrap-restful-response]]
            [ring.mock.request :refer [header]]
            [ring.util.response :refer [response status]] 
            [todolist_svc.middleware :refer :all]))

(defroutes api-routes
  (context "/api" []
    (OPTIONS "/" []
     (->
       (response {:version "0.1.0-SNAPSHOT"})
       (header "Allow" "OPTIONS")))
    (ANY "/" []
         (throw (IllegalArgumentException. "Testing, 123")))) 
     ; (->
     ;   (response nil)
     ;   (status 405)
     ;   (header "Allow" "OPTIONS"))))
  (route/not-found "Not Found"))

(def app
  (->
  (handler/api api-routes) 
  (wrap-request-logger)
  (wrap-exception-handler)
  (wrap-response-logger)
  (wrap-restful-response)))
