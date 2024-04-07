# Change java version with SDKMAN
sdk list java
sdk install java 11.0.14.1-jbr
sdk use java 11.0.14.1-jbr
sdk use java 17.0.10-jbr

# Create jar
./mvnw clean package
./mvnw clean package -DskipTests

# Create image
sudo docker build . -t msvc-clients -f Dockerfile

# List images
sudo docker images

# Run image as container
sudo docker run --rm -it --add-host=host.docker.internal:host-gateway -p 8081:8081 msvc-clients

# List containers
sudo docker ps
sudo docker ps -a 

# Stop container
sudo docker stop msvc-clients