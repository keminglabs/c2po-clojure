(ns c2po-livereload.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [aleph.http :refer [wrap-ring-handler wrap-aleph-handler
                                start-http-server]]
            [lamina.core :refer [enqueue permanent-channel receive-all siphon]]
            [hiccup.core :refer [html]]
            [cheshire.core :refer [generate-string parse-string]]))


(defonce !server (atom nil))
(defonce !opts (atom {:port 8987}))
(defonce !current-plot (atom [:h1 "Ready"]))
(defonce broadcast-channel (permanent-channel))

;;Messages to livereload client.
;;http://help.livereload.com/kb/ecosystem/livereload-protocol
(defn lr-reload [path-to-reload]
  {:command "reload"
   :path path-to-reload
   :liveCSS true})

(defn lr-alert [msg]
  {:command "alert"
   :message msg})

(defn lr-hello []
  {:command "hello"
   :protocols ["http://livereload.com/protocols/official-7"]
   :serverName "c2po"})

;;Whenever the content of the current page changes, broadcast to livereload
(add-watch !current-plot :reload
           (fn [_ _ _ _] (enqueue broadcast-channel (generate-string (lr-reload "/")))))

(defn livereload-handler
  "When a websocket client connects, this function runs."
  [ch handshake]
  (receive-all ch (fn [msg]
                    (when (= "hello" (:command (parse-string msg true)))
                      ;;Say hello back
                      (enqueue ch (generate-string (lr-hello)))
                      ;;Subscribe it to events
                      (siphon broadcast-channel ch)))))

(defroutes main-routes
  (GET "/" []
       (html [:html
              [:head
               ;;TODO Livereload when accessing elsewhere from localhost.
               [:script {:src (str "/livereload.js?host=localhost&port=" (:port @!opts))}]]
              [:body
               [:div#visualization {:style "margin: auto;"}
                @!current-plot]]]))

  (GET "/livereload" []
       (wrap-aleph-handler livereload-handler))

  (GET "/livereload.js" []
       (slurp (io/resource "livereload.js")))

  (route/not-found "Not found."))

(def app (-> main-routes
             wrap-ring-handler))

(defn stop-server! []
  (when-let [s @!server]
    (s) ;;Aleph returns a function that, when called, stops the server.
    (reset! !server nil)))

(defn start-server!
  ([] (start-server! (:port @!opts)))
  ([port]
     (stop-server!)
     (swap! !opts assoc-in [:port] port)
     (reset! !server (start-http-server #'app {:port port :websocket true}))
     (println (str "Started c2po livereload server on port " port))))

(defn display! [x]
  (when-not @!server
    (start-server!))
  (reset! !current-plot x)
  nil)