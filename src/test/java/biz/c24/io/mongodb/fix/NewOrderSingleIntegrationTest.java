package biz.c24.io.mongodb.fix;

import biz.c24.io.api.data.ComplexDataObject;
import biz.c24.io.api.presentation.FIXSource;
import biz.c24.io.fix42.NewOrderSingleElement;
import biz.c24.io.mongodb.fix.configuration.C24Configuration;
import biz.c24.io.mongodb.fix.configuration.MongoDbConfiguration;
import biz.c24.io.mongodb.fix.impl.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
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
public class NewOrderSingleIntegrationTest {

    public static final String COLLECTION_NAME = "NewOrderSingle";
    private final String FIX_NEW_ORDER_SINGLE_1 = "/data-fixture/new-order-single/new-order-single-01.dat";
    private final String FIX_NEW_ORDER_SERIES = "/data-fixture/new-order-single/new-order-series-01.dat";
    
    @Resource(name = "c24NewOrderSingleParseTemplate")
    private C24ParseTemplate<NewOrderSingleElement, FIXSource> c24Template;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void dropCollection() throws Exception {
        mongoTemplate.dropCollection(COLLECTION_NAME);
    }

    @Test
    public void insertSingleOrder() throws Exception {

        String rawMessage = FileUtils.readClasspathResourceAsString(FIX_NEW_ORDER_SINGLE_1);
        ComplexDataObject newOrderSingle = c24Template.bind(rawMessage);
        mongoTemplate.save(c24Template.asMongoDBObject(newOrderSingle), COLLECTION_NAME);
    }

    @Test
    public void insertSingleOrderSeries() throws Exception {

        File newOrderSingleSeriesFile = FileUtils.readClasspathResourceAsFile(FIX_NEW_ORDER_SERIES);
        BufferedReader reader = new BufferedReader(new FileReader(newOrderSingleSeriesFile));
        String rawMessage;
        while ((rawMessage = reader.readLine()) != null) {
            ComplexDataObject newOrderSingle = c24Template.bind(rawMessage);
            mongoTemplate.save(c24Template.asMongoDBObject(newOrderSingle), COLLECTION_NAME);
        }
    }
}