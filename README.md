# Java Migration Tool

[![Build Status](https://travis-ci.org/YashchenkoN/java-migration-toole.svg?branch=master)](https://travis-ci.org/YashchenkoN/java-migration-tool)

Simple tool allowing managing DB schema / static (reference) data via Java Code.

## Motivation
There are plenty of DB nowadays (SQL / NoSQL) but not all of them have Java libraries to manage DB schema / static data, especially if it comes to NoSQL DBs.
Popular solutions like Flyway / Liquibase don't support NoSQL DBs.
This project is aimed to develop generic tool that might be easily integrated with any type of DB.

## Library
Library has been published to Maven Repository
https://mvnrepository.com/artifact/io.github.yashchenkon/java-migration-tool-spring-boot-starter

## How it works
Basically the tool is divided into several modules:
- `java-migration-tool` module contains basic interfaces and logic of the tool
- `java-migration-tool-spring-boot-starter ` module contains auto configuration for Spring Boot framework
Other modules are related to concrete integrations with different storages.

## Usage
If you have standalone Spring Boot application it's enough to just add `java-migration-tool-spring-boot-starter` to your classpath (lib is already published to Maven repository) and also you need to add a module of concrete integration in order to make it working properly.

As of now, only Datastore integration exists. If you want to use it, just add https://mvnrepository.com/artifact/io.github.yashchenkon/java-migration-tool-datastore to your classpath.

## How to implement new integration
In order to implement an integration with any DB you have to implement 2 interfaces:
1. `MigrationLock`
2. `MigrationRepository`
As a reference and example you can use Datastore integration https://github.com/YashchenkoN/java-migration-tool/tree/master/java-migration-tool-datastore/src/main/java/io/github/yashchenkon/migration/datastore
