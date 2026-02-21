# Step 1: Build stage
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
# כאן התיקון - אנחנו מחפשים כל קובץ jar שנוצר בתיקיית target
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]