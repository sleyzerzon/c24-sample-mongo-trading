package biz.c24.io.mongodb.fix;

import biz.c24.io.api.data.ComplexDataObject;
import com.mongodb.DBObject;

/**
 * Produced on behalf of C24 Technologies Ltd.
 * 
 * @author Matt Vickery - matt.vickery@incept5.com
 * @since 31/08/2012
 */
public interface C24MessageFormatEmitter {
    public String asJson(ComplexDataObject complexDataObject);
    public String asXML(ComplexDataObject complexDataObject);
    public DBObject asMongoDBObject(ComplexDataObject complexDataObject);
}
