#!/bin/bash

cd `dirname $0`
. ./_project.sh

FIRST_ARG=$1
SECOND_ARG=$2

echo "...copying your templates"
cp -R ./templates/doc/html/* $DBFLUTE_HOME/templates/doc/html
