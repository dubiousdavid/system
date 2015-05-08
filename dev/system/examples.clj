(ns system.examples
  (:use system rk.example))

(defn ldap-connect [config]
  config)

(defn ldap-close [conn]
  nil)

(defn mysql-connect [config]
  config)

(defn mysql-close [conn]
  nil)

(defn mk-routes [ldap mysql]
  [ldap mysql])

(defn http-server-start [port routes]
  [port routes])

(defn http-server-stop [server]
  nil)

(defrecord LDAP [conn]
  Service
  (start [this config deps]
    (->LDAP (ldap-connect (:ldap config))))
  (stop [this _]
    (->LDAP (ldap-close conn))))

(defrecord MySQL [conn]
  Service
  (start [this config deps]
    (->MySQL (mysql-connect (:mysql config))))
  (stop [this _]
    (->MySQL (mysql-close conn))))

(defrecord HTTP [server]
  Service
  (start [this config deps]
    (let [routes (mk-routes (:ldap deps) (:mysql deps))]
      (->HTTP (http-server-start (:http config) routes))))
  (stop [this _]
    (->HTTP (http-server-stop server))))

(def config
  {:ldap {:host "localhost" :port 389}
   :mysql {:host "localhost" :port 3306}
   :http {:port 80}})

(defsystem system
  :ldap (->LDAP nil)
  :mysql (->MySQL nil)
  :http (->HTTP nil))

(ex system)
(ex "start" (->map (start system config)))
(ex "stop" (-> system (start config) stop))
