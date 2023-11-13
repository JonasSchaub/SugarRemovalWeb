#FROM openjdk:8u171-slim
FROM eclipse-temurin:17-jre-ubi9-minimal
EXPOSE 8092
VOLUME /tmp

COPY target/sugarremovalweb-1.1.0.0.jar app.jar
#COPY inchiPet.jar /

#RUN java -jar inchiPet.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
