package biz.c24.io.mongodb.fix.application;

import biz.c24.io.api.data.ComplexDataObject;
import biz.c24.io.api.presentation.FIXSource;
import biz.c24.io.fix42.ExecutionReportElement;
import biz.c24.io.mongodb.fix.C24MessageParser;
import biz.c24.io.mongodb.fix.configuration.MongoDbConfiguration;
import biz.c24.io.mongodb.fix.impl.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CreateExecutionReports {

    private final Logger LOGGER = LoggerFactory.getLogger(CreateExecutionReports.class);
    private AnnotationConfigApplicationContext applicationContext;
    private final String FIX_NEW_ORDER_SERIES = "/data-fixture/execution-report/execution-report-series-01.dat";
    public static final String COLLECTION_NAME = "ExecutionReport";

    public static void main(String[] args) {
        new CreateExecutionReports().run();
    }

    private void run() {
        LOGGER.info("Starting application {}.", getClass());
        loadSpringContainer();
        createExecutionReports();
        LOGGER.info("Terminating application {}.", getClass());
    }

    private void loadSpringContainer() {
        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(MongoDbConfiguration.class);
        applicationContext.refresh();
    }

    private void createExecutionReports() {
        Assert.state(applicationContext != null);
        MongoTemplate mongoTemplate = applicationContext.getBean(MongoTemplate.class);
        C24MessageParser<ExecutionReportElement, FIXSource> c24ExecutionReportMessageParser
                = (C24MessageParser<ExecutionReportElement, FIXSource>) applicationContext.getBean("c24ExecutionReportMessageParser");

        try {
            File newOrderSingleSeriesFile = FileUtils.readClasspathResourceAsFile(FIX_NEW_ORDER_SERIES);
            BufferedReader reader = new BufferedReader(new FileReader(newOrderSingleSeriesFile));
            String rawMessage;
            while ((rawMessage = reader.readLine()) != null) {
                ComplexDataObject newOrderSingle = c24ExecutionReportMessageParser.bind(rawMessage);
                mongoTemplate.save(c24ExecutionReportMessageParser.asMongoDBObject(newOrderSingle), COLLECTION_NAME);
            }
        } catch (Exception e) {
            LOGGER.error("Error loading NewOrderSingle messages.", e);
        }
    }
}
