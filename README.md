# URL Shortener - Reducto

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)

A simple URL Shortener Service

## Getting Started

### Prerequisites

- [JDK 21](https://openjdk.org/projects/jdk/21/)
- [Docker](https://www.docker.com/products/docker-desktop/)
- [Maven](https://maven.apache.org/) (Optional)
- [SDKMAN!](https://sdkman.io/) (Optional)

### Run locally

```
 ./mvnw clean package
 
 docker compose up -d
 
 ./mvnw spring-boot:run
```

Or run it using Docker

```
docker compose --profile local-dev up -d
```

If you are running in a Intel x64
```
docker compose build --build-arg BUILDPLATFORM=linux/amd64 url-shortener-api
docker compose --profile local-dev up -d
```


## API Description

All URIs are relative to *http://localhost:8080*

> [!NOTE]
> It's possible to have control doing an HTTP basic authentication **flitwick:alohomora**

> **POST** /api/shorten
- **Authorization**: Basic
- **Content-Type**: application/json
- **Accept**: application/json

#### Request | [UrlRequest](src/main/java/dev/felipebraga/urlshortener/controller/request/UrlRequest.java)
```json
{
  "url": "http://example.com",
  "expiresIn": "2023-12-18T19:21:00" // Optional
}
```

Only authenticated users can set an expire date.
Even in the case that `expiresIn` is pass to the request, the Shortener will ignore the value and use a default of +6h from now.

#### Response | [UrlCreateResponse](src/main/java/dev/felipebraga/urlshortener/controller/response/UrlCreatedResponse.java)
```json
{
  "shortenedUrl": "http://localhost:8080/RtD0to",
  "sourceUrl": "http://example.com",
  "expiresIn": "2023-12-18T19:21:00"
}
```

> **GET** /api/shorten/{uniqueId}

- **Authorization**: Basic
- **Content-Type**: application/json
- **Accept**: application/json

#### Response | [UrlResponse](src/main/java/dev/felipebraga/urlshortener/controller/response/UrlResponse.java)
```json
{
  "shortCode": "RtD0to",
  "shortenedUrl": "http://localhost:8080/RtD0to",
  "sourceUrl": "http://example.com",
  "expiresIn": "2023-12-18T19:21:00",
  "createdAt": "2023-12-18T18:21:00"
}
```

> **DELETE** /api/shorten/{uniqueId}
- **Authorization**: Basic
#### Response | 204 No Content

---
> **GET** /{uniqueId}
#### Response | 307

## Things do improve or do

