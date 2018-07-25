# sample-api
An API that allows a user to get a parking rate for a time span.

## Getting Started

### Requirements
* Maven 3.5.3
* Java 10.0.1
* npm (optional, for documentation HTML re-generation)

### Build/Run/Query
1. Build the project with ```mvn clean install```
2. Start the web server with ```./run.sh```
    * The server runs synchronously and is bound to ```Runtime.addShutdownHook```.  It can be gracefully stopped with ```ctrl-c``` or by ```kill```ing the PID.
3. Query the web server (from a different shell) with ```./sample-request.sh```
4. View the documentation at <http://localhost:8080/docs/index.html>


## Project Modules and Structure

### documentation
This module contains the API documentation.  The documentation is defined in ```documentation/src/scripts/api.yaml```  The documentation is written in Swagger 2.0—<https://swagger.io>.

The server serves an interactive version of the documentation at <http://localhost:8080/docs/index.html>.  The static resources for the documenation are in ```documentation/src/main/resources/docs``` and are compiled into the documentation JAR when the documenation project is build.

The HTML documentation must be manually generated after changes to the api.yaml file are made.  Run ```./generate-docs.sh``` from within ```documentation/src/main/scripts``` to generate the HTML documentation.

The HTML documentation is generated by ```spectacle```—<https://github.com/sourcey/spectacle>.  Run ```./install-spectacle.sh``` to install Spectacle.  It is an NPM module.  The installation script assumes ```npm``` is in your path.  The ```generate-docs.sh``` script assumes ```spectacle``` and other NPM executables are in your path.

### schema
This module defines a ```Rate``` Protobuf message in ```rate.proto```.  This ```Rate``` message defines the response class for all Rate API endpoints, including Protobuf, JSON, and XML.  This application uses the library ```jackson-datatype-protobuf``` (<https://github.com/HubSpot/jackson-datatype-protobuf>) to allow Jackson to serialize a Protobuf to JSON, and with the help of the ```jackson-dataformat-xml``` library, to XML as well.

The ```schema``` module uses the ```protoc-jar-maven-plugin``` to generate the Java classes for ```rate.proto```.  This plugin has the advantage of containing the ```protoc``` binaries and does not require the ```protoc``` executable to be available.

### engine
This module contains the business logic for performing the Rate Service functionality.  It is separated from the ```service``` project so that a different web framework can be swapped out at any time.  It also defines and configures core web components, such as JSON serializers, so that input parsing and response formats remain consistent across web frameworks.  It utilizes Google's Guice Dependency Injection framework, and makes its components available via ```EngineModule```.

### service
This module implements the Rate API through a Jersey server. In addition to serving the Rate API, it serves the static documentation, and endpoint peformance metrics collected with the Dropwizard Metrics library—<https://metrics.dropwizard.io> 

### integration-tests
The file ```ServiceIT``` performs web-client level testing of the Rate API, as well as confirming the server returns the documentation and metrics.  With the exception of starting and stopping the web server within the test context, the tests have no direct dependency on a specific service implementation.  Therefore the tests would not need to change if the service implementation changed, or could be used to test multiple service implementations.

The ```integration-tests``` includes a test ```rates.json``` file in ```integration-tests/src/test/resources/rates.json``` for testing.

### deployment
This module packages all Rate API modules into a single executable uber jar in ```deployment/target/sample-api.jar```.  Since it is an executable jar, it can be started with ```java -jar sample-api.jar``` and is extremely portable.  Upon startup the service binds to port ```8080``` but can be configured with a different port via the ```port``` flag, e.g. ```-Dport=9000```.

This package is the only place where runtime resources are included, such as specifying a SLF4J implementation (currently LOG4J2).  It also includes the production ```rates.json``` file for deployment in ```deployment/src/main/resources```.  Currently this is the test rates, but this file could be updated for production deployment. 

After packaging the single jar, the deployment module runs a final smoke test via ```deployment/src/test/scripts/test-deployment.sh```.  The script starts the server in the background, waits for the ```/ping``` endpoint to respond, then makes a request to each endpoint, ensuring everything is wired up correctly.  