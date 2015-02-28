(ns system.util
  (:use rk.example))

(defn try-action [action k form]
  `(try ~form
        (catch Exception e#
          (throw (Exception. (str "Failed to " ~action " " ~k ". " (.getMessage e#)))))))

(defmacro try-start [k form]
  (try-action "start" k form))

(ex (try-start :divide (/ 1 0)))

(defmacro try-stop [k form]
  (try-action "stop" k form))

(ex (try-stop :divide (/ 1 0)))
