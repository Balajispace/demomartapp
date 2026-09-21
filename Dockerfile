# Stage 1: Build WAR with Maven
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run application using Tomcat 9 (supports javax.servlet 4.0)
FROM tomcat:9.0-jre17-temurin
WORKDIR /usr/local/tomcat

# Remove default webapps
RUN rm -rf webapps/*

# Copy compiled WAR file as ROOT.war to serve on root path /
COPY --from=builder /app/target/balajimart.war webapps/ROOT.war

# Expose default Tomcat port
EXPOSE 8080

CMD ["catalina.sh", "run"]
