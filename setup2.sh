kkk#!/bin/bash

# Variables
REPO_URL="https://github.com/godaasinskaite/book_management_api" 
PROJECT_DIR="book_management_api_1"  # Directory for the cloned repository

# Function to clone a repository
clone_repo() {
  local repo_url=$1
  local target_dir=$2

  # Check if the target directory already exists
  if [ -d "$target_dir" ]; then
    echo "Directory $target_dir already exists. Skipping clone."
  else
    echo "Cloning repository $repo_url into $target_dir..."
    git clone "$repo_url" "$target_dir"

    # Check if the clone was successful
    if [ $? -ne 0 ]; then
      echo "Failed to clone repository $repo_url"
      exit 1
    fi
  fi
}

# Function to build and run Docker Compose
run_docker_compose() {
  local project_dir=$1

  echo "Navigating to $project_dir..."
  cd "$project_dir" || exit 1

  echo "Running Docker Compose in $project_dir..."
  docker-compose up --build -d

  # Check if Docker Compose ran successfully
  if [ $? -ne 0 ]; then
    echo "Failed to run Docker Compose in $project_dir"
    exit 1
  fi

  # Navigate back to the original directory
  cd - || exit 1
}

# Clone the Spring Boot repository
clone_repo "$REPO_URL" "$PROJECT_DIR"

# Build and run Docker Compose for the project
run_docker_compose "$PROJECT_DIR"

# Wait for a few seconds to allow the application to start
echo "Waiting for the application to start..."
sleep 10  # Adjust the duration as needed

# Open Swagger UI in the default web browser
echo "Opening Swagger UI..."

# Use xdg-open or open (fallback) for simplicity
if command -v xdg-open > /dev/null; then
  xdg-open "http://localhost:8080/swagger-ui/index.html#/"
elif command -v open > /dev/null; then
  open "http://localhost:8080/swagger-ui/index.html#/"
else
  echo "Open http://localhost:8080/swagger-ui/index.html#/ in your web browser"
fi

