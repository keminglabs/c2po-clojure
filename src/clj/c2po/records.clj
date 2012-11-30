(ns c2po.records
  (:require [c2po.literals :refer [literal-tag print-literal]]
            [clojure.string :as str]))

(defprotocol PlainMappable
  (to-map [this] "Converts the record into a plain map with a :_tag key suitable for serialization into JSON."))

(defmacro def-c2po-record
  [prefix name]
  (let [record-name (str/capitalize (clojure.core/name name))]
    `(do
       (defrecord ~(symbol record-name) []
         PlainMappable
         (to-map [this#]
           (assoc this# :_tag (literal-tag ~prefix ~(clojure.core/name name))))

         ;;Note, this currently isn't called because of a toplevel condp in cheshire.generate/generate.
         ;;See https://github.com/dakrone/cheshire/issues/32
         ;; cheshire.generate/JSONable
         ;; (to-json [this# jg#]
         ;;   (cheshire.generate/encode-map (to-map this#)
         ;;                                 jg#))

         )

       ;;print the record appropriately
       (defmethod print-method ~(symbol record-name) [x# w#]
         (print-literal ~prefix ~(clojure.core/name name) x# w#))

       ;;record constructor
       (defn ~name [& kwargs#]
         (~(symbol (str "map->" record-name))
          (apply hash-map kwargs#))))))
