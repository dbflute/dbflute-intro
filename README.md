DBFlute Intro
=======================
The GUI Application for DBFlute Management

# For Intro users
## How to use Intro
1. download jar file
2. execute this command 
   1. `$ java -jar dbflute-intro.jar`
   2. `$ java -jar -Dintro.decomment.server=true dbflute-intro.jar  # if you use decomment server mode`

http://dbflute.seasar.org/ja/manual/function/generator/intro/index.html

# For committers or contributors
## How to compile (for committer)

1. git clone https://github.com/dbflute/dbflute-intro.git
2. import as Gradle project on your IDE or `$ ./gradlew eclipse` (if Eclipse)
3. install node v8.x.x (if non-existent in your computer)
   1. `$ brew install node@8`  
   
   or, it might be better to use nodebrew.
4. install components of npm for application (at dbflute-intro directory)
   1. `$ npm install`

## How to boot (for committer)
1. `$ ./gradlew run` => boot API Server (same as IntroBoot.main())
2. `$ npm start` => boot FrontEnd Application (Riot)

## How to refresh application
1. $ `./gradlew refresh`

## How to build
1. `$ ./gradlew build`
2. `$ java -jar build/libs/dbflute-intro.jar`

## Server-side Framework

using LastaFlute:
https://github.com/lastaflute/lastaflute

## Front-side Framework

using Riot:
```
dbflute-intro
└--src
   ├--main
   └--static
      └-- app
          ├-- common
          │   ├-- FFetchWrapper.js
          │   ├-- UiAssist.js
          │   ├-- directive
          │   │   └-- FileDirective.js
          │   ├-- factory
          │   │   └-- ApiFactory.js
          │   ├-- i18n.tag
          │   ├-- navbar.tag
          │   └-- result-view.tag
          ├-- error
          │   └-- 404.tag
          ├-- main
          │   └-- main.tag
          ├-- client
          │   ├-- client.tag
          │   └-- create.tag
          ├-- settings
          │   └-- settings.tag
          ├-- welcome
          │   └-- welocome.tag
          ├-- index.css
          └-- index.js
```

## Release Engineering

### Github release
1. Increment version at build.gradle and package.json
2. Add tag on release commit
   1. Old tag name : https://github.com/dbflute/dbflute-intro/releases
3. Edit tag and upload "intro.jar"
4. Publish tag to release

### 88 command release
1. Access at https://github.com/dbflute/dbflute.github.io/blob/master/meta/public.properties
2. Edit public.properties
```
# latest version of DBFlute Intro, can be used as comparing version
intro.latest.version = ????? # Edit this version

# download URL of DBFlute Intro 
intro.download.url = https://github.com/dbflute/dbflute-intro/releases/download/dbflute-intro-$$version$$/dbflute-intro.jar
```

### Docker image release
1. Execute this command
```
$ cd path/to/dbflute-intro
$ sh docker-build.sh && sh docker-push.sh ${release version} # input release version as argument
```
2. Update image version [here](https://hub.docker.com/repository/docker/dbflute/dbflute-intro).

### Document
1. Write release information at [here](https://github.com/dbflute/dbflute-document/blob/f1e0aab6d2dbc882e2aa5e4e4143637a2b87b4d0/web/ja/manual/function/generator/intro/index.html#L49)
2. Deploy the dbflute document

### Send release notification 
1. Add Document at [here](https://github.com/dbflute/dbflute-document/blob/master/web/ja/manual/function/generator/intro/index.html#L61)
2. Notify all around the world

## Upgrade DBFlute

1. `sh ./dbflute_introdb/manage.sh upgrade`
2. `set DBFLUTE_HOME=../mydbflute/dbflute-${latest}` in changed `./dbflute_trohamadb/_project.sh` and `./dbflute_trohamadb/_project.bat`
3. copy template files to `./dbflute_introdb`
4. `sh ./dbflute_introdb/manage.sh 1`
