# Api auth-user

## Purpose
    This API is intended to register users and access levels to resources.
    After the user is properly registered, it will be possible to make requests
    to generate "Bearer" access tokens that will be valid for 1 hour (UTC)

## Technologies used
    - Java 17
    - Spring boot
    - Spring security
    - spring profile
    - Mysql
    - Liquibase
    - Jwt
    - Git actions
    - Amazon ECS
    - Amazon Store parameter
    - Junit
    - Mockito
    - Test containers
    - Docker

## Requirements
    - java 17
    - mysql 8.x.x
    - docker(optional)

## How to run this project ?
    The auth-user API is a Java 17 application using Spring Boot.
    To run the project, you need to have Java 17 and MySQL 8 installed locally.
    If you don't have MySQL 8 running locally, you can optionally use a Docker container.
    In the project's root directory, there is a file named "docker-compose.yml".
    To use a container, simply run the command "docker-compose up" in the root directory
    to start a MySQL container before running the project.
    You will also need to set a Spring profile environment variable for the API to run correctly:
    SPRING_PROFILES_ACTIVE=local.

# How to get started ?
    There is already a profile registered that has access to all the API's resources, including the ability
    to create other users. You can retrieve a valid token with these test credentials:
    POST: http://localhost:8080/api/v1/authentication/login
            {
                "email": "fabio_dos_reis@outlook.com",
                "password": "123"
            }

    

