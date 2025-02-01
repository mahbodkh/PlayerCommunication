#!/bin/bash
# Compile the project
mvn clean compile

# Run the program
mvn exec:java -Dexec.mainClass="com.example.app.Main"