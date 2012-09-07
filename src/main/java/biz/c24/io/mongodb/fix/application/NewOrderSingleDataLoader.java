package biz.c24.io.mongodb.fix.application;

import biz.c24.io.api.data.ComplexDataObject;
import biz.c24.io.api.data.ValidationManager;
import biz.c24.io.api.presentation.FIXSource;
import biz.c24.io.fix42.NewOrderSingleElement;
import biz.c24.io.mongodb.fix.C24ParseTemplate;
import biz.c24.io.mongodb.fix.impl.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Data loader for persisting FIX messages to MongoDB
 */
public class NewOrderSingleDataLoader extends AbstractDataLoader {

    private final Logger LOGGER = LoggerFactory.getLogger(NewOrderSingleDataLoader.class);
    private final String FIX_NEW_ORDER_SERIES = "/data-fixture/new-order-single/new-order-series-01.dat";
    public static final String COLLECTION_NAME = "NewOrderSingle";

    public static void main(String[] args) {
        new NewOrderSingleDataLoader().run();
    }

    private void run() {
        LOGGER.info("Starting application {}.", getClass());
        loadSpringContainer();
        createNewOrders();
        LOGGER.info("Terminating application {}.", getClass());
    }

    private void createNewOrders() {
        MongoTemplate mongoTemplate = getMongoTemplate();
        C24ParseTemplate<NewOrderSingleElement, FIXSource> c24Template
                = getC24NewOrderSingleParseTemplate();
        ValidationManager validationManager = getC24ValidationManager();

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
