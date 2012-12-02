(defproject com.keminglabs/c2po "0.1.0-SNAPSHOT"
  :description "Free-to-use client for the C2PO grammar of graphics"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [clj-http "0.5.8"]
                 [slingshot "0.10.3"]]
  
  :profiles {:dev {:dependencies [
                                  ;;Bits for the livereload server
                                  [compojure "1.1.3" :exclusions [org.clojure/tools.macro]]
                                  [aleph "0.3.0-beta8" :exclusions [cheshire]]
                                  [hiccup "1.0.2"]

                                  ;;Vomnibus is a collection of sample data sets
                                  [com.keminglabs/vomnibus "0.3.2"]]}}
  
  :min-lein-version "2.0.0"
  :source-paths ["src/clj" "src/cljs"])
