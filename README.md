DBFlute Intro
=======================
The GUI Application for DBFlute Management

# For Intro users
## How to use Intro
1. download jar file
2. `$ java -jar dbflute-intro.war`

TODO jflute how to distribution?

# For committers or contributers
## How to compile (for committer)

1. git clone https://github.com/dbflute/dbflute-intro.git
2. import on IDE: import as Gradle project on your IDE or `$ ./gradlew eclipse` (if Eclipse)
3. install bower, npm
 1. `$ bower install`
 2. `$ npm install`

TODO jflute only bower, npm OK?

## How to boot at local (for committer)
1. `$ ./gradlew run` => boot API Server (same as IntroBoot.main())
2. `$ ./gradlew gulp_serve` => boot FrontEnd Application (AngularJS)

## How to refresh application
1. $ `./gradlew refresh`

TODO jflute what is this?

## How to Build
1. `$ ./gradlew build`
2. `$ java -jar build/libs/dbflute-intro.war`

