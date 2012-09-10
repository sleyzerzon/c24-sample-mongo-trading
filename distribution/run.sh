#!/bin/sh

mkdir -p build
javac -sourcepath src -classpath 'dependencies/*' -d build src/biz/c24/io/mongodb/fix/application/NewOrderSingleDataLoader.java
javac -sourcepath src -classpath 'dependencies/*' -d build src/biz/c24/io/mongodb/fix/application/ExecutionReportDataLoader.java
java -classpath 'build:src:dependencies/*' biz.c24.io.mongodb.fix.application.NewOrderSingleDataLoader
java -classpath 'build:src:dependencies/*' biz.c24.io.mongodb.fix.application.ExecutionReportDataLoader
