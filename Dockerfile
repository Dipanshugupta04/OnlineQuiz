FROM openjdk:17-jdk
ADD target/OnlineQuiz.jar OnlineQuiz.jar
ENTRYPOINT ["java","-jar","/OnlineQuiz.jar"]