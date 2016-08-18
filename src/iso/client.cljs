(ns iso.client
  (:require [rum.core :as r]
            [goog.dom :as dom]
            [iso.draw :as d]))

(enable-console-print!)

(defonce canvas-el (dom/createDom "canvas" #js {"width" "1000px" "height" "800px"}))

(def canvas-mixin
  {:did-mount (fn [state]
                (dom/appendChild
                 (js/ReactDOM.findDOMNode (:rum/react-component state))
                 canvas-el)

                state)})

(defn frame [delta & args]
  (let [d (/ delta 1000)]

    (let [ctx (.getContext canvas-el "2d")]

      (set! (.. ctx -fillStyle) "#222")

      (.beginPath ctx)
      (.fillRect ctx
                 0 0
                 (.-width canvas-el)
                 (.-height canvas-el))
      (.closePath ctx)

      (let [n 20]
        (doseq [pos (map (fn [i] [(* 50 i) (* -200 (Math/sin (+ d (* i (/ (* 2 Math/PI) n)))))]) (range n))]
          (d/draw-iso-cube ctx (d/v-+-v [0 250] pos)
                           :color "#f0f"
                           :scale 0.25))))))

(defonce animator
  (do
    (println "start animator")
    (js/requestAnimationFrame (fn cb [& args]
                                (apply frame args)
                                (js/requestAnimationFrame cb)))))

(r/defc canvas < canvas-mixin []
  [:div])

(r/defc root [state]
  [:div (canvas)])

(r/defc scaffold [state]
  (root @state))

(defn main []
  (defonce state (atom {}))
  (let [comp (r/mount (scaffold state) (dom/getElement "root"))]
    (add-watch state :watch (fn [k r o n]
                              (r/request-render comp)))))
