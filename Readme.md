# Change java version with SDKMAN
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

# List images
sudo docker images

# Run image as container
sudo docker run -d --rm --name msvc-clients -p 8081:8081 --env-file msvc-clients/.env  --network casapp-net msvc-clients
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

# Docker login
sudo docker login -u dwinpaez

# Minikube
minikube start
minikube dashboard
minikube stop
minikube service msvc-clients --url --namespace=casapp
minikube service msvc-bookings --url --namespace=casapp

# kubectl apply
kubectl apply -f msvc-clients/k8s/deployment.yaml
# kubectl delete
kubectl delete -f msvc-clients/k8s/deployment.yaml 

kubectl rollout restart deploy msvc-clients --namespace=casapp
kubectl create clusterrolebinding admin --clusterrole=cluster-admin --serviceaccount=casapp:default

# Run clients with sh
sh clients-run-docker.sh {profile} example: sh clients-run-docker.sh dev
