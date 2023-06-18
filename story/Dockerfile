FROM openjdk:20
ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} story.jar
ENTRYPOINT ["java", "-jar", "/story.jar"]