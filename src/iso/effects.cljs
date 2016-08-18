(ns iso.effects
  (:require [iso.draw :as d]))

(defn draw-sine-wave [ctx width height delta]
  (let [d (/ delta 1000)]
    (let [n 20]
      (doseq [pos (map (fn [i] [(* 50 i) (* -200 (Math/sin (+ d (* i (/ (* 2 Math/PI) n)))))]) (range n))]
        (d/draw-iso-cube ctx (d/v-+-v [0 250] pos)
                         :color "#f0f"
                         :scale 0.25)))))

(defn draw-circular [ctx width height delta]
  (let [d (/ delta 1000)]
    (let [n 20]
      (doseq [pos (map (fn [i] [(* 200 (Math/cos (+ d (* i (/ (* 2 Math/PI) n)))))
                                (* 200 (Math/sin (+ d (* i (/ (* 2 Math/PI) n)))))])
                       (range n))]

        (d/draw-iso-cube ctx (d/v-+-v [(/ width 2) (/ height 2)] pos)
                         :color "#f0f"
                         :scale 0.25)))))
