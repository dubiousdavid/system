(ns system.examples-test
  (:use system midje.sweet system.examples))

(fact system =>
  (map->SystemMap
   {:kv-pairs
    [[:ldap (map->LDAP {:conn nil})]
     [:mysql (map->MySQL {:conn nil})]
     [:http (map->HTTP {:server nil})]]}))
(facts "start"
  (fact (start system config) =>
    (map->SystemMap
     {:kv-pairs
      [[:ldap (map->LDAP {:conn {:port 389, :host "localhost"}})]
       [:mysql (map->MySQL {:conn {:port 3306, :host "localhost"}})]
       [:http
        (map->HTTP
         {:server
          [{:port 80}
           [(map->LDAP {:conn {:port 389, :host "localhost"}})
            (map->MySQL {:conn {:port 3306, :host "localhost"}})]]})]]})))
(facts "stop"
  (fact (-> system (start config) stop) =>
    (map->SystemMap
     {:kv-pairs
      [[:ldap (map->LDAP {:conn nil})]
       [:mysql (map->MySQL {:conn nil})]
       [:http (map->HTTP {:server nil})]]})))
