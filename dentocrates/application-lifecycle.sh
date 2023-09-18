#!/bin/bash
# Log file path
LOG_FILE="your_absolute_path_to/Dentocrates/logfile.log"

function log_message() {
    local timestamp
    timestamp=$(date +"%Y-%m-%d %T")
    echo "$timestamp: $1" >> "$LOG_FILE"
}

# Function to handle Ctrl+C (SIGINT)
function shutdown_backend() {
    log_message "Shutting down Backend..."
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
        log_message "Process not found on port 8080."
        exit 1
    fi
}

function shutdown_proxy_server() {
    log_message "Shutting down proxy server..."
      proxy_pid=$(lsof -t -i :3001)

      if [ -n "$proxy_pid" ]; then
          log_message "Found PID: $proxy_pid"

          kill -15 $proxy_pid
          # Shutdown proxy server
          shutdown_backend
      else
          log_message "Process not found on port 3001."
          shutdown_backend
      fi
}

function shutdown_frontend() {
    log_message "Shutting down Frontend..."
    frontend_pid=$(lsof -t -i :3000)

    if [ -n "$frontend_pid" ]; then
        log_message "Found PID: $frontend_pid"
        kill -15 $frontend_pid
        shutdown_proxy_server
    else
        log_message "Process not found on port 3000."
        shutdown_proxy_server
    fi
}

function shutdown_chat_bot() {
  log_message "Shutting down ChatBot..."
  chat_pid=$(lsof -t -i :5000)

  if [ -n "$chat_pid" ]; then
      log_message "Found PID: $chat_pid"
      kill -15 $chat_pid
      shutdown_frontend
  else
      log_message "Process not found on port 5000."
      shutdown_frontend
  fi
}

# Register the shutdown_backend function to be executed when Ctrl+C is pressed
trap shutdown_chat_bot SIGINT


export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64" # replace with your path to jdk
export EMAIL="dentocrates@dentocrates.com"
export oauthId=your_oauth_id
export oauthSecret=your_oauth_secret
export SECRET=your_jwt_secret
export SENDER_PASSWORD=your_email_sender_authenticating_password
export SENDER_USERNAME=your_email_sender_authenticating_username

BACKEND_DIR="your_absolute_path_to/Dentocrates/dentocrates/"
FRONTEND_DIR="your_absolute_path_to/Dentocrates/dentocrates/frontend/"
CHAT_BOT_DIR="your_absolute_path_to/ConvoCat/"
# Change to the backend directory
log_message "Changing to the backend directory..."
cd "$BACKEND_DIR" || { log_message "Error: Unable to change to backend directory."; exit 1; }

# Start the backend (Spring-Java)
log_message "Cleaning and building the backend..."
./mvnw clean install -Dmaven.test.skip=true

log_message "Starting Backend..."
./mvnw spring-boot:run &

# Sleep for a few seconds to allow the backend to start
sleep 10

# Change to the frontend directory
log_message "Changing to the frontend directory..."
cd "$FRONTEND_DIR" || { log_message "Error: Unable to change to frontend directory."; shutdown_backend; }

# Start the frontend (React)
log_message "Starting Frontend..."
npm start &
log_message "Starting Proxy Server..."
npm run start-proxy &

cd "$CHAT_BOT_DIR" || { log_message "Error: Unable to change to chatbot directory."; shutdown_frontend; }
log_message "Starting ChatBot..."
python3 app.py &
wait

