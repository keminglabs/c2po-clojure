;;This tutorial covers basic usage and plot creation.

(require
 ;;interfaces to the c2po compiler
 '[c2po.core :refer [c2po render!]]

 ;;pieces of the grammar of graphics
 '[c2po.geom :as geom]
 '[c2po.stat :as stat]
 '[c2po.group :as group]
 '[c2po.scale :as scale]

 ;;sample datasets
 '[vomnibus.r :refer [mtcars]]

 '[clojure.pprint :refer [pprint]])



;;This is a spec for a simple scatterplot of car fuel efficiency (in miles per gallon) against weight (in tons).
(def scatterplot-spec
  {:data mtcars
   :geom :point
   :mapping {:x :wt
             :y :mpg}})

;;This client contains a built-in web server to display plots; calling:

(render! scatterplot-spec)

;;automatically starts a webserver on http://localhost:8987 and renders the provided spec via the public c2po compiler.
;;(To use another port, start the server manually via `(c2po.livereload-server/start-server PORT)` before calling the `render!`.)
;;Additional calls to render! will automatically update the plot in your browser---no need to refresh.

;;If you would like the raw SVG string, call:

(c2po scatterplot-spec)

;;If your company has an internally-hosted version of the compiler, you can pass its URL as the second argument or call `(c2po.core/set-compiler-url! URL)` to change the default.

;;c2po has smart defaults, but you can easily override them by using records in your plot specifications instead of keywords.
;;We can change the radius of the points in the plot like so:

(render!
 {:data mtcars
  :geom (geom/point :radius 3)
  :mapping {:x :wt
            :y :mpg}})

;;Note that we are working entirely with data structures.
;;Instead of writing out a new spec, we could have derived it from the original one by replacing the `:point` keyword with the more detailed record returned by the `(geom/point :radius 3)` constructor:

(render!
 (assoc scatterplot-spec
   :geom (geom/point :radius 3)))

;;Instead of giving the radius aesthetic a fixed value, we can also map it to a dimension of the data.
;;The cars in this dataset all have

(distinct (map :cyl mtcars)) ;;=> (6 4 8)

;;cylinders in their engines.
;;We can map this directly to points with 4, 6, and 8 pixels;

(render!
 {:data mtcars
  :geom :point
  :mapping {:x :wt :y :mpg
            :radius :cyl}})

;;or manually specify the radial scale (a map of cyl -> pixel radius) to make the points more visually separable.

(render!
 {:data mtcars
  :geom :point
  :mapping {:x :wt :y :mpg
            :radius :cyl}
  :scales {:radius {4 2, 6 6, 8 12}}})

;;Some of the larger circles could be obscuring smaller ones, so a better choice might be to make the points semi-transparent, `(geom/point :opacity 0.5)`, or to map the number of cylinders to fill color instead of radius:

(render!
 {:data mtcars
  :geom (geom/point :radius 8)
  :mapping {:x :wt :y :mpg
            :fill :cyl}
  :scales {:fill {4 "hsl(0,0%,90%)"
                  6 "hsl(0,0%,50%)"
                  8 "hsl(0,0%,20%)"}}})

;;By default the x and y scales of a plot are linear scales that cover the range of the data.
;;We can explicitly provide  scales to control the plot's domains, ranges, and labels.

(render! (assoc scatterplot-spec
           :scales {:x (scale/linear :domain [0 10] :label "Weight" :ticks (range 11))
                    :y (scale/linear :domain [10 40] :label "Miles per gallon")}))
