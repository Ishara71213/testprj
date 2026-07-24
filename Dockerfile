FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests 

#Skip the test build and run test both
#RUN mvn clean package -Dmaven.test.skip=true   


FROM eclipse-temurin:17.0.19_10-jre-ubi10-minimal

WORKDIR /app

# ENV DB_URL=jdbc:mysql://mysql-db:3345/testprj_db?createDatabaseIfNotExist=true
# ENV DB_USERNAME=root
# ENV DB_PASSWORD=1234

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]