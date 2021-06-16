(ns hello.core
  (:gen-class)
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [ring.adapter.jetty :as s]
            [ring.util.response :as res]))

(defonce server (atom nil))

(defn ok [body] {:status 200 :body body})

(defn html [res]
  (res/content-type res "text/html; charset=utf-8"))

(defn view-index [req] "<h1>Index</h1>")
(defn view-about [req] "<h1>About</h1>")

(defn index [req] (-> (view-index req) res/response html))
(defn about [req] (-> (view-about req) res/response html))

(defroutes handler
  (GET "/" req index)
  (GET "/about" req about)
  (route/not-found "<h1>404 page not found</1>"))

(defn start-server []
  (when-not @server
    (reset! server
            (s/run-jetty handler
                         {:address "0.0.0.0"
                          :port 8080
                          :join? false}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn restart-server []
  (when @server
    (stop-server)
    (start-server)))

(defn -main [& args]
  (start-server))
