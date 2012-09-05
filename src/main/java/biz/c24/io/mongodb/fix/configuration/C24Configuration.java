package biz.c24.io.mongodb.fix.configuration;

import biz.c24.io.api.data.ValidationManager;
import biz.c24.io.api.presentation.FIXSource;
import biz.c24.io.fix42.ExecutionReportElement;
import biz.c24.io.fix42.NewOrderSingleElement;
import biz.c24.io.mongodb.fix.C24ParseTemplate;
import biz.c24.io.mongodb.fix.impl.C24ParseTemplateImpl;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * Produced on behalf of C24 Technologies Ltd.
 * 
 * @author Matt Vickery - matt.vickery@incept5.com
 * @since 31/08/2012
 */
@Configuration
public class C24Configuration {

    @Bean
    public static PropertyPlaceholderConfigurer properties() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        final org.springframework.core.io.Resource[] resources 
                = new ClassPathResource[]{
                new ClassPathResource("/META-INF/spring/mongoDb.properties")
        };
        ppc.setLocations(resources);
        return ppc;
    }

    @Bean(name = "fixParser")
    public FIXSource getFixParser() {

        FIXSource fixSource = new FIXSource();

        fixSource.setEndOfDataRequired(true);
        fixSource.setAllowFieldsOutOfOrder(true);
        fixSource.setAllowNoData(true);
        fixSource.setIgnoreRepeatingGroupOrder(true);
        fixSource.setIgnoreUnknownFields(true);

        return fixSource;
    }

    @Bean(name = "newOrderSingleElement")
    public NewOrderSingleElement getNewOrderSingleElement() {
        return new NewOrderSingleElement();
    }

    @Bean(name = "tradeCaptureReportElement")
    public ExecutionReportElement getExecutionReportElement() {
        return new ExecutionReportElement();
    }

    @Bean(name = "c24ValidationManager")
    public ValidationManager getC24ValidationManager() {
        return new ValidationManager();
    }

    @Bean(name = "c24NewOrderSingleParseTemplate")
    public C24ParseTemplate<NewOrderSingleElement, FIXSource> 
        getC24NewOrderSingleParseTemplate() {
        return new C24ParseTemplateImpl<NewOrderSingleElement, 
                                FIXSource>(getFixParser(), getNewOrderSingleElement());
    }

    @Bean(name = "c24ExecutionReportParseTemplate")
    public C24ParseTemplate<ExecutionReportElement, FIXSource> 
        getC24ExecutionReportParseTemplate() {
        return new C24ParseTemplateImpl<ExecutionReportElement, 
                                FIXSource>(getFixParser(), getExecutionReportElement());
    }
}