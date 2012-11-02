(ns c2po.literals)

(def c2po-literal-prefix "com.keminglabs/c2po")

(defn print-literal [prefix name x writer]
  (.write writer (str "\"#" c2po-literal-prefix "." prefix "." name " " (pr-str (into {} x)) "\"")))
