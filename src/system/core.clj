(ns system.core
  "Start and stop services in a particular order."
  (:use [system.util :only [try-start try-stop unique-keys? pairs? throwe]])
  (:import [clojure.lang Seqable IPersistentCollection]))

(defprotocol Service
  (start [this config] [this config deps]
    "Start a service with the given config and deps.")
  (stop [this] [this config]
    "Stop a service, optionally passing a config."))

(declare ->SystemMap)

(deftype SystemMap [kv-pairs]
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
         ->SystemMap))

  Seqable
  (seq [this] (seq kv-pairs))

  IPersistentCollection
  (empty [this] (SystemMap. []))
  (equiv [this that] (= kv-pairs (.kv-pairs that)))
  (cons [this x] (SystemMap. (conj kv-pairs x))))

(defn ->SystemMap
  "Create a new system map."
  [kv-pairs]
  (throwe (pairs? kv-pairs) "Uneven number of key/value pairs.")
  (throwe (unique-keys? kv-pairs) "Keys must be unique.")
  (SystemMap. kv-pairs))

(defmacro defsystem
  "Define a system. Takes a sequence of key/value pairs."
  [n & args]
  (let [kv-pairs (->> args (partition-all 2) (map vec) vec)]
    `(def ~n (->SystemMap ~kv-pairs))))

(defn ->map
  "Convert a SystemMap into a hash map."
  [x]
  (into {} (.kv-pairs x)))

(defn pairs
  "Convert a SystemMap into a vector of vector pairs."
  [x]
  (.kv-pairs x))
