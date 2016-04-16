#!/bin/bash

cd `dirname $0`
. ./_project.sh

pushd ../../dbflute-intro/
sh ./gradlew cleanTest test --tests *LastaDocTest.test_document
popd

sh manage.sh 12
