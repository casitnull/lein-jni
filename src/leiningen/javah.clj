(ns leiningen.javah)



(defn javah
  "call javah to java class"
  [project & args]
  (leiningen.core.main/info "args are: " args)
  (println "Hi!"))
