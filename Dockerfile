# Build image
FROM openjdk:23 as build

WORKDIR /app

# Copy project files
COPY pom.xml mvnw ./
COPY hillel-expense-tracker-common /app/hillel-expense-tracker-common
COPY hillel-expense-tracker-client /app/hillel-expense-tracker-client
COPY hillel-expense-tracker-dao /app/hillel-expense-tracker-dao
COPY hillel-expense-tracker-repo /app/hillel-expense-tracker-repo
COPY hillel-expense-tracker-service /app/hillel-expense-tracker-service
COPY hillel-expense-tracker-web /app/hillel-expense-tracker-web
COPY hillel-expense-tracker-web-boot /app/hillel-expense-tracker-web-boot

# Copy and prepare Maven wrapper
COPY .mvn /app/.mvn
RUN chmod +x mvnw

# Build the project and skip tests
RUN ./mvnw clean package -DskipTests



# Base image # JRE
FROM openjdk:23-slim

# Multistage
COPY --from=build /app/hillel-expense-tracker-web-boot/target/hillel-expense-tracker-web-boot-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar

#COPY hillel-expense-tracker-web-boot/target/hillel-expense-tracker-web-boot-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]