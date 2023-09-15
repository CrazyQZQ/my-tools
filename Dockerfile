FROM hub.c.163.com/library/java:latest
VOLUME /tmp
ADD target/my-tools-0.0.1-SNAPSHOT.jar my-tools.jar
ENTRYPOINT ["java","-jar","/my-tools.jar"]