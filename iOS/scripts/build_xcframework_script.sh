#!/bin/bash

build_debug() {
  # Without dev null input, the script will be stuck forever when it started from fastlane
  ./gradlew shared:rocket:syncIosLibDebug < /dev/null
}

build_release() {
  # Without dev null input, the script will be stuck forever when it started from fastlane
  ./gradlew shared:rocket:syncIosLibRelease < /dev/null
}

echo "Building local shared module"
echo "Moving to $PROJECT_DIR"
cd "$PROJECT_DIR/.."

user=$(whoami)
if [ "$user" == "gitlab-ci" ]; then
  echo "No script here. Pre action script is moved to .gitlab-ci.yaml file."
else
  echo "Setting up variable for local build"
  export JAVA_HOME=/Applications/Android\ Studio.app/Contents/jbr/Contents/Home
  chmod +x gradlew # Somehow it denied permisions and this needs to be enabled
  if [ "$1" == "DEBUG" ]; then
    build_debug
  elif [ "$1" == "RELEASE" ]; then
    build_release
  else
    echo "Unknown build variant. Supported are [DEBUG, RELEASE]"
    exit 1
  fi
fi
