FROM amazoncorretto:8

WORKDIR /app

COPY build/libs/dbflute-intro.jar /app
COPY dist/cacheKey.json /app/dist/cacheKey.json
COPY dist/manifest.json /app/dist/manifest.json

EXPOSE 8926
CMD ["java", "-jar", "-Dintro.host=0.0.0.0", "dbflute-intro.jar"]
