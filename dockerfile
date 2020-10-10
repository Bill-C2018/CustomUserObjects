FROM openjdk:8
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

#mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)
#docker build --build-arg DEPENDENCY=build/dependency -t springio/gs-spring-boot-docker .
#docker run -p 8080:8081 -t  

ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.userobjects.app.UserObjects1Application"]
