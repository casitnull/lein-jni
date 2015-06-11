# lein-jni

A Leiningen plugin to compile jni code.

## Usage

In your project.clj, you should add below configurations:

```clj
{:jni {:native-source-path "native"
       :native-make-cmd "make"
       :native-target-name "libxxx.so"
       :javah-classes ["a.b.class1" "m.n.class2"]}
 :prep-tasks ["javac" "jni" "compile"]
 :middleware [lein-jni.plugin/middleware]}
```

lein-jni help you do below 3 tasks:
1. **javah**: to call javah to create ".h" files for jni java classes
2. **make**: go into your native code folder and do make
3. **copy**: copy your so file to project native folder

To call lein-jni in shell:

```bash
lein jni
```

or

```bash
lein jni javah
lein jni make
lein jni copy
```

## License

Copyright © 2015 Casit

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
