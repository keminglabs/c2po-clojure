(ns c2po.records
  (:use [c2po.literals :only [print-literal]])
  (:require [clojure.string :as str]))

(defmacro def-c2po-record
  [prefix name]
  (let [record-name (str/capitalize (clojure.core/name name))]
    `(do
       (defrecord ~(symbol record-name) [])
       ;;print the record appropriately
       (defmethod print-method ~(symbol record-name) [x# w#]
         (print-literal ~prefix ~(clojure.core/name name) x# w#))
       
       ;;record constructor
       (defn ~name [& kwargs#]
         (~(symbol (str "map->" record-name))
          (apply hash-map kwargs#))))))




