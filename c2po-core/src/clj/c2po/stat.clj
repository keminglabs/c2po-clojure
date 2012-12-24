(ns c2po.stat
  (:use [c2po.records :only [def-c2po-record]]))

(def-c2po-record "stat" sum)
(def-c2po-record "stat" density)
(def-c2po-record "stat" quantiles)

