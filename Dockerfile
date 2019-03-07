FROM amazoncorretto:8

WORKDIR /app

COPY build/libs/dbflute-intro.jar /app

EXPOSE 8926
CMD ["java", "-jar", "-Dintro.host=0.0.0.0", "dbflute-intro.jar"]
