package biz.c24.io.mongodb.fix.application;

import biz.c24.io.api.data.ComplexDataObject;
import biz.c24.io.api.data.ValidationException;
import biz.c24.io.api.presentation.FIXSource;
import biz.c24.io.fix42.ExecutionReportElement;
import biz.c24.io.mongodb.fix.C24ParseTemplate;
import biz.c24.io.mongodb.fix.impl.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class for loading ExecutionReport FIX messages and persisting them to a Mongo DB
 *
 */
public class ExecutionReportDataLoader extends AbstractDataLoader     {

    public static final String ERROR_LOADING_EXECUTION_REPORT_MESSAGES = "Error loading ExecutionReport messages.";
    public static final String EXECUTION_REPORT_MESSAGE_VALIDATION_FAIL = "ExecutionReport message validation fail.";
    private final Logger LOGGER = LoggerFactory.getLogger(ExecutionReportDataLoader.class);

    /**
     * The data file to load
     */
    private final String FIX_NEW_ORDER_SERIES = "/data-fixture/execution-report/execution-report-series-01.dat";

    /**
     * The name of the collection to store the Execution Report objects in Mongo
     */
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

    /**
     * Read each line in the data file and parse, validate, transform and persist
     */
    private void createExecutionReports() {
        
        MongoTemplate mongoTemplate = getMongoTemplate();
        C24ParseTemplate<ExecutionReportElement, FIXSource> c24Template = getC24ExecutionReportParseTemplate();
        Assert.state(mongoTemplate != null);
        Assert.state(c24Template != null);

        try {
            File newOrderSingleSeriesFile = FileUtils.readClasspathResourceAsFile(FIX_NEW_ORDER_SERIES);
            BufferedReader reader = new BufferedReader(new FileReader(newOrderSingleSeriesFile));
            String rawMessage;
            while ((rawMessage = reader.readLine()) != null) {
                ComplexDataObject executionReport = c24Template.bind(rawMessage);
                c24Template.validateByException(executionReport);
                mongoTemplate.save(c24Template.asMongoDBObject(executionReport), COLLECTION_NAME);
            }
        } 
        catch (IOException e) {
            LOGGER.error(ERROR_LOADING_EXECUTION_REPORT_MESSAGES, e);
            throw new RuntimeException(ERROR_LOADING_EXECUTION_REPORT_MESSAGES, e);
        } 
        catch (ValidationException e) {
            LOGGER.error(EXECUTION_REPORT_MESSAGE_VALIDATION_FAIL);
            throw new RuntimeException(EXECUTION_REPORT_MESSAGE_VALIDATION_FAIL, e);
        }
    }
}
