#!/usr/bin/env bash
set -euo pipefail

JARS=(
  "lib/sqlite-jdbc-3.45.0.0.jar"
  "lib/slf4j-api-2.0.13.jar"
  "lib/slf4j-nop-2.0.13.jar"
)

OUT=out
SRC_DIRS=( "src/main/java" )
CLASSPATH=$(IFS=:; echo "${JARS[*]}")":$OUT"

echo "Compiling..."
mkdir -p "$OUT"
javac -cp "$CLASSPATH" -d "$OUT" $(find ${SRC_DIRS[@]} -name '*.java')

echo "Running..."
java -cp "$CLASSPATH" com.example.pcb.MainApp
