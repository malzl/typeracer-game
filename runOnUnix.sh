#!/bin/bash

# Define the root directory to search in
PROJECT_DIR=$(pwd)

# Attempt to locate the JavaFX SDK lib directory automatically within the project directory
echo "Searching for JavaFX SDK directory within the project..."
JAVA_FX_SDK_PATH=$(find $PROJECT_DIR -type f -name "javafx.base.jar" | head -n 1 | xargs dirname)

# Check if a possible SDK path was found
if [ -n "$JAVA_FX_SDK_PATH" ]; then
  echo "Possible JavaFX SDK found at $JAVA_FX_SDK_PATH"
  read -p "Is this the correct JavaFX SDK path? (yes/no): " response
  if [[ "$response" != "yes" ]]; then
    JAVA_FX_SDK_PATH="" # reset if the user says no
  fi
fi

# If no path was found or the user rejected the found path, ask for it
if [ -z "$JAVA_FX_SDK_PATH" ]; then
  read -p "Please enter the path to the JavaFX SDK lib directory: " JAVA_FX_SDK_PATH
fi

# Check if the entered path is valid
if [ ! -d "$JAVA_FX_SDK_PATH" ]; then
  echo "The path provided is not a valid directory. Exiting."
  exit 1
fi

# Prompt the user to specify the number of instances of game.jar to run (between 1 and 4 as this is the game maximum)
read -p "Please enter the number of game.jar instances to run (1-4): " NUM_INSTANCES

# Check if the number of instances is valid
if [[ "$NUM_INSTANCES" -lt 1 || "$NUM_INSTANCES" -gt 4 ]]; then
  echo "Invalid number of instances. Please enter a number between 1 and 4. Exiting."
  exit 1
fi

# Run the specified number of instances of the GameApp JAR
for ((i=1; i<=NUM_INSTANCES; i++)); do
  java --module-path $JAVA_FX_SDK_PATH --add-modules javafx.controls,javafx.fxml \
    --add-exports javafx.graphics/com.sun.glass.utils=ALL-UNNAMED \
    --add-exports javafx.base/com.sun.javafx=ALL-UNNAMED \
    --add-exports javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED \
    -jar JARs/GameApp/typewrite-game-1.0-SNAPSHOT-all.jar &
  echo "Started instance $i of game.jar"
done

# Run the Server JAR
java --module-path $JAVA_FX_SDK_PATH --add-modules javafx.controls,javafx.fxml \
  --add-exports javafx.graphics/com.sun.glass.utils=ALL-UNNAMED \
  --add-exports javafx.base/com.sun.javafx=ALL-UNNAMED \
  --add-exports javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED \
  -jar JARs/ServerApp/typewrite-game-1.0-SNAPSHOT-server.jar &
echo "Started server.jar"
