#!/bin/bash

cd `dirname $0`
. ./_project.sh

echo "...copying customized RemoteApiGen templates"
cp -R ./freegen/customized/remoteapi/* ./freegen/remoteapi/

if [ -e ../build/lastadoc/swagger.json ];then
  echo "...copying swagger.json to RemoteApiGen schema"
  cp ../build/lastadoc/swagger.json ../src/main/resources/remoteapi/schema/remoteapi_schema_intro_swagger.json
fi

FIRST_ARG=$1
SECOND_ARG=$2

sh $DBFLUTE_HOME/etc/cmd/_df-manage.sh $MY_PROPERTIES_PATH $FIRST_ARG $SECOND_ARG
taskReturnCode=$?

echo "...formatting generated types immediately"
npx prettier --write "../frontend/src/static/app/api/intro/**/*.ts"

if [ $taskReturnCode -ne 0 ];then
  exit $taskReturnCode;
fi
