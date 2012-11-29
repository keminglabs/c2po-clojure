(ns c2po.examples.usage
  (:require [c2po.core :refer [c2po]]
            [c2po.livereload-server :refer [render!]]

            [c2po.geom :as geom]
            [c2po.stat :as stat]
            [c2po.group :as group]

            [vomnibus.r :refer [mtcars]]
            [clojure.pprint :refer [pprint]]))

;;This is a spec for a simple scatterplot of car fuel efficiency (in miles per gallon) against weight (in tons)
(def scatterplot-spec
  {:data mtcars
   :geom :point
   :mapping {:x :wt
             :y :mpg}})

;;The c2po function sends this spec to the public c2po compiler and returns an svg string:
(c2po scatterplot-spec) ;;"<svg height=\"660\" width=\"660\" ...
;;If your company has an internally-hosted version of the compiler, you can pass its URL as the second argument.

;;Getting a raw SVG string back isn't very convenient, so to aid interactive data visualization this client contains a livereload web server.
;;Calling

(render! scatterplot-spec)

;;automatically starts a webserver on http://localhost:8987 and renders the provided spec.
;;(If you need to use another port, you can start the server manually via (c2po.livereload-server/start-server PORT) before calling the render! function.)
;;Additional calls to render! will automatically update the plot in your browser---no need to refresh.

;;c2po has smart defaults, but you can easily override them by using records in your plot specifications instead of keywords.
;;We can change the radius of the points in the plot like so:

(render!
 {:data mtcars
  :geom (geom/point :radius 3)
  :mapping {:x :wt
            :y :mpg}})

;;Note that we are working entirely with data structures, so we could have just as well written:

(render!
 (assoc scatterplot-spec
   :geom (geom/point :radius 3)))
