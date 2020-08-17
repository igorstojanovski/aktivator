FROM adoptopenjdk/openjdk11:jdk-11.0.8_10
VOLUME /tmp
COPY build/libs/*.jar app.jar
ENV environment dev
ENTRYPOINT ["java","-jar","./app.jar", "--spring.profiles.active=${environment}"]