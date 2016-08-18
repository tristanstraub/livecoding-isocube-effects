(ns iso.draw)

(def ^:dynamic *color* "#777")

(defn move-to [ctx x y]
  (.moveTo ctx x y))

(defn line-to [ctx x y]
  (.lineTo ctx x y))

(defn draw-line [ctx points]
  (set! (.. ctx -strokeStyle) *color*)

  (apply move-to ctx (first points))
  (apply line-to ctx (last points))

  (.stroke ctx))

(defn draw-poly [ctx points]
  (set! (.. ctx -strokeStyle) *color*)

  (apply move-to ctx (first points))

  (doseq [p (rest points)]
    (apply line-to ctx p))

  (apply line-to ctx (first points))

  (.stroke ctx))

(defn draw-silhouette [ctx]
  (draw-poly ctx [[0 0]
                 [100 100]
                 [100 0]]))

(defn s-*-v [s v]
  (mapv * (repeat s) v))

(defn v-+-v [a b]
  (mapv + a b))

(defn v-*-v [a b]
  (mapv * a b))

(defn squish [squishiness v]
  (v-*-v [1 squishiness] v))

(defn outline [squishiness middle]
  (->> [[-1 0] [0 -1] [1 0] [1 middle] [0 (inc middle)] [-1 middle]]
       (map (partial squish squishiness))))

(defn draw-iso-cube [ctx pos & {:keys [color scale] :or {scale 1}}]
  (let [middle      2
        squishiness 0.5
        t #(->> %
                (map (partial s-*-v 100))
                (map (partial v-+-v [150 150]))
                (map (partial s-*-v scale))
                (map (partial v-+-v pos)))]

    (binding [*color* color]
      (draw-poly ctx
                 (->> (outline squishiness middle)
                      t))

      (doseq [line [[[-1 0] [0 (/ middle 2)]]
                    [[1 0] [0 (/ middle 2)]]
                    [[0 (/ middle 2)] [0 (+ middle (/ middle 2))]]]]
        (draw-line ctx
                   (->> line
                        (map (partial squish squishiness))
                        t))))))
