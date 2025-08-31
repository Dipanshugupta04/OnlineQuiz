FROM openjdk:17-jdk
ADD target/demo.jar demo.jar
ENTRYPOINT ["java","-jar","/demo.jar"]