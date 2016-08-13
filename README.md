#DBFlute Intro

## developer

```
# git clone

# refresh app
cd dbflute-intro
./gradlew refresh

# during development
cd dbflute-intro
## Api (run main class(org.dbflute.intro.IntroBoot) in the IDE. or execute commond)
./gradlew run
## Front End
./gradlew gulp_serve

# build + run
cd dbflute-intro
./gradlew build
java -jar build/libs/dbflute-intro.war
```

## URL list

```
# dbflute intro
api/intro/manifest

# dbflute engine
api/engine/publicProperties
api/engine/versions
api/engine/download/{version}

# dbflute client
api/client/list
api/client/detail/{project}
api/client/add
api/client/remove/{project}
api/client/update
api/client/task/{project}/{task}
api/client/schemahtml
api/client/historyhtml
```

end.
