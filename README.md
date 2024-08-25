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
    A api auth-user se trata de uma api java 17 com spring boot, para rodar o projeto
    é necessário ter o java 17 instalado e o mysql 8 localmente.
    Caso não tenha um serviço do mysql 8 localmente, pode ser usado opcionalmente um
    container Docker, na raíz do projeto existe um arquivo de nome "docker-compose.yml"
    caso queira usar um container basta rodar o comando "docker-compose up" na raíz do
    projeto para subir um container com o mysql antes de rodar o projeto.
    Também será necessário setar uma váriavel de ambiente do spring profile para que
    a Api suba corretamente: SPRING_PROFILES_ACTIVE=local.

# How to get started ?
    Já existe um perfil cadastrado que tem acesso a todos os recursos da api, inclusive
    criar outros usuários.
    É possível recuperar um token valido com essas credenciais de teste:
    POST: http://localhost:8080/api/v1/authentication/login
            {
                "email": "fabio_dos_reis@outlook.com",
                "password": "123"
            }

    

