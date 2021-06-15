(ns hello.core
  (:gen-class)
  (:require [ring.adapter.jetty :as s]
            [ring.util.response]))

(defonce server (atom nil))

(defn ok [body] {:status 200 :body body})

(defn not-found []
  {:status 404
   :body "<h1>404 page not found</1>"})

(defn html [res]
  (assoc res :headers {"Content-Type" "text/html; charset=utf-8"}))

(defn view-index [req] "<h1>Index</h1>")
(defn view-about [req] "<h1>About</h1>")

(defn index [req] (-> (view-index req) ok html))
(defn about [req] (-> (view-about req) ok html))

(def routes {"/" index "/about" about})

(defn match-route [uri]
  (get routes uri))

(defn handler [req]
  (let [uri (:uri req)
        maybe-fn (match-route uri)]
    (if maybe-fn
      (maybe-fn req)
      (not-found))))

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
