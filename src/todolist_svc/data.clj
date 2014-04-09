(ns todolist_svc.data
  (:require [clj-time.core :as time]
            [monger.collection :as collection]
            [monger.core :refer [connect! get-db set-db!]]
            [monger.result :refer [ok?]]
            [monger.util :as util]
            [slingshot.slingshot :refer [throw+]]
            [validateur.validation :refer :all])
  (:import (org.bson.types ObjectId)))

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

(defn- object-id? [id]
  (and
    (not (nil? id))''
    (string? id)
    (re-matches #"[0-9a-f]{24}" id)))

(defn create-doit
  [doit]
  (let [new-doit (created-now (modified-now (with-oid doit)))]
    (if (valid? doit-validator new-doit)
      (if (ok? (collection/insert (mongo-options :doits-collection) new-doit))
        new-doit
        (throw+ {:type ::failed} "Create Failed"))
      (throw+ {:type ::invalid} "Invalid DoIt"))))

(defn get-doit
  [id]
  (if (object-id? id)
    (let [doit (collection/find-one-as-map
                 (mongo-options :doits-collection) { :_id (ObjectId. id)})]
      (if (nil? doit)
        (throw+ {:type ::not-found} (str id " not found"))
        doit))
    (throw+ {:type ::invalid} "Invaild DoIt ID")))

(defn delete-doit
  [id]
  (validate [id :ObjectId])
  (let [doit (get-doit id)]
    (if (ok? (collection/remove-by-id 
             (mongo-options :doits-collection) (ObjectId. id)))
    doit 
    (throw+ {:type ::failed} "Delete Failed"))))
