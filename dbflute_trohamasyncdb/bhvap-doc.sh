#!/bin/bash

cd `dirname $0`
. _project.sh

echo "/nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
echo "Specify the environment type to use bhvap."
echo "nnnnnnnnnn/"
export DBFLUTE_ENVIRONMENT_TYPE=bhvap

echo "/nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
echo "Specify the file path to be used as build-properties."
echo "nnnnnnnnnn/"
export MY_PROPERTIES_PATH=build-${MY_PROJECT_NAME}.properties

echo "/nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
echo "Execute {Doc}."
echo "nnnnnnnnnn/"
sh $DBFLUTE_HOME/etc/cmd/_df-doc.sh $MY_PROPERTIES_PATH

echo "/nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
echo "Remove the environment type (closing)."
echo "nnnnnnnnnn/"
unset DBFLUTE_ENVIRONMENT_TYPE

