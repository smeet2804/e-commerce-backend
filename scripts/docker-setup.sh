#!/bin/bash

set -x  # Enable script debugging

ECR_REPO_URI="public.ecr.aws/u0f0p0q3/ci-cd-demo:latest"
CONTAINER_NAME="ci-cd-demo"

docker stop $CONTAINER_NAME || true
docker rm $CONTAINER_NAME || true

echo "Pulling latest Docker image..."
docker pull $ECR_REPO_URI

echo "Running Docker container..."
docker run -d --name $CONTAINER_NAME -p 8080:8080 $ECR_REPO_URI

echo "Docker container started."
