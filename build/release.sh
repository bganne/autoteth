#!/bin/sh
TOPDIR="$(dirname "$(readlink -f "$0")")/.."
RXPATH="$HOME/src/android/redex/install/bin"
APK="$TOPDIR/target/release/AutoTETH-unaligned.apk"
cd "$TOPDIR"
pwd
set -x
lein with-profile lean do droid build, droid crunch-resources, droid package-resources, droid create-apk
PATH="$PATH:$RXPATH" redex -o "$APK" "$APK"
lein with-profile lean do droid sign-apk, droid zipalign-apk
