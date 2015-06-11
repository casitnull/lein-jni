(ns lein-jni.plugin)

(defn middleware [project]
  (let [np (:native-path project)
        opt (str "-Djava.library.path=" np)
        jvm-op (:jvm-opts project)]
    (if ((set jvm-op) opt)
      project
      (assoc project :jvm-opts (conj jvm-op opt)))))
