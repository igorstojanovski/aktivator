[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=aktivator-io_aktivator-api&metric=ncloc)](https://sonarcloud.io/dashboard?id=aktivator-io_aktivator-api)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=aktivator-io_aktivator-api&metric=alert_status)](https://sonarcloud.io/dashboard?id=aktivator-io_aktivator-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=aktivator-io_aktivator-api&metric=coverage)](https://sonarcloud.io/dashboard?id=aktivator-io_aktivator-api)

[![Build Status](https://travis-ci.org/aktivator-io/aktivator-api.svg?branch=master)](https://travis-ci.org/aktivator-io/aktivator-api)
# Prerequisites

You need the following environments set in order to be able to run the application:

* AUTH0_CLIENT_ID
* AUTH0_CLIENT_SECRET
* AUTH0_DOMAIN 

# Run the application

`gradle bootRun`

This will start the application and make it accessible at http://localhost:8283
There are two utility endpoints available:

http://localhost:8283/h2-console  
http://localhost:8283/swagger-ui.html

* To get an access token post:

```
 https://igorski.eu.auth0.com/oauth/token   
    
 grant_type: "password"
 client_id: "STJqKwTPtwln51X63uC19dOTYqAzWHbj"
 audience: "https://buy-it-api.igorski.co"
 username: "igorce@outlook.com"
 password: 
```
This will return a response with access_token in the body. For example:

```
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6",
    "expires_in": 86400,
    "token_type": "Bearer"
}
```
Then for each request, provide an "Authorization" with that access_token:

```
GET /api/campaign/donation HTTP/1.1
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6
```
### To deploy the app
```
heroku deploy:jar build/libs/*.jar -app causea-test
```
  
