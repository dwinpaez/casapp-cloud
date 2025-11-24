# CasApp Cloud - Microservices Lab

A microservices-based application laboratory built with Spring Boot and Kubernetes, designed for learning and experimenting with cloud-native architecture patterns.

## Overview

CasApp Cloud is a distributed system that demonstrates modern microservices architecture, featuring OAuth2 authentication, service discovery, and container orchestration with Kubernetes. This project serves as a hands-on learning environment for cloud-native development.

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                      Minikube Cluster                           │
│                                                                 │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐    │
│  │  msvc-auth   │    │ msvc-clients │    │msvc-bookings │    │
│  │   (8083)     │◄───┤   (8081)     │◄───┤   (8082)     │    │
│  │              │    │              │    │              │    │
│  │ OAuth2 Server│    │ Client Mgmt  │    │Booking Mgmt  │    │
│  └──────────────┘    └──────────────┘    └──────────────┘    │
│         ▲                    ▲                    ▲            │
│         │                    │                    │            │
│         └────────────────────┴────────────────────┘            │
│                              │                                 │
│                    ┌─────────▼──────────┐                      │
│                    │   msvc-gateway     │                      │
│                    │     (8090)         │                      │
│                    │   API Gateway      │                      │
│                    └────────────────────┘                      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## Microservices

| Service | Port | Description | Technology |
|---------|------|-------------|------------|
| **msvc-auth** | 8083 | OAuth2 Authorization Server | Spring Security OAuth2 |
| **msvc-clients** | 8081 | Client management & OAuth2 Resource Server | Spring Boot, H2 Database |
| **msvc-bookings** | 8082 | Booking management system | Spring Boot, H2 Database |
| **msvc-gateway** | 8090 | API Gateway & routing | Spring Cloud Gateway |

## Key Features

- **OAuth2/OIDC Authentication**: Centralized authentication with authorization code flow
- **Service Discovery**: Spring Cloud Kubernetes for automatic service registration
- **Dynamic Configuration**: ConfigMaps for environment-specific settings
- **Container Orchestration**: Kubernetes deployments with health checks
- **API Gateway**: Single entry point with routing and load balancing
- **H2 In-Memory Databases**: Lightweight persistence for development

## Technology Stack

- **Framework**: Spring Boot 3.5.4 / Spring Cloud
- **Language**: Java 21
- **Build Tool**: Maven
- **Authentication**: Spring Security OAuth2 Authorization Server
- **Containerization**: Docker
- **Orchestration**: Kubernetes (Minikube for local development)
- **Service Discovery**: Spring Cloud Kubernetes
- **Database**: H2 (in-memory)

## Quick Start

```bash
# Start Minikube
minikube start

# Deploy all services
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/
kubectl apply -f msvc-auth/k8s/
kubectl apply -f msvc-clients/k8s/
kubectl apply -f msvc-bookings/k8s/
kubectl apply -f msvc-gateway/k8s/

# Get service URLs
minikube service msvc-gateway --url --namespace=casapp
```

---

# Development Guide

## Change java version with SDKMAN
sdk list java
sdk install java 11.0.14.1-jbr
sdk use java 11.0.14.1-jbr
sdk use java 17.0.10-jbr

# Create jar
./mvnw clean package
./mvnw clean package -DskipTests

# Create image
sudo docker build -t msvc-clients . -f Dockerfile
sudo docker build -t msvc-clients:latest . -f msvc-clients/Dockerfile
sudo docker build -t msvc-bookings:latest . -f msvc-bookings/Dockerfile
sudo docker build -t msvc-gateway:latest . -f msvc-gateway/Dockerfile
sudo docker build -t msvc-auth:latest . -f msvc-auth/Dockerfile

# List images
sudo docker images
minikube service msvc-clients --url --namespace=casappsvc-clients/.env  --network casapp-net msvc-clients
sudo docker run -d --rm --name msvc-bookings -p 8082:8082 --env-file msvc-bookings/.env --network casapp-net msvc-bookings

# Attach container
sudo docker attach <id-container>

# Logs
sudo docker logs -f <id-container>

# Remove all images or containers
sudo docker rmi <id-images>
sudo docker image prune
sudo docker rm <id-container>
sudo docker container prune

# List containers
sudo docker ps
sudo docker ps -a 

# Stop container
sudo docker stop msvc-clients

# Mode interactive
sudo docker run --rm -it --add-host=host.docker.internal:host-gateway -p 8081:8081 msvc-clients /bin/sh

# Docker network creation
sudo docker network create casapp-net
sudo docker network list

# Docker tag
sudo docker tag msvc-clients dwinpaez/msvc-clients
sudo docker push dwinpaez/msvc-clients

sudo docker tag msvc-bookings dwinpaez/msvc-bookings
sudo docker push dwinpaez/msvc-bookings

sudo docker tag msvc-gateway dwinpaez/msvc-gateway
sudo docker push dwinpaez/msvc-gateway

# Docker login
sudo docker login -u dwinpaez

# Minikube
minikube start
minikube dashboard
minikube stop
minikube service msvc-clients --url --namespace=casapp   # show URL
minikube service msvc-bookings --url --namespace=casapp   # show URL

# kubectl apply
kubectl apply -f msvc-clients/k8s/configmap.yaml
kubectl apply -f msvc-clients/k8s/deployment.yaml

kubectl apply -f msvc-auth/k8s/configmap.yaml
kubectl apply -f msvc-auth/k8s/deployment.yaml
# kubectl delete
kubectl delete -f msvc-clients/k8s/deployment.yaml 

kubectl rollout restart deploy msvc-clients --namespace=casapp
kubectl create clusterrolebinding admin --clusterrole=cluster-admin --serviceaccount=casapp:default

# Run clients with sh
sh clients-run-docker.sh {profile} example: sh clients-run-docker.sh dev


# Build complete
sudo docker build -t msvc-clients:latest . -f msvc-clients/Dockerfile
sudo docker tag msvc-clients dwinpaez/msvc-clients:latest
sudo docker push dwinpaez/msvc-clients:latest
kubectl apply -f msvc-clients/k8s/configmap.yaml
kubectl apply -f msvc-clients/k8s/deployment.yaml

sudo docker build -t msvc-bookings:latest . -f msvc-bookings/Dockerfile
sudo docker tag msvc-bookings dwinpaez/msvc-bookings:latest
sudo docker push dwinpaez/msvc-bookings:latest

sudo docker tag msvc-gateway dwinpaez/msvc-gateway:latest
sudo docker push dwinpaez/msvc-gateway:latest

sudo docker build -t msvc-auth:latest . -f msvc-auth/Dockerfile
sudo docker tag msvc-auth dwinpaez/msvc-auth:latest
sudo docker push dwinpaez/msvc-auth:latest

sudo docker build -t msvc-clients:latest . -f msvc-clients/Dockerfile && sudo docker tag msvc-clients dwinpaez/msvc-clients:latest && sudo docker push dwinpaez/msvc-clients:latest
kubectl delete -f k8s/configmap.yaml && kubectl create -f k8s/configmap.yaml

# Build gateway
sudo docker build -t msvc-gateway:latest . -f msvc-gateway/Dockerfile
sudo docker tag msvc-gateway dwinpaez/msvc-gateway:latest
sudo docker push dwinpaez/msvc-gateway:latest
kubectl apply -f msvc-gateway/k8s/deployment.yaml
minikube service msvc-gateway --url --namespace=casapp

# AWS

sudo apt install awscli
aws configure
    AWS Access Key ID [None]: AKI----
    AWS Secret Access Key [None]: D1L---
    Default region name [None]: us-east-1    
    Default output format [None]: json

aws sts get-caller-identity

# K9S
k9s
:service
:pod


# Ports Configuration

## Application Ports (targetPort - where Spring Boot listens inside container)
- msvc-auth: 8083
- msvc-bookings: 8082
- msvc-clients: 8081
- msvc-gateway: 8090

## Kubernetes Service Ports (port - what other services use to communicate)
- msvc-auth: 8003
- msvc-bookings: 8002
- msvc-clients: 8001

## Port Mapping Explanation
In Kubernetes Services:
- `targetPort`: The port where your Spring Boot app listens inside the Pod (8081, 8082, 8083)
- `port`: The port exposed by the Kubernetes Service for inter-service communication (8001, 8002, 8003)

Example flow:
```
msvc-clients calls http://msvc-bookings:8002
  → K8s Service (port 8002)
  → Pod (targetPort 8082)
  → Spring Boot listening on 8082
```

When calling between microservices inside Kubernetes, always use the Service port (8001, 8002, 8003)

# Kubernetes Complete Deployment

## Deploy to Minikube (Full Process)

```bash
# 1. Apply namespace
kubectl apply -f k8s/namespace.yaml

# 2. Apply global resources
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/cluster-role.yaml

# 3. Apply ConfigMaps
kubectl apply -f msvc-auth/k8s/configmap.yaml
kubectl apply -f msvc-clients/k8s/configmap.yaml
kubectl apply -f msvc-bookings/k8s/configmap.yaml

# 4. Apply Services (this creates the DNS entries for service discovery)
kubectl apply -f msvc-auth/k8s/service.yaml
kubectl apply -f msvc-clients/k8s/service.yaml
kubectl apply -f msvc-bookings/k8s/service.yaml

# 5. Apply Deployments
kubectl apply -f msvc-auth/k8s/deployment.yaml
kubectl apply -f msvc-clients/k8s/deployment.yaml
kubectl apply -f msvc-bookings/k8s/deployment.yaml
kubectl apply -f msvc-gateway/k8s/deployment.yaml

# 6. Verify deployment
kubectl get all -n casapp
kubectl get pods -n casapp

# 7. Access services externally via Minikube
minikube service msvc-auth --url --namespace=casapp
minikube service msvc-clients --url --namespace=casapp
minikube service msvc-bookings --url --namespace=casapp
```

## Update and Redeploy a Service

```bash
# After making changes to code, rebuild and redeploy:
sudo docker build -t msvc-auth:latest . -f msvc-auth/Dockerfile
sudo docker tag msvc-auth dwinpaez/msvc-auth:latest
sudo docker push dwinpaez/msvc-auth:latest

# Update ConfigMap if needed
kubectl apply -f msvc-auth/k8s/configmap.yaml

# Restart deployment to pull new image
kubectl rollout restart deploy msvc-auth --namespace=casapp

# Check rollout status
kubectl rollout status deploy msvc-auth --namespace=casapp
```