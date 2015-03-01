(ns system
  "Start and stop services in a particular order."
  (:use [system.util :only [try-start try-stop]]))

(defprotocol Service
  (start [this config] [this config deps]
    "Start a service with the given config and deps.")
  (stop [this] [this config]
    "Stop a service with the given config."))

(defrecord SystemMap [kv-pairs]
  Service
  (start [this config]
    (->> kv-pairs
         (reduce (fn [[started pairs] [k v]]
                   (let [service (try-start k (start v config started))
                         started (assoc started k service)]
                     [started (conj pairs [k service])]))
                 [{} []])
         second
         ->SystemMap))
  (stop [this] (stop this nil))
  (stop [this config]
    (->> (reverse kv-pairs)
         (reduce (fn [pairs [k v]]
                   (let [service (try-stop k (stop v config))]
                     (conj pairs [k service])))
                 [])
         reverse
         vec
         ->SystemMap)))

(defmacro defsystem
  "Define a system. Takes a sequence of key/value pairs."
  [n & args]
  (let [kv-pairs (->> args (partition 2) (map vec) vec)]
    `(def ~n (SystemMap. ~kv-pairs))))
