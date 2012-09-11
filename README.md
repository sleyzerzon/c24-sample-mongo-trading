c24-sample-mongo-trading
========================

A C24 sample project that demonstrates a synergy between C24s iO product and the mongoDB document database.

Sample Project Scenario
=======================

The essence of the sample project scenario is that a client of a broking firm places orders and those orders are then 
filled by the broker. Each client order (NewOrderSingle) may give rise to one or more execution reports 
(ExecutionReport); orders can be filled with a single execution or multiple individual executions.

Technical Overview
==================
C24 iO binds each raw FIX (string) message to a C24 Java FIX object. These C24 Java FIX objects are then converted into 
mongoDB documents and saved into a mongoDB database. The ExecutionReports that are loaded represent simulated processing 
for the collection of the NewOrderSingle messages. The core driver for this sample was to demonstrate financial messages 
being parsed by C24, saved to a mongoDB database and then queried using mongoDB query facilities.

Project Distribution
====================
The sample project has been distributed in two forms, you've already located the source. The first distribution is for 
Internet-enabled environments. Cloning the Github project and running the usual 'mvn clean test' will download 
dependencies, compile all of the application code and run the integration test classes. 

The second distribution provides the sample project as a package that can be run in a non-Internet enabled environment. 
The package is distributed as a zip file (http://dev.c24.biz/io_mongodb/samples/c24-sample-mongo-trading-1.0-project.zip) that needs to be unpacked and run using the supplied ant build file or shell 
script. The ant build file contains several interesting targets, clean, compile, createNewOrders and 
createExecutionReports. Each of these needs to be run in turn in order to populate the database with data necessary for 
the queries to be executed. The default target invokes all targets in the correct order automatically and so running 
'ant' in the root directory of the project will complete the task. Running the shell script './run.sh' will also achieve 
the same result.

Dependencies
============
You will need a running mongoDB database, see the next section for configuration details. The C24 generated jar file 
includes a reference implementation licence for the Fix 4.2 message processing.

Configuration
=============
Whichever project is executed, a mongoDB database must be running and available for service. The directory 
src/main/java/resources contains a database configuration file named mongoDB.properties. Connection parameters for the 
mongoDB database that you plan to use need to be configured in that file. Although you would always use authentication 
credentials in a production system, none are necessary for this sample. All that is required is the server (host) name, 
database name and port number.

Test Data
=========
In order to support that scenario, a set of FIX NewOrderSingle and ExecutionReport messages, which were generated from 
a front office trading simulator, will be saved into a mongoDB database. All of the messages used in this sample are 
provided as static data and are contained on two files that can be found in the sample project source 
(src/main/java/resources).

ApplicationCode
===============
In the package biz.c24.io.mongodb.fix.application you can find two classes - CreateNewOrderSingles and 
CreateExecutionReports. These can be run from within an IDE environment if the Github repository is cloned, or with the 
ant build file (build.xml) as default targets or with the supplied shell script of the zip packaged distribution.

Test Code
=========
The NewOrderSingle and ExecutionReport test messages can also be run by invoking two supplied integration tests that can
be found in the biz.c24.io.mongodb.fix package of src/test/java; NewOrderSingleIntegrationTest and 
ExecutionReportIntegrationTest. Both of these tests are automatically invoked for user that run 'mvn clean test' on this
cloned source repository.