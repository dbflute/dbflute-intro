FROM amazoncorretto:8

COPY scripts/startup.sh /scripts/startup.sh
COPY version.txt /scripts/version.txt
COPY dbflute-intro.jar /app/dbflute-intro.jar

# TODO hakiba: make sure need it.
#COPY dist/cacheKey.json /app/dist/cacheKey.json
#COPY dist/manifest.json /app/dist/manifest.json

EXPOSE 8926
CMD ["sh", "scripts/startup.sh"]
