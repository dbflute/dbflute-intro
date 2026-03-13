#!/bin/bash

cd `dirname $0`
. _project.sh

export DBFLUTE_ENVIRONMENT_TYPE=syncdb

echo "/nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
echo "...Calling the ReplaceSchema task"
echo "nnnnnnnnnn/"
sh $DBFLUTE_HOME/etc/cmd/_df-replace-schema.sh $MY_PROPERTIES_PATH
taskReturnCode=$?

echo "/nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
echo "Remove the environment type (closing)."
echo "nnnnnnnnnn/"
unset DBFLUTE_ENVIRONMENT_TYPE

if [ $taskReturnCode -ne 0 ];then
  exit $taskReturnCode;
fi
