(ns c2po.literals)

(def c2po-literal-prefix "com.keminglabs/c2po")
(def tag-ns-sep
  "Chararcter to use as namespace separator in tagged literals. See http://dev.clojure.org/jira/browse/CLJ-1100"
  "$")

(defn print-literal [prefix name x writer]
  (.write writer (str "#" c2po-literal-prefix tag-ns-sep
                      prefix tag-ns-sep
                      name " " (pr-str (into {} x)))))
