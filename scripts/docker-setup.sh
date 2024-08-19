#!/bin/bash

set -x  # Enable script debugging

ECR_REPO_URI="public.ecr.aws/u0f0p0q3"
declare -A SERVICES
SERVICES=(
    ["ci-cd-demo"]="8080"
    ["CartService"]="8084"
    ["OrderService"]="8085"
    ["PaymentService"]="8086"
    ["ProductCatalogService"]="8083"
    ["ServiceDiscovery"]="8761"
    ["UserService"]="8082"
    ["EmailService"]="8081"
)

# Function to check if a Docker container is running
function is_container_running() {
    docker ps --filter "name=$1" --format '{{.Names}}' | grep -w "$1" &> /dev/null
}

# Function to check if a Docker image exists locally
function is_image_installed() {
    docker image inspect $1 &> /dev/null
}

# Start Redis if not running
if ! is_container_running "redis"; then
    if ! is_image_installed "redis:latest"; then
        echo "Pulling Redis image..."
        docker pull redis:latest
    fi
    echo "Starting Redis..."
    docker run -d --name redis -p 6379:6379 redis:latest
else
    echo "Redis is already running."
fi

# Start Zookeeper and Kafka if not running
if ! is_container_running "kafka"; then
    if ! is_image_installed "zookeeper:latest"; then
        echo "Pulling Zookeeper image..."
        docker pull zookeeper:latest
    fi
    if ! is_image_installed "kafka:latest"; then
        echo "Pulling Kafka image..."
        docker pull kafka:latest
    fi
    echo "Starting Zookeeper..."
    docker run -d --name zookeeper -p 2181:2181 zookeeper:latest
    echo "Starting Kafka..."
    docker run -d --name kafka -p 9092:9092 --link zookeeper kafka:latest
else
    echo "Kafka and Zookeeper are already running."
fi

# Start Elasticsearch if not running
if ! is_container_running "elasticsearch"; then
    if ! is_image_installed "elasticsearch:latest"; then
        echo "Pulling Elasticsearch image..."
        docker pull elasticsearch:latest
    fi
    echo "Starting Elasticsearch..."
    docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 elasticsearch:latest
else
    echo "Elasticsearch is already running."
fi

# Start MySQL if not running
if ! is_container_running "mysql"; then
    if ! is_image_installed "mysql:latest"; then
        echo "Pulling MySQL image..."
        docker pull mysql:latest
    fi
    echo "Starting MySQL..."
    docker run -d --name mysql -e MYSQL_ROOT_PASSWORD=admin@123 -p 3306:3306 mysql:latest
else
    echo "MySQL is already running."
fi

# Start all microservices
for SERVICE in "${!SERVICES[@]}"; do
    PORT="${SERVICES[$SERVICE]}"
    CONTAINER_NAME="$SERVICE"

    docker stop $CONTAINER_NAME || true
    docker rm $CONTAINER_NAME || true

    echo "Pulling latest Docker image for $SERVICE..."
    docker pull $ECR_REPO_URI/$SERVICE:latest

    echo "Running Docker container for $SERVICE on port $PORT..."
    docker run -d --name $CONTAINER_NAME -p $PORT:$PORT $ECR_REPO_URI/$SERVICE:latest

    echo "Docker container for $SERVICE started on port $PORT."
done
