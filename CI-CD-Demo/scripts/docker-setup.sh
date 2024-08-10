#!/bin/bash

# Replace these placeholders with your actual values
ECR_REPO_URI="public.ecr.aws/u0f0p0q3/ci-cd-demo:latest"
CONTAINER_NAME="ci-cd-demo"

## Authenticate Docker to ECR
#$(aws ecr get-login-password --region <your-region> | docker login --username AWS --password-stdin <your-ecr-repo-uri>)

# Pull the latest Docker image from ECR
docker pull $ECR_REPO_URI

# Run the Docker container
docker run -d --name $CONTAINER_NAME -p 8080:8080 $ECR_REPO_URI
