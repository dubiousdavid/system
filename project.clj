(defproject system "0.1.0"
  :description "Start and stop services in a particular order."
  :url "https://github.com/dubiousdavid/system"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.roomkey/example "0.3.0"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]]
                   :source-paths ["dev"]}})
