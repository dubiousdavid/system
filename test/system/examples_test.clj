(ns system.examples-test
  (:use system system.util midje.sweet system.examples))

(facts "defsystem"
  (fact (pairs system) =>
    [[:ldap (map->LDAP {:conn nil})]
     [:mysql (map->MySQL {:conn nil})]
     [:http (map->HTTP {:server nil})]]))
(facts "start"
  (fact (->map (start system config)) =>
    {:ldap (map->LDAP {:conn {:port 389, :host "localhost"}}),
     :mysql (map->MySQL {:conn {:port 3306, :host "localhost"}}),
     :http
     (map->HTTP
      {:server
       [{:port 80}
        [(map->LDAP {:conn {:port 389, :host "localhost"}})
         (map->MySQL {:conn {:port 3306, :host "localhost"}})]]})}))
(facts "stop"
  (fact (-> system (start config) stop pairs) =>
    [[:ldap (map->LDAP {:conn nil})]
     [:mysql (map->MySQL {:conn nil})]
     [:http (map->HTTP {:server nil})]]))
(facts "try-start"
  (fact (try-start :divide (/ 1 0)) =>
    (throws java.lang.Exception "Failed to start :divide. Divide by zero")))
(facts "try-stop"
  (fact (try-stop :divide (/ 1 0)) =>
    (throws java.lang.Exception "Failed to stop :divide. Divide by zero")))
