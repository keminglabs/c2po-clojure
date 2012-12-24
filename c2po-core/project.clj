(defproject com.keminglabs/c2po-core "0.1.0-SNAPSHOT"
  :description "Core c2po client"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [clj-http "0.6.3" :exclusions [commons-codec]]
                 [slingshot "0.10.3"]]

  :min-lein-version "2.0.0"
  :source-paths ["src/clj" "src/cljs"])