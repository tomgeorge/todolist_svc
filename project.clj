(defproject todolist_svc "0.4.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [ring-middleware-format "0.3.2"]
                 [ring/ring-json "0.3.0"]
                 [com.taoensso/timbre "3.1.6"]
                 [cheshire "5.3.1"]
                 [com.novemberain/monger "1.7.0"]
                 [com.novemberain/validateur "2.0.0"]]  
  :plugins [[lein-ring "0.8.10"]
            [quickie "0.2.5"]] 
  :ring {:handler todolist_svc.handler/app}
  :source-paths ["src"]
  :test-paths ["test"]
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
