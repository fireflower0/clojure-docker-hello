(ns hello.core
  (:gen-class)
  (:use [ring.adapter.jetty]
        [ring.util.response]))

(defonce server (atom nil))

(defn handler [request]
  (content-type
   (response "<h1>Hello, world!</h1>")
   "text/html"))

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
