# PCB Assembly Simulation – Project 4

A minimal end-to-end Java project that

* simulates a small PCB production line for three board types (Test, Sensor, Gateway)
* persists every 1 000-board run to an SQLite database
* exposes results through a REST endpoint
* provides a tiny CLI client that fetches and prints a formatted report

The code purposefully stays framework-free: JDK `HttpServer`, plain JDBC, hand-rolled JSON utilities, and simple dependency injection through constructors.

---

## Contents

```
pcb-assembly-sim/
├── server/
│   └── src/main/java/com/example/pcb/server/
│       ├── …                                   core simulation + REST
│       └── persistence/
│           ├── ResultStore.java                DAO interface
│           └── SQLiteResultStore.java          JDBC implementation
├── client/
│   └── src/main/java/com/example/pcb/client/   reporting CLI
└── run_all.sh                                  build-and-demo helper
```

---

## Design highlights

* Strategy pattern (`PCBType` + three concrete boards)
* Factory Method (`PCBFactory`)
* Constructor-based dependency injection (`Simulation` receives its strategy)
* DAO façade (`ResultStore`) with a single SQLite adapter
* REST layer based on JDK `HttpServer` – `GET /results?type=TEST|SENSOR|GATEWAY`

---

## Prerequisites

* JDK 17 or later
* Three external libraries (see `run_all.sh` for direct Maven Central URLs)

  * `sqlite-jdbc-3.45.0.0.jar`
  * `slf4j-api-2.0.13.jar`
  * `slf4j-nop-2.0.13.jar`

---

## Quick start

```
chmod +x run_all.sh
./run_all.sh
```

The script

1. checks the three JARs are present (downloads are shown if missing)
2. compiles all sources into `out/`
3. launches `SimulationServer` on port 8000
4. runs the CLI client three times (`TEST`, `SENSOR`, `GATEWAY`)
5. terminates the server

Typical console output:

```
[Server] Stored Test Board result in DB
[Server] Stored Sensor Board result in DB
[Server] Stored Gateway Board result in DB
HTTP endpoint ready at http://localhost:8000/results

==> Report for TEST:

=== Test Board  –  1000 boards ===
...
```

---

## Manual build (javac)

```bash
JARS="sqlite-jdbc-3.45.0.0.jar:slf4j-api-2.0.13.jar:slf4j-nop-2.0.13.jar"
javac -cp "$JARS" -d out $(find server/src/main/java client/src/main/java -name '*.java')

# start server
java -cp "$JARS:out" com.example.pcb.server.SimulationServer

# in another shell
java -cp "$JARS:out" com.example.pcb.client.ReportClient SENSOR
```

---

## REST API reference

```
GET /results?type=TEST|SENSOR|GATEWAY
200 OK
Content-Type: application/json

{
  "pcbType":"Sensor Board",
  "pcbsRun":1000,
  "stationFailures":{"Apply Solder Paste":1, …},
  "pcbDefectFailures":{"Place Components":2, …},
  "totalFailed":34,
  "totalProduced":966
}
```

Example request:

```
curl http://localhost:8000/results?type=GATEWAY
```

---

## Extending the project

* To use Redis instead of SQLite, implement a `RedisResultStore` that fulfils `ResultStore` and change one line in `SimulationServer.main`.
* The `JsonUtil` helpers intentionally avoid third-party parsers. Swapping for Jackson or Gson is straightforward if external libraries are permitted.
* Concurrency: replace `server.setExecutor(null)` with a fixed-thread pool to serve multiple clients.
