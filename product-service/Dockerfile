FROM openjdk:13-jdk-slim
MAINTAINER xuwuruoshui <xuwuruoshui@gmail.com>
RUN mkdir /opt/app
COPY target/product-service.jar /opt/app/product-service.jar
ENTRYPOINT ["java", "-jar", "/opt/app/product-service.jar"]