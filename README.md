# autoteth

This is a [Clojure/Android](http://clojure-android.info/) application.
It has two features:
 - automatically turns Bluetooth tethering on when Bluetooth becomes available (eg. after you reboot your phone or leave airplane mode)
 - toggle Bluetooth tethering on/off from the main acitivity, so you can add a shortcut to your home screen instead of having to go through several layers or configuration menus

## Install

You can download the APK [here](https://github.com/bganne/autoteth-release/raw/master/autoteth.apk).

## Build

Of course, you will need the [Android SDK](http://developer.android.com/sdk/installing/index.html).
It makes use of [lein-droid](https://github.com/clojure-android/lein-droid/).

When everything is setup, just run this command in the autoteth containing directory:
`~# lein with-profile lean do clean, droid doall`

## License

Copyright © 2016 Benoît Ganne

Distributed under the WTFPL v2.
