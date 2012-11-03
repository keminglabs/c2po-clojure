C2PO Clojure language bindings
==============================

[C2PO](http://keminglabs.com/c2po/) is a grammar of graphics implementation inspired by Hadley Wickham's ggplot2 library.
This Clojure package uses the free online C2PO compiler and is limited to 1 MB of data.
Plot specifications are compiled directly to an SVG string, which is returned:

```clojure
(use '[c2po.core :only [c2po set-data-readers!]])

;;Set local unprefixed data readers so you can say things like #point {}
(set-data-readers! :unprefixed)

(def scatterplot {:data (repeatedly 20 #(hash-map :this (rand) :that (rand)))
                  :mapping {:x :this :y :that}
                  :geom #point {:opacity 0.5}})

;;The C2PO server returns an SVG string with embedded CSS.
;;The easiest way to view it is to save it to disk and open in a browser:
(spit "rad_scatterplot.svg" (c2po scatterplot))

;;(Auto-reload + built-in server coming soon)
```

This is an *experimental* package; the package API, plot specification syntax, and remote server may change or disappear at any time.

Install
-------

Add to your `project.clj`:

```clojure
[com.keminglabs/c2po "0.1.0-SNAPSHOT"]
```
