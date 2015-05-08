(ns system.util)

(defn try-action [action k form]
  `(try ~form
        (catch Exception e#
          (throw (Exception. (str "Failed to " ~action " " ~k ". " (.getMessage e#)))))))

(defmacro try-start [k form]
  (try-action "start" k form))

(defmacro try-stop [k form]
  (try-action "stop" k form))
