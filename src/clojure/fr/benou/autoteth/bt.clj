(ns fr.benou.autoteth.bt
  (:require [neko.log :as log]
            [neko.notify :refer [toast]])
  (:import [android.bluetooth
            BluetoothAdapter
            BluetoothProfile
            BluetoothProfile$ServiceListener]))

(defn pan-run [context callback]
  "Run (callback context BluetoothPan)"
  (let [pan-profile 5
        adapter     (BluetoothAdapter/getDefaultAdapter)]
    (.getProfileProxy
      adapter
      context
      (reify
        BluetoothProfile$ServiceListener
        (onServiceConnected [this profile pan]
          (log/d "onServiceConnected:" this profile pan)
          (callback context pan)
          (.closeProfileProxy adapter profile pan)))
      pan-profile)))

(defn set-tethering-pan [context pan state]
  "Run BluetoothPan.setBluetoothTethering(state)
  and display status message"
  (.setBluetoothTethering pan state)
  (toast
    context
    (str
      "Bluetooth tethering "
      (if state "enabled" "disabled"))
    :long))

(defn set-tethering-dyn [context state-fn]
  "Set Bluetooth tethering with return value of state-fn.
  state-fn is called with BluetoothPan as argument"
  (pan-run
    context
    #(set-tethering-pan %1 %2 (state-fn %2))))

(defn toggle-tethering [context]
  "Toggle Bluetooth tethering state"
  (set-tethering-dyn context #(not (.isTetheringOn %))))

(defn set-tethering [context state]
  "Set Bluetooth tethering state"
  (set-tethering-dyn context (constantly state)))


(gen-class
  :name fr.benou.autoteth.bt.main
  :extends android.app.Activity
  :exposes-methods {onCreate onCreateSuper}
  :prefix main-)

(defn main-onCreate [^fr.benou.autoteth.bt.main this bundle]
  "Main activity. Toggle Bluetooth tethering status."
  (log/d "main-onCreate:" this bundle)
  (.onCreateSuper this bundle)
  (toggle-tethering this)
  (.finish this))


(gen-class
  :name fr.benou.autoteth.bt.svc
  :extends android.app.IntentService
  :init init
  :constructors {[] [String]}
  :prefix svc-)

(defn svc-init []
  [["fr.benou.autoteth.svc"] nil])

(defn svc-onHandleIntent [this intent]
  "Service in charge of turning Bluetooth tethering on.
  Normally called from fr.benou.autoteth.bcast"
  (log/d "svc-onHandleIntent:" this intent)
  (set-tethering this true))


(gen-class
  :name fr.benou.autoteth.bt.bcast
  :extends android.content.BroadcastReceiver
  :prefix bcast-)

(defn bcast-onReceive [this
                       ^android.content.Context context
                       ^android.content.Intent intent]
  "Broadcast receiver triggered on
  android.bluetooth.adapter.action.STATE_CHANGED event."
  (log/d "bcast-onReceive:" this context intent)
  (let [extra-new   "android.bluetooth.adapter.extra.STATE"
        state-on    BluetoothAdapter/STATE_ON
        state-new   (.getIntExtra intent extra-new -1)]
    (when (= state-new state-on)
      (.startService
        context
        (android.content.Intent.
          context
          fr.benou.autoteth.bt.svc)))))
