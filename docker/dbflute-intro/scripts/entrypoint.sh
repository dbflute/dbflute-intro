#!/bin/sh

VERSION=$(cat /scripts/version.txt)

if [ ! -e dbflute-intro.jar ]; then
  echo "dbflute-intro.jar is not exists. start download dbflute-intro-${VERSION}.jar..."
  curl -fL -o dbflute-intro.jar https://github.com/dbflute/dbflute-intro/releases/download/dbflute-intro-${VERSION}/dbflute-intro.jar
fi

echo "launch dbflute-intro.jar..."
java -jar -Dintro.host=0.0.0.0 dbflute-intro.jar
