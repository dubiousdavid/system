# system

Start and stop services in a particular order.

Based on ideas from [component](https://github.com/stuartsierra/component). Differences from component:

1. No need to explicitly define dependencies for a particular service.  Simply order the services in the system map to facilitate dependency needs.
2. Configuration is first-class. To start the system or a particular service, a config value must be passed.  This allows you to start/stop a system using a development or production config in a functional way.
3. Systems can be combined and added to after inital creation.

## Installation

```clojure
[com.2tothe8th/system "0.1.0"]
```

## API Documentation

http://dubiousdavid.github.io/system/

## How to use

Example scenario:
```clojure
(use 'system)

(defrecord LDAP [conn]
  Service
  (start [this config deps]
    (->LDAP (ldap/connect (:ldap config))))
  (stop [this config]
    (->LDAP (ldap/close conn))))

(defrecord MySQL [conn]
  Service
  (start [this config deps]
    (->MySQL (mysql/connect (:mysql config))))
  (stop [this config]
    (->MySQL (mysql/close conn))))

(defrecord HTTP [server]
  Service
  (start [this config deps]
    (let [routes (mk-routes (:ldap deps) (:mysql deps))]
      (->HTTP (http/start (:http config) routes))))
  (stop [this _]
    (->HTTP (http/stop server))))

(defsystem system
  :ldap (->LDAP nil)
  :mysql (->MySQL nil)
  :http (->HTTP nil))

(def config
  {:ldap {:host "localhost" :port 389}
   :mysql {:host "localhost" :port 3306}
   :http {:port 80}})

;; Start the system
(def started (start system config))
;; Transform the system into a hash map
(->map started)
;; Stop the system
(stop started)
```

You can also use `conj` and `concat` with a system to add another service or combine multiple systems.

```clojure
(defsystem sys1
  :ldap (->LDAP nil))

(defsystem sys2
  :mysql (->MySQL nil))

;; Concatenate sys1 and sys2
(def sys3 (->SystemMap (concat sys1 sys2)))
;; Append the http service to sys1
(def sys4 (conj sys1 [:http (->HTTP nil)])
;; View the services in the order that they will be started
(pairs sys4)
```
