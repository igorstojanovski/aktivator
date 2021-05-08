FROM adoptopenjdk/openjdk11:jdk-11.0.8_10
RUN addgroup --system spring && useradd -r spring -g spring
USER spring:spring
VOLUME /tmp
COPY ./libs/*.jar app.jar
ENV environment dev
ENTRYPOINT ["java","-jar","./app.jar", "--spring.profiles.active=${environment}"]