# AppBuilder
![Build Status](https://travis-ci.org/chihweil5/AppBuilder.svg?branch=master)
A web service that curates meta and build data for open source Android projects on GitHub

## Before you start
- Install Docker
    https://www.docker.com/products/overview

- Pull the ApplicationBuilder image from Docker Hub : chihweil5/builder
```
$ docker pull chihweil5/builder
```

## Run
- Run the image
```
docker run --rm -p 8080:8080 chihweil5/builder
```

- Open your browser and go to:
    http://localhost:8080/ApplicationBuilder-0.0.1-SNAPSHOT/appbuilder

