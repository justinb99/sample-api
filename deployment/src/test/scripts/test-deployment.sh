#!/usr/bin/env bash

EXIT_CODE=0
# Test with an alternative port
PORT=9000
URL="http://localhost:${PORT}"
TIMESPAN="start=2015-07-01T07%3A00%3A00Z&end=2015-07-01T12%3A00%3A00Z"

function GET() {
    echo "GET ${1}"
    curl -s -f "${URL}${1}"

    if [ $? -gt 0 ]; then
        echo "FAILED"
        EXIT_CODE=$((EXIT_CODE+1))
    fi

    echo ""
    echo ""
}

function GET_Rate() {
    GET "${1}?${TIMESPAN}"
}

# Utilizes the Metrics PingServlet to detect when the service is up
function waitForService() {
    started=0

    for i in `seq 1 15`;
    do
        curl -s "${URL}/ping" > /dev/null 2>&1
        if [ $? -gt 0 ]; then
            echo "Ping failed, service unavailable."
            sleep 1
        else
            echo "Service available!"
            started=1
            break
        fi
    done

    if [ ${started} -eq 0 ]; then
        echo "Service failed to initialize. Exiting..."
        exit 1
    fi
}

java -Dport=${PORT} -jar target/sample-api.jar > logs/test-uber-jar.log 2>&1 &
SERVER_PID=$!

echo "Server started on Port ${PORT} with PID ${SERVER_PID}"

echo "Waiting for server to initialize..."
waitForService

echo ""

GET_Rate "/v1/rate"
GET_Rate "/v1/rate.json"
GET_Rate "/v1/rate.xml"
GET_Rate "/v1/rate.proto"

GET "/metrics"

echo "Stopping server..."
kill ${SERVER_PID}

echo "Last 5 lines of log file:"
tail -n 5 logs/test-uber-jar.log

echo ""
echo "Exiting with ${EXIT_CODE}"
exit ${EXIT_CODE}
