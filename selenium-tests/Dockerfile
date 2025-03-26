FROM maven:3.9-eclipse-temurin-11

WORKDIR /selenium-tests

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .

CMD ["mvn", "test"]

