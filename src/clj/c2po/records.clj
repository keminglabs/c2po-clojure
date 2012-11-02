(ns c2po.records
  (:use [c2po.literals :only [print-literal]]))

(defmacro def-c2po-records
  [prefix & names]
  `(do
     (def ~(symbol prefix) ~(vec (map name names)))
     ~@(for [name names]
         `(do
            (defrecord ~name [])
            (defmethod print-method ~name [x# w#]
              (print-literal ~prefix (clojure.core/name '~name) x# w#))))
     nil))

(def-c2po-records "geom" point line bar boxplot)
(def-c2po-records "stat" sum)
(def-c2po-records "group" bin)





