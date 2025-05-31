#!/usr/bin/env bash
# -----------------------------------------------
# run_all.sh – build & demo for PCB-sim Project-4
# -----------------------------------------------
set -euo pipefail

# ---------- dependency jars ---------------------------------
JARS=(
  "sqlite-jdbc-3.45.0.0.jar"     # JDBC driver
  "slf4j-api-2.0.13.jar"         # logging API
  "slf4j-nop-2.0.13.jar"         # no-op backend (quiet)
)

OUT_DIR="out"
SRC_DIRS=( "server/src/main/java" "client/src/main/java" )

# allow ./lib to keep jars in one place
CLASSPATH=$(IFS=:; echo "${JARS[*]}")":${OUT_DIR}"

# ---------- pre-flight --------------------------------------
for jar in "${JARS[@]}"; do
  [[ -r "$jar" ]] || {
    echo "Missing dependency: $jar"
    echo "Download from Maven Central, e.g.:
    curl -O https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.0.0/sqlite-jdbc-3.45.0.0.jar
    curl -O https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.13/slf4j-api-2.0.13.jar
    curl -O https://repo1.maven.org/maven2/org/slf4j/slf4j-nop/2.0.13/slf4j-nop-2.0.13.jar"
    exit 1
  }
done

# ---------- compile -----------------------------------------
echo "==> Compiling sources ..."
mkdir -p "$OUT_DIR"
SRC_FILES=$(find "${SRC_DIRS[@]}" -name '*.java')
javac -cp "$CLASSPATH" -d "$OUT_DIR" $SRC_FILES
echo "    Compilation finished."

# ---------- launch server -----------------------------------
echo "==> Starting SimulationServer ..."
java -cp "$CLASSPATH" com.example.pcb.server.SimulationServer &
SERVER_PID=$!

trap "echo -e '\n==> Stopping server (pid $SERVER_PID)'; kill $SERVER_PID 2>/dev/null || true" EXIT
sleep 2   # give it time to open the HTTP port

# ---------- query all board types ---------------------------
for TYPE in TEST SENSOR GATEWAY; do
  echo -e "\n==> Report for $TYPE:"
  java -cp "$CLASSPATH" com.example.pcb.client.ReportClient "$TYPE"
done

echo -e "\n==> All done."
