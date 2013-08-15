(ns hauru.core
  (:import [org.apache.shiro SecurityUtils]
           [org.apache.shiro.authc UsernamePasswordToken]
           [org.apache.shiro.authz AuthorizationException Permission]
           [org.apache.shiro.authz.permission RolePermissionResolver WildcardPermission WildcardPermissionResolver]
           [org.apache.shiro.mgt DefaultSecurityManager]
           [org.apache.shiro.realm SimpleAccountRealm]))

(def accounts    [{:username "lonestarr"
                   :password "vespa"
                   :roles    ["goodguy" "schwartz"]}
                  {:username "darkhelmet"
                   :password "ludicrousspeed"
                   :roles    ["darklord" "schwartz"]}
                  {:username "presidentskroob"
                   :password "12345"
                   :roles    ["president"]}])

(def roles       ["goodguy" "darklord" "schwartz" "president"])

(def permissions {"admin"    "*"
                  "schwartz" "lightsaber:*"
                  "goodguy"  "winnebago:drive:eagle5"
                  "darklord" "spaceball-one:command"})

(def realm
  (let [realm    (SimpleAccountRealm. "simple-realm")
        resolver (proxy [RolePermissionResolver] []
                   (resolvePermissionsInRole [roleString]
                     [(WildcardPermission. (permissions roleString))]))]
    (doseq [account accounts]
      (.addAccount realm (:username account) (:password account) (into-array (:roles account))))
    (doseq [role roles]
      (.addRole realm role))
    (.setRolePermissionResolver realm resolver)
    realm))

(defn -main
  [& args]
  ;; Initialize Shiro security manager
  (SecurityUtils/setSecurityManager (DefaultSecurityManager. realm))

  ;; Authentication examples
  (let [subject (SecurityUtils/getSubject)
        token   (UsernamePasswordToken. "lonestarr" "vespa")]
    (.login subject token)
    (println (.getPrincipal subject))
    (println (map (juxt identity #(.hasRole subject %))
                  ["goodguy" "darklord" "schwartz" "president"])))

  ;; Authorization examples
  (println (map (fn [[username password]]
                  (let [subject (SecurityUtils/getSubject)
                        token   (UsernamePasswordToken. username password)]
                    (.login subject token)
                    (map (juxt identity #(.isPermitted subject %)) ["*" "lightsaber:*" "winnebago:drive:eagle5"])))
                [["lonestarr"  "vespa"]
                 ["darkhelmet" "ludicrousspeed"]])))
