(ns c2po.geom
  (:use [c2po.records :only [def-c2po-record]]))

(def-c2po-record "geom" point)
(def-c2po-record "geom" line)
(def-c2po-record "geom" bar)
(def-c2po-record "geom" boxplot)

