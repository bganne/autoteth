# AutoTETH

Easy tethering management for Android:
 - automatically turns Bluetooth tethering on when Bluetooth becomes available (eg. after you reboot your phone or leave airplane mode)
 - activities to toggle Bluetooth, USB and Wifi (hotspot) tethering on/off, so you can add shortcuts to your home screen instead of having to go through several layers or configuration menus

This is a [Clojure/Android](http://clojure-android.info/) application.

## Install

You can download the APK [here](https://github.com/bganne/bin/raw/master/release/AutoTETH/AutoTETH.apk).

## Dependencies

 -  [Android SDK](http://developer.android.com/sdk/installing/index.html).
 -  [lein-droid](https://github.com/clojure-android/lein-droid/).

## Build

When everything is setup, run this command from the top directory:
`~# lein with-profile lean do droid clean, droid doall`

## License

Copyright © 2016 Benoît Ganne

Distributed under the WTFPL v2.
