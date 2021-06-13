(ns hello.core
  (:gen-class)
  (:use [ring.adapter.jetty]
        [ring.util.response]))

(defonce server (atom nil))

(defn ok [body] {:status 200 :body body})

(defn html [res]
  (assoc res :headers {"Content-Type" "text/html; charset=utf-8"}))

(defn view-index [req] "<h1>Index</h1>")

(defn index [req] (-> (view-index req) ok html))

(defn handler [req]
  (index req))

(defn start-server []
  (when-not @server
    (reset! server
            (run-jetty handler
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
