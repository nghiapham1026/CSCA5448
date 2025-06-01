#!/usr/bin/env bash
set -euo pipefail
JARS=(
  "sqlite-jdbc-3.45.0.0.jar"
)
OUT=out
SRC_DIRS=( "src/main/java" )
CLASSPATH=$(IFS=:;echo "${JARS[*]}")":$OUT"

echo "Compiling..."
mkdir -p "$OUT"
javac -cp "$CLASSPATH" -d "$OUT" $(find ${SRC_DIRS[@]} -name '*.java')
echo "Running..."
java  -cp "$CLASSPATH" com.example.pcb.MainApp
