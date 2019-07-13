[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=aktivator-io_aktivator-api&metric=ncloc)](https://sonarcloud.io/dashboard?id=aktivator-io_aktivator-api)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=aktivator-io_aktivator-api&metric=alert_status)](https://sonarcloud.io/dashboard?id=aktivator-io_aktivator-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=aktivator-io_aktivator-api&metric=coverage)](https://sonarcloud.io/dashboard?id=aktivator-io_aktivator-api)

[![Build Status](https://travis-ci.org/aktivator-io/aktivator-api.svg?branch=master)](https://travis-ci.org/aktivator-io/aktivator-api)

# Authentication

The API uses a Keycloak instance as an authentication server.  
Kecloak can be accessed here: http://35.157.118.243:8080/auth

Clients need to be registered in order to get client ID and secret.  
The access token url is: http://35.157.118.243:8080/auth/realms/aktivator-test/protocol/openid-connect/token

To register a user in the aktivator-test realm: http://35.157.118.243:8080/auth/realms/aktivator-test/account

# Development 

For the code to run you need to set this environment variable:

* AKT_DEV_CLIENT_SECRET - The API is registered as a client in the Keycloak. This is its secret.

In order to run the integration tests you need to set these additional variables:

* AKT_WEB_DEV_CLIENT_SECRET - The ITs simulate a web client. For that purpose a client 
is registered in the Keycloak. This is it's secret.
* AKT_USER - Test user in Keycloak 
* AKT_PASSWORD - Password for the test user.

# Hosting

The latest version of the project is hosted on Beanstalk.
App can be reached at: http://aktivator-api-snapshot.eu-north-1.elasticbeanstalk.com
