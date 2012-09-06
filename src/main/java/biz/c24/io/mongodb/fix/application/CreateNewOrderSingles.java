package biz.c24.io.mongodb.fix.application;

import biz.c24.io.api.data.ComplexDataObject;
import biz.c24.io.api.data.ValidationManager;
import biz.c24.io.api.presentation.FIXSource;
import biz.c24.io.fix42.NewOrderSingleElement;
import biz.c24.io.mongodb.fix.C24ParseTemplate;
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

public class CreateNewOrderSingles {

    private final Logger LOGGER = LoggerFactory.getLogger(CreateNewOrderSingles.class);
    private AnnotationConfigApplicationContext applicationContext;
    private final String FIX_NEW_ORDER_SERIES = "/data-fixture/new-order-single/new-order-series-01.dat";
    public static final String COLLECTION_NAME = "NewOrderSingle";

    public static void main(String[] args) {
        new CreateNewOrderSingles().run();
    }

    private void run() {
        LOGGER.info("Starting application {}.", getClass());
        loadSpringContainer();
        createNewOrders();
        LOGGER.info("Terminating application {}.", getClass());
    }

    private void loadSpringContainer() {
        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(MongoDbConfiguration.class);
        applicationContext.refresh();
    }

    private void createNewOrders() {
        Assert.state(applicationContext != null);
        MongoTemplate mongoTemplate = applicationContext.getBean(MongoTemplate.class);
        C24ParseTemplate<NewOrderSingleElement, FIXSource> c24Template
                = (C24ParseTemplate<NewOrderSingleElement, FIXSource>) applicationContext.getBean("c24NewOrderSingleParseTemplate");
        ValidationManager validationManager = (ValidationManager)applicationContext.getBean("c24ValidationManager");

        try {
            File newOrderSingleSeriesFile = FileUtils.readClasspathResourceAsFile(FIX_NEW_ORDER_SERIES);
            BufferedReader reader = new BufferedReader(new FileReader(newOrderSingleSeriesFile));
            String rawMessage;
            while ((rawMessage = reader.readLine()) != null) {
                ComplexDataObject newOrderSingle = c24Template.bind(rawMessage);
                validationManager.validateByException(newOrderSingle);
                mongoTemplate.save(c24Template.asMongoDBObject(newOrderSingle), COLLECTION_NAME);
            }
        } catch (Exception e) {
            LOGGER.error("Error loading NewOrderSingle messages.", e);
        }
    }
}
