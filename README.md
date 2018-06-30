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
4. install components of bower, npm for application (at dbflute-intro directory)
 1. `$ npm install`

## How to boot (for committer)
1. `$ ./gradlew run` => boot API Server (same as IntroBoot.main())
2. `$ npm install` => boot FrontEnd Application (Riot)

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
