FROM openjdk:17

WORKDIR /app

COPY target/auth-user-*-RELEASE.jar /app/auth-user.jar

EXPOSE 8080

CMD ["java", "-jar", "auth-user.jar"]

