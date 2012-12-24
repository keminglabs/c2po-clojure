(defproject com.keminglabs/c2po-livereload "0.1.0-SNAPSHOT"
  :description "Livereload server useful for interactive visualizations"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.3" :exclusions [org.clojure/tools.macro]]
                 [aleph "0.3.0-beta8" :exclusions [cheshire]]
                 [hiccup "1.0.2"]

                 ;;Vomnibus is a collection of sample data sets
                 [com.keminglabs/vomnibus "0.3.2"]]

  :min-lein-version "2.0.0"
  :source-paths ["src/clj"])