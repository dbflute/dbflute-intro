FROM amazoncorretto:8

COPY scripts/entrypoint.sh /scripts/entrypoint.sh
COPY version.txt /scripts/version.txt
COPY dbflute-intro.jar /app/dbflute-intro.jar

WORKDIR /app

EXPOSE 8926
ENTRYPOINT ["sh", "/scripts/entrypoint.sh"]
