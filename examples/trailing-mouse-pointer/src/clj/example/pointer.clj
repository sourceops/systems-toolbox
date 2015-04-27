(ns example.pointer
  (:gen-class)
  (:require
    [clojure.core.match :refer [match]]
    [matthiasn.systems-toolbox.component :as comp]))

(defn mk-state [_] (atom {:count 0}))

(defn process-mouse-pos
  "Handler function for received mouse positions, increments counter and returns mouse position to sender."
  [app msg put-fn]
  (let [[_ params] msg]
    (swap! app update-in [:count] inc)
    (put-fn (with-meta [:cmd/mouse-pos (assoc params :count (:count @app))] (meta msg)))))

(defn in-handler
  "Handle incoming messages: process / add to application state."
  [app put-fn msg]
  (match msg
         [:cmd/mouse-pos _] (process-mouse-pos app msg put-fn)
         :else (println "Unmatched event in :pointer-cmp: " msg)))

(defn component [cmp-id] (comp/make-component cmp-id mk-state in-handler nil))
