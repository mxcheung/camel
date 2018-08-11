# camel

https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.3/html/fuse_integration_services_2.0_for_openshift/camel-spring-boot
https://www.slideshare.net/mraible/developing-testing-and-scaling-with-apache-camel-uberconf-2015
https://github.com/davsclaus/camel-riders-in-the-cloud
http://camel.apache.org/articles.html
https://tech.willhaben.at/testing-apache-camel-applications-with-spring-boot-da536568d9f7
https://stackoverflow.com/questions/25869428/classpath-resource-not-found-when-running-as-jar
http://useof.org/java-open-source/org.beanio.BeanReader
https://github.com/apache/camel/blob/master/camel-core/src/test/java/org/apache/camel/component/file/AntPathMatcherGenericFileFilterTest.java

java -jar -Dspring.profiles.active=dev -Dcamelsimple.route.path=/route/dev/pub camelsimple.jar


java -jar -Dspring.profiles.active=dev -Dserver.port=8088 -Dcamelsimple.route.path=C:\camel\route\dev\pub .\target\camelsimple-0.0.1-SNAPSHOT.jar

java -jar -Dspring.profiles.active=dev 
-Dserver.port=8089 
-Dmanagement.port=8089 
-Dcamelsimple.route.path=C:\camel\route\dev\sub .\target\camelsimple-0.0.1-SNAPSHOT.jar


java -jar -Dspring.profiles.active=dev -Dserver.port=8089 -Dmanagement.port=8089 -Dcamelsimple.route.path=/route/dev/sub camelsimple.jar




-Dcamelsimple.route.path=/route/dev/pub
http://www.baeldung.com/java-application-logs-to-elastic-stack
elasticsearch-2.4.4
kibana-4.6.0-windows-x86
logstash-2.4.0


```
├── src/
│   └── Application.java/
│       ├── controller/
│       │   └── RouteController.java
│       ├── service/
│           └── RouteService.java
│           └── RouteServiceImpl.java
│       ├── route/
│       │   └── DefaultRouteBuilder.java
│       │   └── WireTapRouteBuilder.java
│       │   └── BaseRouteBuilder.java
│       ├── processor/
│       │   └── NoopProcessor.java
│       │   └── KibanaProcesor.java
│       ├── model/
│       │   └── RouteDef.java
│       └── service/
│           └── RouteService.java
│           └── RouteServiceImpl.java
└── src/
    └── main/
        └── resources/
            └── application.yml/
            └── route/
	            └── dev/
	                └── bos-dclear-route.json
	                └── dclear-bos-route.json
	                └── resend-bos-route.json
	            └── uat/
	                └── bos-dclear-route.json
	                └── dclear-bos-route.json
	                └── resend-bos-route.json
	            └── prd/
	                └── bos-dclear-route.json
	                └── dclear-bos-route.json
```




### What is this repository for? ###

* Service to setup camel routes
* 1.0.0

### How do I get set up? ###

* Summary of set up
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact



Building The Project
================
---
###Eclipse:
1. Before importing into eclipse execute the `mvn eclipse:eclipse` command to prepare the project to be able to 
be simply imported into the IDE.
2. Within the IDE, select `import existing eclipse project` option and as the eclipse:eclipse command will have
prepared **.project** and **.classpath** files, this will mean minimal setup required.
3. Add the integration-test src and resources to your classpath to ensure your can easily execute integration tests from the IDE.

###Compiling
The project has been designed to be built via `maven` the pom has been configured to be able to execute unit and
integration tests, generate code coverage reports, and code checkstyle testing.

Four Test Profiles have been configured to aid in building the project depending on what you wish to achieve:

1. `unit-tests` will only execute unit tests.
2. `integration-tests` will only execute integration tests.
3. `all-tests` both unit and integration tests are run.
4. `no-tests` no tests run.

The default profile is __all-tests__.

Some example executions are as followed.
* mvn _clean test_ => Creates code coverage report for unit tests.
* mvn _clean verify -P unit-tests_ => Creates code coverage report for unit tests and fails if does not meet coverage ratios.
* mvn _clean verify -P integration-tests_ => Creates code coverage report for unit tests and fails if does not meet coverage ratios. 
* mvn _clean verify -P all-tests_ => Creates code coverage reports for unit and integration tests and fails if does not meet coverage ratios).
* mvn _clean package -P no-tests_ => Does not execute tests and creates the distributable.
* mvn _clean verify -P all-tests checkstyle:check_ => runs all tests, checkstyle and packages.	

Before submitting your code to GIT run __clean verify -P all-tests checkstyle:check__ and build successfully to ensure that all tests and code style checks are completed to ensure Continuous Integration build errors do not arise.

Test Profiles
=============
no-tests - all tests are ignored
all-tests - all tests are executed
unit-tests - only unit tests are executed
integration-tests - only integration tests are executed
      