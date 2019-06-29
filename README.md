[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=aktivator-io_aktivator-api&metric=ncloc)](https://sonarcloud.io/dashboard?id=aktivator-io_aktivator-api)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=aktivator-io_aktivator-api&metric=alert_status)](https://sonarcloud.io/dashboard?id=aktivator-io_aktivator-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=aktivator-io_aktivator-api&metric=coverage)](https://sonarcloud.io/dashboard?id=aktivator-io_aktivator-api)

[![Build Status](https://travis-ci.org/aktivator-io/aktivator-api.svg?branch=master)](https://travis-ci.org/aktivator-io/aktivator-api)

# Authentication

The API uses a Keycloak instance as an authentication server.  
Kecloak can be accessed here: http://35.157.118.243:8080/auth

Clients need to be registered in order to get client ID and secret.  
The access token url is: http://35.157.118.243:8080/auth/realms/aktivator-test/protocol/openid-connect/token