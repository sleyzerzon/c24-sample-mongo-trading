package biz.c24.io.mongodb.fix;

import biz.c24.io.api.data.ComplexDataObject;
import biz.c24.io.api.presentation.FIXSource;
import biz.c24.io.fix42.ExecutionReportElement;
import biz.c24.io.mongodb.fix.configuration.C24Configuration;
import biz.c24.io.mongodb.fix.configuration.MongoDbConfiguration;
import biz.c24.io.mongodb.fix.impl.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Produced on behalf of C24 Technologies Ltd.
 * 
 * @author Matt Vickery - matt.vickery@incept5.com
 * @since 31/08/2012
 */
@ContextConfiguration(classes = {C24Configuration.class, MongoDbConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ExecutionReportIntegrationTest {

    public static final String COLLECTION_NAME = "ExecutionReport";
    private final String FIX_EXECUTION_REPORT_SINGLE_1 = "/data-fixture/execution-report/execution-report-single-01.dat";
    private final String FIX_EXECUTION_REPORT_SERIES = "/data-fixture/execution-report/execution-report-series-01.dat";

    @Autowired
    private C24ParseAdapter<ExecutionReportElement, FIXSource> c24ExecutionReportParseAdapter;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void dropCollection() throws Exception {
        mongoTemplate.dropCollection(COLLECTION_NAME);
    }
    
    @Test
    public void insertExecutionReport() throws Exception {
        String rawMessage = FileUtils.readClasspathResourceAsString(FIX_EXECUTION_REPORT_SINGLE_1);
        ComplexDataObject executionReport = c24ExecutionReportParseAdapter.bind(rawMessage);
        mongoTemplate.save(c24ExecutionReportParseAdapter.asMongoDBObject(executionReport), COLLECTION_NAME);
    }

    @Test
    public void insertExecutionReportSeries() throws Exception {

        File newOrderSingleSeriesFile = FileUtils.readClasspathResourceAsFile(FIX_EXECUTION_REPORT_SERIES);
        BufferedReader reader = new BufferedReader(new FileReader(newOrderSingleSeriesFile));
        String rawMessage;
        while ((rawMessage = reader.readLine()) != null) {
            ComplexDataObject executionReport = c24ExecutionReportParseAdapter.bind(rawMessage);
            mongoTemplate.save(c24ExecutionReportParseAdapter.asMongoDBObject(executionReport), COLLECTION_NAME);
        }
    }
}