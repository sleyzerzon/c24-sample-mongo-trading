package biz.c24.io.mongodb.fix.db;

import biz.c24.io.api.data.ComplexDataObject;

/**
 * Produced on behalf of C24 Technologies Ltd.
 * 
 * @author Matt Vickery - matt.vickery@incept5.com
 * @since 31/08/2012
 */
public interface MongoDbAdapter {
    public void save(String collection, String jsonDocument);
    public void save(String collection, ComplexDataObject complexDataObject);
    public void dropCollection(java.lang.String collectionName);
}
