(ns c2po.data-readers
  (:use [c2po.literals :only [c2po-literal-prefix tag-ns-sep]])
  (:require c2po.records))

(defn record-x [x m]
  ((resolve (symbol "c2po.records" (str "map->" x))) m))

(defn data-reader-for [type x prefix-type]
  (let [tag (case prefix-type
              :none (str x)
              :type (str type tag-ns-sep x)
              :full (str c2po-literal-prefix tag-ns-sep type tag-ns-sep x))]
    {(symbol tag) (partial record-x x)}))

(def recs [["geom" c2po.records/geom]
           ["stat" c2po.records/stat]
           ["group" c2po.records/group]])

(def full-prefixed
  (apply merge
         (for [[type xs] recs, x xs]
           (data-reader-for type x :full))))

(def type-prefixed
  (apply merge
         (for [[type xs] recs, x xs]
           (data-reader-for type x :type))))

(def unprefixed
  ;;Requires special handling for stat/geom collision for boxplot.
  ;;TODO: stat boxplot should be generic stat n-tiles instead.
  (->
   (apply merge
          (conj (for [[type xs] recs, x xs]
                  (data-reader-for type x :none))
                (data-reader-for "stat" "boxplot" :type)
                (data-reader-for "geom" "boxplot" :type)))
   (dissoc 'boxplot)))
