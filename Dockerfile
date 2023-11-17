FROM eclipse-temurin:17-jre-ubi9-minimal
EXPOSE 8092
VOLUME /tmp

COPY target/sugarremovalweb-1.1.0.0.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
