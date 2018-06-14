DBFlute Intro
=======================
The GUI Application for DBFlute Management

# For Intro users
## How to use Intro
1. download jar file
2. `$ java -jar dbflute-intro.jar`

http://dbflute.seasar.org/ja/manual/function/generator/intro/index.html

# For committers or contributers
## How to compile (for committer)

1. git clone https://github.com/dbflute/dbflute-intro.git
2. import as Gradle project on your IDE or `$ ./gradlew eclipse` (if Eclipse)
3. (install node, gulp, bower if non-existent in your computer)
 1. `$ brew install node`
 2. `$ npm install -g gulp`
 3. `$ npm install -g bower`
4. install components of bower, npm for application (at dbflute-intro directory)
 1. `$ bower install`
 2. `$ npm install`

## How to boot (for committer)
1. `$ ./gradlew run` => boot API Server (same as IntroBoot.main())
2. `$ ./gradlew gulp_serve` => boot FrontEnd Application (AngularJS)

## How to refresh application
1. $ `./gradlew refresh`

## How to build
1. `$ ./gradlew build`
2. `$ java -jar build/libs/dbflute-intro.jar`

## Server-side Framework

using LastaFlute:
https://github.com/lastaflute/lastaflute

## Front-side Framework

using Angular:
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
 |     |  |-bootstrap.css
 |     |  |-index.css     // core styles
 |     |  +-index.js      // core angular modules
 |     |
 |     |-404.html
 |     +-index.html       // base HTML of all components
```

## Release Engineering

### Github release
- Increment version at build.gradle
- Add tag on release commit
  - Old tag name : https://github.com/dbflute/dbflute-intro/releases
- Edit tag and upload "intro.jar"
- Publish tag to release

### 88 command release
- Access at https://github.com/dbflute/dbflute.github.io/blob/master/meta/public.properties
- Edit public.properties
```
# latest version of DBFlute Intro, can be used as comparing version
intro.latest.version = ????? # Edit this version

# download URL of DBFlute Intro 
intro.download.url = https://github.com/dbflute/dbflute-intro/releases/download/dbflute-intro-$$version$$/dbflute-intro.jar
```

### Send Release notification 
Notify all around the world
