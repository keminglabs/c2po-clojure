(ns c2po.core
  (:require c2po.data-readers
            [clj-http.client :as client]))

(def url "C2PO free compiler URL"
  "http://c2po.keminglabs.com")

(defn valid-spec? [m]
  (and (map? m)
       (every? #{:mapping :data :geom} (keys m))))

(defn c2po
  ([spec] (c2po spec url))
  ([spec url]
     (if-not (valid-spec? spec)
       (throw (Error. "C2PO plot spec should be a map with, at minimum, :mapping, :data, and :geom keys."))
       (client/post url {:multipart [{:name "c2po" :content (pr-str spec)}]}))))


(defn set-data-readers!
  ([] (set-data-readers! :unprefixed))
  ([type] (set! *data-readers* (case type
                                 :unprefixed c2po.data-readers/unprefixed
                                 :type-prefixed c2po.data-readers/type-prefixed
                                 :full-prefixed c2po.data-readers/full-prefixed
                                 (Error. "Available literal types are :unprefixed, :type-prefixed, and :full-prefixed.")))))


(comment
  (set-data-readers! :unprefixed)
  (def scatterplot {:data (repeatedly 2 #(hash-map :a (rand) :b (rand)))
                    :mapping {:x :a :y :b}
                    :geom #point {:opacity 0.5}})
  (prn scatterplot)
  
  (c2po scatterplot "http://localhost:5000")

  )
