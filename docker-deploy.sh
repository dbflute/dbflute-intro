#!/bin/sh

TAG=`git tag | tail -1f`
VERSION=`echo ${TAG} | sed s/dbflute-intro-//g`

echo ${TAG}
echo ${VERSION}

## git checkout ${TAG}
docker build . -t ${TAG}
## docker push dbflute/dbflute-intro:${TAG}
