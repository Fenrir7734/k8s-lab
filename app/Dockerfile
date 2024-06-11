FROM maven:3.8-openjdk-18
WORKDIR /backend
COPY . .
RUN mvn clean install -DskipTests
CMD mvn spring-boot:run -Dspring-boot.run.profiles=docker