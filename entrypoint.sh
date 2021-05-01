#!/bin/sh

# /appをホストにマウントする場合、jarファイルが消えてしまうのでコピーしておく (クライアント未作成時の起動などで利用される想定)
if [ ! -e /app/dbflute-intro.jar ]; then
  cp /dbflute-intro.jar /app/dbflute-intro.jar
fi

echo "launch dbflute-intro.jar..."
java -jar -Dintro.host=0.0.0.0 dbflute-intro.jar
