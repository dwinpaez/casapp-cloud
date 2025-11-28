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

## Testing OAuth2 Authentication Flow

### Architecture Overview

```
┌─────────────┐         ┌──────────────┐         ┌──────────────┐
│   Client    │────1───▶│  msvc-auth   │         │ msvc-clients │
│  (Browser)  │◀───2────│  (OAuth2     │         │ (Resource    │
│             │         │   Server)    │         │  Server)     │
│             │────3───▶│              │         │              │
│             │◀───4────│              │         │              │
│             │         └──────────────┘         └──────────────┘
│             │                                          ▲
│             │──────────5 (with JWT token)─────────────┘
└─────────────┘
```

1. User accesses protected resource
2. Redirected to login (msvc-auth)
3. User submits credentials
4. Receives JWT token
5. Uses token to access resources

### Credentials

- **Username**: `admin`
- **Password**: `test123`

(Configured in `msvc-auth/SecurityConfig.java:108-112`)

### Step-by-Step Testing

#### Method 1: Using cURL (Recommended for API testing)

**Step 1: Get Authorization Code**

Open in browser:
```
http://<MSVC_AUTH_URL>/oauth2/authorize?response_type=code&client_id=msvc-oidc-client&scope=read%20write&redirect_uri=http://<MSVC_CLIENTS_URL>/login/oauth2/code/msvc-oidc-client
```

Replace `<MSVC_AUTH_URL>` and `<MSVC_CLIENTS_URL>` with your actual URLs from:
```bash
minikube service msvc-auth --url --namespace=casapp
minikube service msvc-clients --url --namespace=casapp
```

Login with `admin/test123`, approve scopes, then copy the `code` from the redirect URL.

**Step 2: Exchange code for token**

```bash
curl -X POST http://<MSVC_AUTH_URL>/oauth2/token \
  -u msvc-oidc-client:casapp-secret \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=authorization_code&code=<CODE>&redirect_uri=http://<MSVC_CLIENTS_URL>/login/oauth2/code/msvc-oidc-client"
```

Response:
```json
{
  "access_token": "eyJraWQiOiI...",
  "refresh_token": "...",
  "scope": "read write",
  "token_type": "Bearer",
  "expires_in": 299
}
```

**Step 3: Access protected resource**

```bash
# Should return 401 Unauthorized
curl http://<MSVC_CLIENTS_URL>/client

# Should return 200 OK with clients data
curl -H "Authorization: Bearer <ACCESS_TOKEN>" http://<MSVC_CLIENTS_URL>/client
```

#### Method 2: Testing with Postman (Recommended - Saves Configuration)

**Step 1: Create a New Request**

1. Click **"New" → "HTTP Request"**
2. Name: `Get Clients with OAuth2`
3. URL: `http://<MSVC_CLIENTS_URL>/client`
4. Method: `GET`

**Step 2: Configure Authorization**

In the **"Authorization"** tab:

| Field | Value |
|-------|-------|
| **Type** | `OAuth 2.0` |
| **Add auth data to** | `Request Headers` |

**Step 3: Configure New Token**

Click **"Configure New Token"** and fill in:

| Field | Value |
|-------|-------|
| **Token Name** | `CasApp Token` |
| **Grant Type** | `Authorization Code` |
| **Callback URL** | `http://<MSVC_CLIENTS_URL>/login/oauth2/code/msvc-oidc-client` |
| **Auth URL** | `http://<MSVC_AUTH_URL>/oauth2/authorize` |
| **Access Token URL** | `http://<MSVC_AUTH_URL>/oauth2/token` |
| **Client ID** | `msvc-oidc-client` |
| **Client Secret** | `casapp-secret` |
| **Scope** | `read write` |
| **Client Authentication** | `Send as Basic Auth header` |

**Step 4: Get Token**

1. Click **"Get New Access Token"**
2. Browser opens → Login with `admin/test123`
3. Approve scopes (read, write)
4. Postman receives token automatically
5. Click **"Use Token"**

**Step 5: Send Request**

1. Click **"Send"**
2. Should receive 200 OK with clients data

**Step 6: Save to Collection (Optional)**

1. Right-click request → **"Add to Collection"**
2. Create collection: `CasApp Microservices`
3. OAuth2 config is saved with the request

**Pro Tip:** Configure OAuth2 at **Collection level** for all requests:
- Right-click Collection → **Edit** → **Authorization** tab
- Configure OAuth2 once
- In each request: Select **"Inherit auth from parent"**

#### Method 3: Quick Verification (Without Token)

Test that security is working:

```bash
# Get service URLs
MSVC_CLIENTS_URL=$(minikube service msvc-clients --url --namespace=casapp)

# Should return 401 Unauthorized (HTML login page is wrong!)
curl -i $MSVC_CLIENTS_URL/client
```

**Expected behavior:**
- Response code: `401 Unauthorized`
- Content-Type: `application/json` (NOT `text/html`)
- Body: JSON error, NOT HTML login page

### Troubleshooting

**Problem**: Getting HTML login page with 200 status instead of 401

**Solution**: Make sure `msvc-clients/SecurityConfig.java` only has `oauth2ResourceServer`, not `oauth2Login`

**Problem**: Token validation fails

**Solution**: Verify `issuer-uri` in both services point to the same auth server:
```bash
kubectl get configmap msvc-clients -n casapp -o yaml | grep issuer
```

### Getting Service URLs

```bash
# Get all service URLs
minikube service msvc-auth --url --namespace=casapp
minikube service msvc-clients --url --namespace=casapp
minikube service msvc-bookings --url --namespace=casapp
```

These URLs change when Minikube restarts, so always verify before testing.