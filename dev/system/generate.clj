(ns system.generate
  (:use rk.example)
  (:require system.examples))

(defn- tests [f]
  (f 'system.examples :use '[system])
  (f 'system.util))

(defn preview-tests []
  (tests gen-facts))

(defn gen-tests []
  (tests gen-facts-file))
