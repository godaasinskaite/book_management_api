kkk#!/bin/bash

REPO_URL="https://github.com/godaasinskaite/book_management_api" 
PROJECT_DIR="book_management_api_1"

clone_repo() {
  local repo_url=$1
  local target_dir=$2

  if [ -d "$target_dir" ]; then
    echo "Directory $target_dir already exists. Skipping clone."
  else
    echo "Cloning repository $repo_url into $target_dir..."
    git clone "$repo_url" "$target_dir"

    if [ $? -ne 0 ]; then
      echo "Failed to clone repository $repo_url"
      exit 1
    fi
  fi
}

run_docker_compose() {
  local project_dir=$1

  echo "Navigating to $project_dir..."
  cd "$project_dir" || exit 1

  echo "Running Docker Compose in $project_dir..."
  docker-compose up --build -d

  if [ $? -ne 0 ]; then
    echo "Failed to run Docker Compose in $project_dir"
    exit 1
  fi

  cd - || exit 1
}

clone_repo "$REPO_URL" "$PROJECT_DIR"

run_docker_compose "$PROJECT_DIR"

echo "Waiting for the application to start..."
sleep 5

echo "Opening Swagger UI..."

if command -v xdg-open > /dev/null; then
  xdg-open "http://localhost:8080/swagger-ui/index.html#/"
elif command -v open > /dev/null; then
  open "http://localhost:8080/swagger-ui/index.html#/"
else
  echo "Open http://localhost:8080/swagger-ui/index.html#/ in your web browser"
fi

