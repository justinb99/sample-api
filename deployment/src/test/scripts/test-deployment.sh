#!/usr/bin/env bash

# For relative paths, this script assumes it is executed out of the root of the deployment module

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

function GET_Silent() {
    echo "GET ${1}"
    curl -s -f "${URL}${1}" > /dev/null 2>&1

    if [ $? -gt 0 ]; then
        echo "FAILED"
        EXIT_CODE=$((EXIT_CODE+1))
    fi

    echo ""
    echo ""
}

function GET_404() {
    echo "GET ${1} should fail"
    curl -s -f "${URL}${1}"

    if [ $? -eq 0 ]; then
        echo "FAILED"
        EXIT_CODE=$((EXIT_CODE+1))
    fi

    echo ""
    echo ""
}

# Utilizes the Metrics PingServlet to detect when the service is up
function waitForService() {
    started=0

    for i in `seq 1 15`;
    do
        curl -s "${URL}/ping" > /dev/null 2>&1
        if [ $? -gt 0 ]; then
            echo "Ping failed, service unavailable. Will retry..."
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

mkdir -p logs
java -Dport=${PORT} -jar target/sample-api.jar > logs/test-deployment.log 2>&1 &
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

GET_Silent "/docs/index.html"
GET_Silent "/docs/javascripts/spectacle.js"

GET_404 "/rates.json"

echo "Stopping server..."
kill ${SERVER_PID}

echo "Last 5 lines of log file:"
tail -n 5 logs/test-deployment.log

echo ""
echo "Exiting with ${EXIT_CODE}"
exit ${EXIT_CODE}
