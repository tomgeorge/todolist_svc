(ns todolist_svc.middleware
  (:require [cheshire.generate :refer [encode-str add-encoder]]
            [clojure.string :refer [upper-case]]
            [ring.mock.request :refer [body]]
            [ring.util.response :refer [response status]]
            [taoensso.timbre :as timbre]))

(timbre/refer-timbre) 

(add-encoder java.lang.Exception
 (fn [e jg]
  (.writeStartObject jg) 
  (.writeFieldName jg "exception") 
  (.writeString jg (.getName (class e))) 
  (.writeFieldName jg "message") 
  (.writeString jg (.getMessage e)) 
  (.writeEndObject jg))) 

(defn wrap-request-logger [handler]
  (fn [req]
    (let [{remote-addr :remote-addr request-method :request-method uri :uri} req]
      (debug remote-addr (upper-case (name request-method)) uri)
      (handler req)))) 

(defn wrap-response-logger [handler]
  (fn [req]
    (let [response (handler req)
          {remote-addr :remote-addr request-method :request-method uri :uri} req
          {status :status} response]
      (if (instance? Exception body)
        (warn body remote-addr (upper-case (name request-method)) uri "->" status body)
        (debug remote-addr (upper-case (name request-method)) uri "->" status)) 
      response))) 

(defn wrap-exception-handler [handler]
  (fn [req]
    (try
      (handler req)
      (catch Exception e
        (->
          (response e)
          (status 500))))))
