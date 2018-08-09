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

# For committers or contributers
## How to compile (for committer)

1. git clone https://github.com/dbflute/dbflute-intro.git
2. import as Gradle project on your IDE or `$ ./gradlew eclipse` (if Eclipse)
3. (install node, gulp, bower if non-existent in your computer)
   1. `$ brew install node`
4. install components of bower, npm for application (at dbflute-intro directory)
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
 |-src
 |  |-main
 |  |-static
 |     |-app
 |     |  |-client
 |     |  |  |-client.controller.js
 |     |  |  |-client.html
 |     |  |-common
 |     |  |  |-ApiFactory.js
 |     |  |-main
 |     |  |  |-main.controller.js
 |     |  |  |-main.html
 |     |  |-welcome
 |     |  |  |-welcome.controller.js
 |     |  |  |-welcome.html
 |     |  |-index.css     // core styles
 |     |  +-index.js      // core angular modules
 |     |
 |     |-404.html
 |     +-index.html       // base HTML of all components
```

## Release Engineering

### Github release
1. Increment version at build.gradle
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

### Send release notification 
1. Notify all around the world

## Upgrade DBFlute

1. `sh ./dbflute_introdb/manage.sh upgrade`
2. set `DBFLUTE_HOME ../mydbflute/dbflute-1.x` in changed `_project.sh` and `_project.bat`
3. mv downloaded dbflute to `./mydbflute/dbflute-1.x`
4. copy template files to `./dbflute_introdb`
5. `sh ./dbflute_introdb/manage.sh 1`
