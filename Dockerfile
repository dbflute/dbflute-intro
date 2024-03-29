FROM openjdk:17.0.1-jdk-oraclelinux7

COPY entrypoint.sh /entrypoint.sh
COPY build/libs/dbflute-intro.jar /dbflute-intro.jar

WORKDIR /app

EXPOSE 8926
ENTRYPOINT ["sh", "/entrypoint.sh"]
