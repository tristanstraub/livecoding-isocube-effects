(ns iso.effects
  (:require [iso.draw :as d]))

(defn draw-sine-wave [ctx width height delta]
  (let [d (/ delta 1000)]
    (let [n 20]
      (doseq [pos (map (fn [i] [(* 50 i) (* -200 (Math/sin (+ d (* i (/ (* 2 Math/PI) n)))))]) (range n))]
        (d/draw-iso-cube ctx (d/v-+-v [0 250] pos)
                         :color "#f0f"
                         :scale 0.25)))))

(defn brighten [k]
  (let [v (* k 3)]
    (if (> v 15)
      15
      v)))

(defn draw-circular [ctx width height delta]
  (let [d (/ delta 1000)]
    (let [n 20]
      (doseq [[pos i] (map (fn [i] [[(* 200 (Math/cos (+ d (* i (/ (* 2 Math/PI) n)))))
                                     (* 200 (Math/sin (+ d (* i (/ (* 2 Math/PI) n)))))]
                                    i])
                           (range n))]

        (d/draw-iso-cube ctx (d/v-+-v [(/ width 2) (/ height 2)] pos)
                         :color (str "#"
                                     (nth "0123456789abcdef" (brighten (mod (/ (* 16 (* 3 i)) n) 16)))
                                     (nth "0123456789abcdef" (brighten (mod (/ (* 16 i) n) 16)))
                                     (nth "0123456789abcdef" (brighten (mod (/ (* 16 (* 7 i)) n) 16))))
                         :scale 0.25)))))
