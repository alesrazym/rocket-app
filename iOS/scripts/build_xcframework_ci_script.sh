#!/bin/bash

build_debug() {
  # Without dev null input, the script will be stuck forever when it started from fastlane
  ./gradlew shared:rocket:syncIosLibDebug < /dev/null
}

build_release() {
  # Without dev null input, the script will be stuck forever when it started from fastlane
  ./gradlew shared:rocket:syncIosLibRelease < /dev/null
}

cd ..
  chmod +x gradlew # Somehow it denied permisions and this needs to be enabled
if [ "$1" == "DEBUG" ]; then
  build_debug
elif [ "$1" == "RELEASE" ]; then
  build_release
else
  echo "Unknown build variant. Supported are [DEBUG, RELEASE]"
  exit 1
fi
if [ "$?" != 0 ]; then
  exit 1
fi
cd ios
