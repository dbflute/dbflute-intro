FROM amazoncorretto:8

COPY scripts/entrypoint.sh /scripts/entrypoint.sh
COPY dbflute-intro.jar /jar/dbflute-intro.jar

WORKDIR /app

EXPOSE 8926
ENTRYPOINT ["sh", "/scripts/entrypoint.sh"]
