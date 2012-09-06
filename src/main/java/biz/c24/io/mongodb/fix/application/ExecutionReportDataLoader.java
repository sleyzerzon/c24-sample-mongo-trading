package biz.c24.io.mongodb.fix.application;

import biz.c24.io.api.data.ComplexDataObject;
import biz.c24.io.api.data.ValidationManager;
import biz.c24.io.api.presentation.FIXSource;
import biz.c24.io.fix42.ExecutionReportElement;
import biz.c24.io.mongodb.fix.C24ParseTemplate;
import biz.c24.io.mongodb.fix.impl.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ExecutionReportDataLoader extends AbstractDataLoader     {

    private final Logger LOGGER = LoggerFactory.getLogger(ExecutionReportDataLoader.class);
    private final String FIX_NEW_ORDER_SERIES = "/data-fixture/execution-report/execution-report-series-01.dat";
    public static final String COLLECTION_NAME = "ExecutionReport";

    public static void main(String[] args) {
        new ExecutionReportDataLoader().run();
    }

    private void run() {
        LOGGER.info("Starting application {}.", getClass());
        loadSpringContainer();
        createExecutionReports();
        LOGGER.info("Terminating application {}.", getClass());
    }

    private void createExecutionReports() {
        MongoTemplate mongoTemplate = getMongoTemplate();
        C24ParseTemplate<ExecutionReportElement, FIXSource> c24Template
                = getC24ExecutionReportParseTemplate();
        ValidationManager validationManager = getC24ValidationManager();

        try {
            File newOrderSingleSeriesFile = FileUtils.readClasspathResourceAsFile(FIX_NEW_ORDER_SERIES);
            BufferedReader reader = new BufferedReader(new FileReader(newOrderSingleSeriesFile));
            String rawMessage;
            while ((rawMessage = reader.readLine()) != null) {
                ComplexDataObject executionReport = c24Template.bind(rawMessage);
                validationManager.validateByException(executionReport);
                mongoTemplate.save(c24Template.asMongoDBObject(executionReport), COLLECTION_NAME);
            }
        } catch (Exception e) {
            LOGGER.error("Error loading ExecutionReport messages.", e);
        }
    }
}
