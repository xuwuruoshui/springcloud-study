FROM openjdk:14-jdk-slim
MAINTAINER xuwuruoshui <xuwuruoshui@gmail.com>
RUN mkdir /opt/app
COPY target/eureka-server.jar /opt/app/eureka-server.jar
ENTRYPOINT ["java", "-jar", "/opt/app/eureka-server.jar"]