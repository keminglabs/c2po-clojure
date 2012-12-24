C2PO Clojure language bindings
==============================

[C2PO](http://keminglabs.com/c2po/) is a grammar of graphics implementation inspired by Hadley Wickham's ggplot2 library.
This Clojure package uses the free online C2PO compiler and is limited to 1 MB of data.
Plot specifications are compiled directly to an SVG string, which is returned:

```clojure
(require '[c2po.core :refer [c2po]]
         '[c2po.geom :as geom])

(def scatterplot {:data (repeatedly 20 #(hash-map :this (rand) :that (rand)))
                  :mapping {:x :this :y :that}
                  :geom (geom/point :opacity 0.5)})

;;The C2PO server returns an SVG string with embedded CSS.
;;You can view it by saving it to disk and opening in a browser:
(spit "rad_scatterplot.svg" (c2po scatterplot))

;;Alternatively, you can use the built-in livereload web server:
(require '[c2po.livereload-server :refer [render!]])
(render! scatterplot)
;;which puts your plot at http://localhost:8987
;;Additional calls to `render!` will automatically refresh the browser.
```

Clone this repository and walk through `examples/1-usage.clj` for a more detailed tutorial.

This is an *experimental* package; the package API, plot specification syntax, and remote server may change or disappear at any time.

Install
-------

Add to your `project.clj`:

```clojure
[com.keminglabs/c2po "0.1.0-SNAPSHOT"]
```
