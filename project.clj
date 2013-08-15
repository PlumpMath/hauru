(defproject hauru "0.1.0-SNAPSHOT"
  :description "A Clojure wrapper library for Apache Shiro"
  :url "http://github.com/zerokarmaleft/hauru"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure        "1.5.1"]
                 [org.apache.shiro/shiro-all "1.2.2"]
                 [org.slf4j/slf4j-nop        "1.7.5"]]
  :main hauru.core)
