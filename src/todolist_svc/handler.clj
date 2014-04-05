(ns todolist_svc.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.format-response :refer [wrap-restful-response]]
            [ring.middleware.json :refer [wrap-json-body]]
            [todolist_svc.http :as http]
            [todolist_svc.middleware :refer :all]
            [todolist_svc.data :refer [create-doit]]
            [clojure.walk :refer [keywordize-keys]]))

(defroutes api-routes
  (context "/api" []
    (OPTIONS "/" []
     (http/options [:options] {:version "0.3.0-SNAPSHOT"}))
    (ANY "/" []
         (http/method-not-allowed [:options]))
    (context "/doits" []
      (GET "/" []
        (http/not-implemented))
      (GET "/:id" [id]
        (http/not-implemented))
      (HEAD "/:id" [id]
        (http/not-implemented))
      (POST "/:id" [:as req]
        (let [doit (create-doit (keywordize-keys (req :body)))
              location (http/url-from req (str (doit :_id)))]
          (http/created location doit)))
      (PUT "/:id" [id]
        (http/not-implemented))
      (DELETE "/:id" [id])
      (OPTIONS "/" []
        (http/options [:options :get :head :put :post :delete]))
      (ANY "/" []
        (http/method-not-allowed [:options :get :head :put :post :delete]))))
  (route/not-found "Not Found"))

(def app
  (->
  (handler/api api-routes) 
  (wrap-json-body)
  (wrap-request-logger)
  (wrap-exception-handler)
  (wrap-response-logger)
  (wrap-restful-response)))
