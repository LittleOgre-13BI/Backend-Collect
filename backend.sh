#! /bin/bash 
docker rm -f container_springboot
docker image rm docker_springboot
docker build -t docker_springboot .
docker run --name="container_springboot" -d -p 8081:8080 docker_springboot 
docker ps
