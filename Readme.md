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
sudo docker run -d --rm --name msvc-clients --add-host=host.docker.internal:host-gateway -p 8081:8081 msvc-clients
sudo docker run -d --rm --name msvc-bookings --add-host=host.docker.internal:host-gateway -p 8082:8082 msvc-bookings

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