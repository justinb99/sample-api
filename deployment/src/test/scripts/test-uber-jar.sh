#!/usr/bin/env bash

EXIT_CODE=0
# Test with an alternative port
PORT=9000
URL="http://localhost:${PORT}"
TIMESPAN="start=2015-07-01T07%3A00%3A00Z&end=2015-07-01T12%3A00%3A00Z"

function GET() {
    echo "GET ${1}"
    curl -s -f "${URL}${1}?${TIMESPAN}"

    if [ $? -gt 0 ]; then
        echo "FAILED"
        EXIT_CODE=$((EXIT_CODE+1))
    fi

    echo ""
    echo ""
}

java -Dport=${PORT} -jar target/sample-api.jar > logs/test-uber-jar.log 2>&1 &
SERVER_PID=$!

echo "Server started on Port ${PORT} with PID ${SERVER_PID}"

echo "Waiting 5s for server to initialize..."
sleep 5
echo ""

GET "/v1/rate"
GET "/v1/rate.json"
GET "/v1/rate.xml"
GET "/v1/rate.proto"

echo "Stopping server..."
kill $SERVER_PID

echo "Last 5 lines of log file:"
tail -n 5 logs/test-uber-jar.log

echo ""
echo "Exiting with ${EXIT_CODE}"
exit ${EXIT_CODE}
