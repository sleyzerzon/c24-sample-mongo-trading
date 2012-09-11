package biz.c24.io.mongodb.fix.application;

import biz.c24.io.api.data.ComplexDataObject;
import biz.c24.io.api.data.ValidationException;
import biz.c24.io.api.presentation.FIXSource;
import biz.c24.io.fix42.NewOrderSingleElement;
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
 * Data loader for persisting FIX messages to MongoDB
 */
public class NewOrderSingleDataLoader extends AbstractDataLoader {

    public static final String NEW_ORDER_SINGLE_MESSAGE_VALIDATION_FAIL = "NewOrderSingle message validation fail.";
    public static final String ERROR_LOADING_NEW_ORDER_SINGLE_MESSAGES = "Error loading NewOrderSingle messages.";
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
        C24ParseTemplate<NewOrderSingleElement, FIXSource> c24Template = getC24NewOrderSingleParseTemplate();
        Assert.state(mongoTemplate != null);
        Assert.state(c24Template != null);

        try {
            File newOrderSingleSeriesFile = FileUtils.readClasspathResourceAsFile(FIX_NEW_ORDER_SERIES);
            BufferedReader reader = new BufferedReader(new FileReader(newOrderSingleSeriesFile));
            String rawMessage;
            while ((rawMessage = reader.readLine()) != null) {
                ComplexDataObject newOrderSingle = c24Template.bind(rawMessage);
                c24Template.validateByException(newOrderSingle);
                mongoTemplate.save(c24Template.asMongoDBObject(newOrderSingle), COLLECTION_NAME);
            }
        } 
        catch (IOException e) {
            LOGGER.error(ERROR_LOADING_NEW_ORDER_SINGLE_MESSAGES, e);
            throw new RuntimeException(ERROR_LOADING_NEW_ORDER_SINGLE_MESSAGES, e);
        } 
        catch (ValidationException e) {
            LOGGER.error(NEW_ORDER_SINGLE_MESSAGE_VALIDATION_FAIL, e);
            throw new RuntimeException(NEW_ORDER_SINGLE_MESSAGE_VALIDATION_FAIL, e);
        }
    }
}
