(ns c2po.scale
  (:use [c2po.records :only [def-c2po-record]]))

(def-c2po-record "scale" linear)
(def-c2po-record "scale" log)
