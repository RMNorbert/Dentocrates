#!/bin/bash

PROJECT_DIR="$(dirname "$(readlink -f "$0")")"
LOG_DIR="$PROJECT_DIR/.."
LOG_FILE="$LOG_DIR/logfile.log"
NOT_FOUND="Process not found on port"
DIRECTORY_CHANGE="Changing directory to "
DIRECTORY_CHANGE_ERROR="Error: Unable to change directory to "
STARTING="Starting "
SHUTTING_DOWN="Shutting down "

function log_message() {
    local timestamp
    timestamp=$(date +"%Y-%m-%d %T")
    echo "$timestamp: $1" >> "$LOG_FILE"
}

# Function to handle Ctrl+C (SIGINT)
function shutdown_backend() {
    log_message "$SHUTTING_DOWN Backend"
    # Find the PID of the process listening on port 8080
    pid=$(lsof -t -i :8080)

    # Check if the PID is not empty (process found)
    if [ -n "$pid" ]; then
        log_message "Found PID: $pid"

        # Send a termination signal (SIGTERM) to the PID
        kill -15 $pid
        log_message "Sent SIGTERM to process $pid"
        log_message "Script completed successfully."
        exit 0
    else
        log_message "$NOT_FOUND 8080."
        exit 1
    fi
}

function shutdown_frontend() {
    log_message "$SHUTTING_DOWN Frontend"
    frontend_pid=$(lsof -t -i :3000)

    if [ -n "$frontend_pid" ]; then
        log_message "Found PID: $frontend_pid"
        kill -15 $frontend_pid
        shutdown_backend
    else
        log_message "$NOT_FOUND 3000."
        shutdown_backend
    fi
}

function shutdown_chat_bot() {
  log_message "$SHUTTING_DOWN ChatBot"
  chat_pid=$(lsof -t -i :5000)

  if [ -n "$chat_pid" ]; then
      log_message "Found PID: $chat_pid"
      kill -15 $chat_pid
      shutdown_frontend
  else
      log_message "$NOT_FOUND 5000."
      shutdown_frontend
  fi
}

# Register the shutdown_backend function to be executed when Ctrl+C is pressed
trap shutdown_chat_bot SIGINT


export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64" # replace with your path to jdk
export EMAIL="dentocrates@dentocrates.com"
export oauthId=your_oauth_id
export oauthSecret=your_oauth_secret
export SECRET=048492546432436A442848F3147319D665857603B6B272848F751725773475237A5E567A66564
export SENDER_USERNAME=your_email_sender_email_address
export REDIRECT_URI=http://localhost:3000/login/oauth2/code/


FRONTEND_DIR="$PROJECT_DIR/frontend/"
CHAT_BOT_DIR="$PROJECT_DIR/../ConvoCat/"
# Change to the backend directory
log_message "$DIRECTORY_CHANGE backend..."
cd "$PROJECT_DIR" || { log_message "$DIRECTORY_CHANGE_ERROR backend."; exit 1; }

# Generate the Maven Wrapper (if not already generated)
if [ ! -f "mvnw" ]; then
    log_message "Generating Maven Wrapper..."
    mvn wrapper:wrapper
    chmod +x mvnw  # Make mvnw executable
fi

# Start the backend (Spring-Java)
log_message "Cleaning and building the backend..."
./mvnw clean install -Dmaven.test.skip=true

log_message "$STARTING Backend..."
./mvnw spring-boot:run &

# Sleep for a few seconds to allow the backend to start
sleep 10

# Change to the frontend directory
log_message "$DIRECTORY_CHANGE frontend..."
cd "$FRONTEND_DIR" || { log_message "$DIRECTORY_CHANGE_ERROR frontend."; shutdown_backend; }

# Start the frontend (React)
log_message "$STARTING Frontend..."
npm start &

cd "$CHAT_BOT_DIR" || { log_message "$DIRECTORY_CHANGE_ERROR chatbot."; shutdown_frontend; }
log_message "$STARTING ChatBot..."
python3 app.py &
wait

