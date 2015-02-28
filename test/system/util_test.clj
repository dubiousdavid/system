(ns system.util-test
  (:use midje.sweet system.util))

(fact (try-start :divide (/ 1 0)) =>
  (throws java.lang.Exception "Failed to start :divide. Divide by zero"))
(fact (try-stop :divide (/ 1 0)) =>
  (throws java.lang.Exception "Failed to stop :divide. Divide by zero"))
