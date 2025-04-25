FROM amazoncorretto:21
EXPOSE 8082
COPY target/medical-clinic-proxy-0.0.1-SNAPSHOT.jar app/medical-clinic-proxy-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "app/medical-clinic-proxy-0.0.1-SNAPSHOT.jar"]