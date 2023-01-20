FROM openjdk:17

ADD target/account-manager-1.0.jar account-manager-1.0.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "account-manager-1.0.jar"]
