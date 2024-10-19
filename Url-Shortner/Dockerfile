FROM openjdk:19-jdk
COPY target/urlshortener-docker.jar .
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "urlshortener-docker.jar" ]	