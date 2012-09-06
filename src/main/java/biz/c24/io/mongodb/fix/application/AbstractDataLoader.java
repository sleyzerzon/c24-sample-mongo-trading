package biz.c24.io.mongodb.fix.application;

import biz.c24.io.api.data.ValidationManager;
import biz.c24.io.api.presentation.FIXSource;
import biz.c24.io.fix42.ExecutionReportElement;
import biz.c24.io.fix42.NewOrderSingleElement;
import biz.c24.io.mongodb.fix.C24ParseTemplate;
import biz.c24.io.mongodb.fix.configuration.C24Configuration;
import biz.c24.io.mongodb.fix.configuration.MongoDbConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Generated on behalf of C24 Technologies Ltd.
 *
 * @version 1.0
 * @author: Iain Porter iain.porter@c24.biz
 * @since 06/09/2012
 */
public class AbstractDataLoader {

    private AnnotationConfigApplicationContext applicationContext;

    protected void loadSpringContainer() {
        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(MongoDbConfiguration.class);
        applicationContext.refresh();
    }

    protected MongoTemplate getMongoTemplate() {
        return getMongoDbConfiguration().getMongoTemplate();
    }

    protected C24ParseTemplate<ExecutionReportElement, FIXSource> getC24ExecutionReportParseTemplate() {
        return getC24Configuration().getC24ExecutionReportParseTemplate();
    }

    protected C24ParseTemplate<NewOrderSingleElement, FIXSource> getC24NewOrderSingleParseTemplate() {
        return getC24Configuration().getC24NewOrderSingleParseTemplate();
    }

    protected ValidationManager getC24ValidationManager() {
        return getC24Configuration().getC24ValidationManager();
    }

    private C24Configuration getC24Configuration() {
        return applicationContext.getBean(C24Configuration.class);
    }

    private MongoDbConfiguration getMongoDbConfiguration() {
        return applicationContext.getBean(MongoDbConfiguration.class);
    }
}
