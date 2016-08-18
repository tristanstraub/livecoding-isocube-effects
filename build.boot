#!/usr/bin/env boot
(set-env!
 :source-paths #{"src" "test"}
 :test-paths #{"test"}
 :resource-paths #{"resources"}
 :dependencies '[[org.clojure/clojure "1.8.0"]
                 ;; [org.clojure/clojurescript "1.8.51"]
                 [org.clojure/data.json "0.2.6"]
                 [http-kit "2.1.19"]
                 [ring "1.5.0"]
                 [ring-cors "0.1.8"]
                 [ring/ring-anti-forgery "1.0.1"]

                 [compojure "1.5.0"]
                 [secretary "1.2.3"]
                 [com.taoensso/sente "1.10.0"]
                 [rum "0.9.0"]
                 [org.clojure/core.match "0.3.0-alpha4"]

                 [midje "1.8.3" :scope "test"]
                 [pandeiro/boot-http "0.7.3" :scope "test"]
                 ;; [org.clojure/tools.nrepl "0.2.11" :scope "test"]
                 [adzerk/boot-reload "0.4.12" :scope "test"]
                 [adzerk/boot-cljs "1.7.228-1" :exclude [org.clojure/clojurescript] :scope "test"]
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]


                 [ring-middleware-format "0.7.0"]
                 [enlive "1.1.6"]
                 [cljs-ajax "0.5.8"]
                 [midje "1.8.3"]])

(task-options!
 pom {:project 'iso
      :version "0.1.0"}
 aot {:namespace '#{iso.core}}
 jar {:main 'iso.core
      :manifest {"Description" "Iso"}})

(require '[adzerk.boot-cljs :refer [cljs]])
(require '[pandeiro.boot-http :refer [serve]])
(require '[adzerk.boot-reload    :refer [reload]])

(deftask build-prod-client
  []
  (comp
   (cljs :compiler-options {;; :foreign-libs [{:file "threejs/build/three.js"
                            ;;                 :provides ["THREE"]}]
                            :optimizations :advanced
                            :parallel-build true})
   (target)))

(deftask run []
  (comp ;; (cider)
        (serve)
        (watch)
        (reload :on-jsload 'iso.client/main)
        (cljs :compiler-options {:optimizations :none
                                 :parallel-build true})))
