(ns fr.benou.autoteth.wifi
  (:require [neko.log :as log]
            [neko.notify :refer [toast]])
  (:import [android.net.wifi
            WifiManager
            WifiConfiguration])
  (:gen-class
    :main false
    :extends android.app.Activity
    :exposes-methods {onCreate onCreateSuper})) 

(defn set-tethering
  [context
   ^android.net.wifi.WifiManager wifi
   state]
  "Set Wifi tethering state and display status message"
  (let [^android.net.wifi.WifiConfiguration conf nil]
    (.setWifiApEnabled
      wifi
      conf
      state))
  (toast
    context
    (str
      "Wifi tethering "
      (if state "enabled" "disabled"))
    :long))

(defn toggle-tethering [^android.content.Context context]
  "Toggle Wifi tethering state"
  (let [w-s (android.content.Context/WIFI_SERVICE)
        ^android.net.wifi.WifiManager wifi  (.getSystemService context w-s)
        state (.isWifiApEnabled wifi)]
    (set-tethering context wifi (not state))))


(defn -onCreate [^fr.benou.autoteth.wifi this bundle]
  "Main activity. Toggle Wifi tethering status."
  (log/d "-onCreate:" this bundle)
  (.onCreateSuper this bundle)
  (toggle-tethering this)
  (.finish this))
