package biz.c24.io.mongodb.fix.configuration;

import com.mongodb.Mongo;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.net.UnknownHostException;

/**
 * Produced on behalf of C24 Technologies Ltd.
 * 
 * @author Matt Vickery - matt.vickery@incept5.com
 * @since 31/08/2012
 */
@Import({C24Configuration.class})
public class MongoDbConfiguration {

    @NotNull
    @Value("${mongodb.database}")
    private String database;

    @NotNull
    @Value("${mongodb.port}")
    private int port;

    @NotNull
    @Value("${mongodb.server}")
    private String server;

    @Bean(name = "mongoDbTemplate")
    public MongoTemplate getMongoTemplate() {
        return new MongoTemplate(getMongoDbFactory());
    }

    @Bean
    public MongoDbFactory getMongoDbFactory() {
        return new SimpleMongoDbFactory(getMongoDb(), 
                database);
    }

    @Bean
    public Mongo getMongoDb() {
        try {
            return new Mongo(server, port);
        } catch (UnknownHostException e) {
            throw new BeanCreationException("Error creating mongo bean.", e);
        }
    }
}