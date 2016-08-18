(ns iso.client
  (:require [rum.core :as r]
            [goog.dom :as dom]
            [iso.draw :as d]
            [iso.effects :as e]))

(enable-console-print!)

(defonce canvas-el (dom/createDom "canvas" #js {"width" "1000px" "height" "800px"}))

(def canvas-mixin
  {:did-mount (fn [state]
                (dom/appendChild
                 (js/ReactDOM.findDOMNode (:rum/react-component state))
                 canvas-el)

                state)})


(defn frame [delta & args]
  (let [ctx (.getContext canvas-el "2d")]
    (set! (.. ctx -fillStyle) "#222")

    (let [width (.-width canvas-el)
          height (.-height canvas-el)]
      (.beginPath ctx)
      (.fillRect ctx
                 0 0
                 width height)
      (.closePath ctx))


    (e/draw-sine-wave ctx
                      (.-width canvas-el)
                      (.-height canvas-el)
                      delta)

    (e/draw-circular ctx
                     (.-width canvas-el)
                     (.-height canvas-el)
                     delta)))

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
