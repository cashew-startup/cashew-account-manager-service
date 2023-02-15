FROM openjdk:17

WORKDIR /account-manager

ADD target/account-manager-1.0.jar account-manager-1.0.jar

EXPOSE 8080

VOLUME ["/account-manager/access-refresh-token-keys"]

ENTRYPOINT ["java", "-jar", "account-manager-1.0.jar"]
