(ns fr.benou.autoteth.usb
  (:require [neko.log :as log]
            [neko.notify :refer [toast]])
  (:import [android.net
            ConnectivityManager])
  (:gen-class
    :main false
    :extends android.app.Activity
    :exposes-methods {onCreate onCreateSuper})) 

(defn set-tethering
  [context
   ^android.net.ConnectivityManager cmgr
   state]
  "Set USB tethering state and display status message"
  (.setUsbTethering cmgr state)
  (toast
    context
    (str
      "USB tethering "
      (if state "enabled" "disabled"))
    :long))

(defn is-usb [^String iface regexs]
  (boolean (not-empty (filter #(.matches iface) regexs))))

(defn state-tethering [^android.net.ConnectivityManager cmgr]
  (let [regexs  (.getTetherableUsbRegexs cmgr)
        ifaces  (.getTetherableIfaces cmgr)]
    (boolean (not-empty (filter #(is-usb %1 regexs) ifaces)))))

(defn toggle-tethering [^android.content.Context context]
  "Toggle USB tethering state"
  (let [c-s (android.content.Context/CONNECTIVITY_SERVICE)
        ^android.net.ConnectivityManager cmgr (.getSystemService context c-s)
        state (state-tethering cmgr)]
    (set-tethering context cmgr (not state))))


(defn -onCreate [^fr.benou.autoteth.usb this bundle]
  "Main activity. Toggle USB tethering status."
  (log/d "-onCreate:" this bundle)
  (.onCreateSuper this bundle)
  (toggle-tethering this)
  (.finish this))
