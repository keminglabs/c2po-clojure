(ns c2po.core
  (:require [clj-http.client :as client]
            [clojure.set :as set]))

(def url "C2PO free compiler URL"
  "http://c2po.keminglabs.com")


(defn motd
  "Retrive message of the day (i.e., latest release notes)."
  [url]
  (:body (client/get (str url "/motd"))))

(defn print-once-motd! [url]
  (defonce ^:private motd-printed
    (future
      (try
        (println (motd url))
        (catch Throwable e
          (println (str "Trouble connecting to C2PO server at: " url "\n\n"
                        "Are you connected to the Internet?")))))))

(defn valid-spec? [m]
  (and (map? m)
       (set/subset? #{:mapping :data :geom} (set (keys m)))))

(defn c2po
  ([spec] (c2po spec url))
  ([spec url]

     ;;Print MOTD on first call to c2po fn.
     (print-once-motd! url)

     (if-not (valid-spec? spec)
       (throw (Error. "C2PO plot spec should be a map with, at minimum, :mapping, :data, and :geom keys."))
       (:body (client/post url {:multipart [{:name "c2po" :content (pr-str spec)}]
                                :throw-entire-message? true})))))
