FROM maven:3.9.5 AS build
WORKDIR /app
ARG APPLICATION_PORT
COPY pom.xml /app
RUN mvn dependency:resolve
COPY . /app
RUN mvn clean
RUN mvn package -DskipTests -X

FROM openjdk:21
COPY --from=build /app/target/*.jar app.jar
EXPOSE ${APPLICATION_PORT}
CMD ["java", "-jar", "app.jar"]