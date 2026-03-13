#!/bin/bash

cd `dirname $0`

export answer=y

pushd ../dbflute_resortlinedb
sh manage.sh replace-schema
popd

sh nextdb-renewal.sh
sh manage.sh replace-schema
sh slave-replace-schema.sh
sh syncdb-replace-schema.sh
