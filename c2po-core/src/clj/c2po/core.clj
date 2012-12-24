(ns c2po.core
  (:require [clj-http.client :as client]
            [clojure.set :as set]
            [slingshot.slingshot :refer [try+]]))

(defonce ^:dynamic *url*
  "http://c2po.keminglabs.com")

(defn set-compiler-url! [url]
  (def ^:dynamic *url* url))


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
  ([spec] (c2po spec *url*))
  ([spec url]

     ;;Print MOTD on first call to c2po fn.
     (print-once-motd! url)

     (if-not (valid-spec? spec)
       (throw (Error. "C2PO plot spec should be a map with, at minimum, :mapping, :data, and :geom keys."))
       (try+
        (:body (client/post url {:multipart [{:name "c2po" :content (pr-str spec)}]
                                 :throw-entire-message? true}))
        (catch (contains? % :body) {body :body}
          (throw (Error. body)))))))

(def render!
  "Render c2po spec and display on livereload server."
  (try
    (require 'c2po-livereload.server)
    (eval '(fn [& args]
             (when-let [svg (apply c2po args)]
               (c2po-livereload.server/display! svg))))
    
    (catch java.io.FileNotFoundException e
      (fn [& args]
        (throw (Error. "c2po-livereload.server not found on your classpath. Make sure it's listed in your project.clj dependency list."))))))
