(defproject system "0.1.0"
  :description "Start and stop services in a particular order."
  :url "https://github.com/dubiousdavid/system"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]]
                   :source-paths ["dev"]
                   :plugins [[codox "0.8.11"]]
                   :codox {:src-dir-uri "https://github.com/dubiousdavid/system/blob/master/"
                           :src-linenum-anchor-prefix "L"
                           :output-dir "."
                           :exclude [system.examples]}}})
