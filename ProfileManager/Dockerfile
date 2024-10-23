FROM openjdk:21
COPY ./target/Recorder-0.0.1-SNAPSHOT.jar /usr/src/ProfileManager/
WORKDIR /usr/src/ProfileManager/
EXPOSE 8080
CMD ["java", "-jar", "Recorder-0.0.1-SNAPSHOT.jar"]