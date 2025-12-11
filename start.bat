@echo off
REM Script for å starte Spring Boot applikasjonen
REM Setter JAVA_HOME automatisk

set JAVA_HOME=C:\Program Files\Java\jdk-21

echo Starting Kleda Dashboard Backend...
echo Using JAVA_HOME: %JAVA_HOME%
echo.

mvnw.cmd spring-boot:run

