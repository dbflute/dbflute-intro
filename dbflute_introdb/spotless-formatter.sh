#!/bin/bash

cd `dirname $0`
cd ..

JAVA_VERSION=11+
export JAVA_HOME=$(/usr/libexec/java_home -v ${JAVA_VERSION})

# auto-setting for JAVA_HOME (keeping exiting JAVA_HOME)
if [[ -z "${JAVA_HOME}" ]]; then
  if [[ `uname` = "Darwin" ]]; then
    export JAVA_HOME=$(/usr/libexec/java_home -v ${JAVA_VERSION})
  fi
fi

./gradlew spotlessApply
