(ns leiningen.jni
  (:require [clojure.java.shell :as sh]
            [leiningen.core.main :as main]))

;; sample profile
{:jni {:native-source-path "native"
       :native-make-cmd "make"
       :native-target-name "libxxx.so"
       :javah-classes ["a.b.class1" "m.n.class2"]}}

(defn- javah
  [project options]
  (sh/with-sh-dir (:compile-path project)
    (doseq [c (:javah-classes options)]
      (let [root (:root project)
            native-src (:native-source-path options)
            args ["javah" "-classpath" "." "-d" native-src c]
            _ (main/info "Javah for class: " c)
            _ (main/info "CMD: " (clojure.string/join " " args))
            r (apply sh/sh args)]
        (if (not  (zero? (:exit r)))
          (main/abort "jni javah failed: " (:err r)))))))

(defn- make
  [project options]
  (sh/with-sh-dir (:native-source-path options)
    (let [make-cmd (:native-make-cmd options)
          _ (main/info "make jni native with cmd: " make-cmd)
          make-cmd (clojure.string/split make-cmd #"\s{1,}")
          r (apply sh/sh make-cmd)]
      (if (not  (zero? (:exit r)))
        (main/abort "jni make failed: " (:err r))))))

(defn- copy
  [project options]
  (if (not (:native-target-name options))
    ;;skip
    nil
    (sh/with-sh-dir (:native-source-path options)
      (let [n-path (:native-path project)
            _ (sh/sh "mkdir" "-p" n-path)
            cmd ["cp" (:native-target-name options) n-path]
            _ (main/info "copy jni binaries to native path, cmd: "
                         (clojure.string/join " " cmd))
            r (apply sh/sh cmd)]
        (if (not  (zero? (:exit r)))
          (main/abort "jni copy failed: " (:err r)))))))

(defn jni
  "I don't do a lot."
  ([project subtask]
   (let [opts (merge {:native-source-path "native"
                      :native-make-cmd "make"}
                     (:jni project))
         opts (assoc opts :native-source-path
                     (str (:root project) "/" (:native-source-path opts)))]
     (case subtask
       "javah" (javah project opts)
       "make" (make project opts)
       "copy" (copy project opts)
       :default (main/abort "wrong subtask name: " subtask))))
  ([project]
   (jni project "javah")
   (jni project "make")
   (jni project "copy")))
