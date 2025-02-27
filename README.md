# automated-curd-generator
Overview
The CRUD Generator is a Spring Boot command-line tool that generates boilerplate code for a RESTful CRUD application using Mustache templates.

Features
Generates entity, repository, service, service implementation, and controller classes.
Uses Mustache templates for easy customization.
Supports defining entity fields dynamically.

Prerequisites
Java 21+
Maven
Spring Boot 3.3.4+

Setup & Installation
Clone the repository:
  git clone https://github.com/yourusername/crud-generator.git
  cd crud-generator
Build the project:
  mvn clean package

Usage:
Run the JAR file with the required parameters:
    java -jar target/crud-generator.jar <EntityName> <PackageName> "<field1>:<type1>,<field2>:<type2>"

Example
    java -jar target/crud-generator.jar Product com.example "name:String,price:Double"

This will generate the following files:
    src/main/java/com/example/model/Product.java
    src/main/java/com/example/repository/ProductRepository.java
    src/main/java/com/example/service/ProductService.java
    src/main/java/com/example/service/impl/ProductServiceImpl.java
    src/main/java/com/example/controller/ProductController.java

Configuration
