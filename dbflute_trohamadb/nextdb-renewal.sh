#!/bin/bash

cd `dirname $0`
. _project.sh

echo "/nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
echo "Specify the environment type to use nextdb."
echo "nnnnnnnnnn/"
export DBFLUTE_ENVIRONMENT_TYPE=nextdb

echo "/nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
echo "...Calling the ReplaceSchema task"
echo "nnnnnnnnnn/"
sh $DBFLUTE_HOME/etc/cmd/_df-replace-schema.sh $MY_PROPERTIES_PATH
taskReturnCode=$?

echo "/nnnnnnnnnnnnnnnnnnnnnnn"
echo "...Calling the JDBC task"
echo "nnnnnnnnnn/"
sh $DBFLUTE_HOME/etc/cmd/_df-jdbc.sh $MY_PROPERTIES_PATH
taskReturnCode=$?

echo "/nnnnnnnnnnnnnnnnnnnnnnnnnnn"
echo "...Calling the Document task"
echo "nnnnnnnnnn/"
sh $DBFLUTE_HOME/etc/cmd/_df-doc.sh $MY_PROPERTIES_PATH
taskReturnCode=$?

if [ $taskReturnCode -ne 0 ];then
  exit $taskReturnCode;
fi
