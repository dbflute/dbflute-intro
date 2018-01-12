#!/bin/bash

cd `dirname $0`
. ./_project.sh

FIRST_ARG=$1
SECOND_ARG=$2

echo "...copying your templates"
cp -R ./templates/doc/html/* $DBFLUTE_HOME/templates/doc/html

sh $DBFLUTE_HOME/etc/cmd/_df-manage.sh $MY_PROPERTIES_PATH $FIRST_ARG $SECOND_ARG
taskReturnCode=$?

if [ $taskReturnCode -ne 0 ];then
  exit $taskReturnCode;
fi
