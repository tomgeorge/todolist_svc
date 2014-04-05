(ns todolist_svc.data
  (:require [clj-time.core :as time]
            [monger.collection :as collection]
            [monger.core :refer [connect! get-db set-db!]]
            [monger.result :refer [ok?]]
            [monger.util :as util]
            [monger.joda-time]
            [monger.json]
            [validateur.validation :refer :all]))

(def mongo-options
  {:host "localhost"
   :port 27017
   :db "doitnow"
   :doits-collection "doits"})

(connect! mongo-options)
(set-db! (get-db (mongo-options :db)))

(defn- with-oid
  [doit]
  (assoc doit :_id (util/object-id)))

(defn- created-now
  [doit]
  (assoc doit :created (time/now)))

(defn- modified-now
  [doit]
  (assoc doit :modified (time/now)))

(def doit-validator (validation-set
                      (presence-of :_id)
                      (presence-of :title)
                      (presence-of :created)
                      (presence-of :modified)))

(defn create-doit
  [doit]
  (let [new-doit (created-now (modified-now (with-oid doit)))]
    (if (valid? doit-validator new-doit)
      (if (ok? (collection/insert (mongo-options :doits-collection) new-doit))
        new-doit
        (throw (Exception. "Write Failed")))
      (throw (IllegalArgumentException.)))))
