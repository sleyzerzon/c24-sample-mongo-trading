package biz.c24.io.mongodb.fix.impl;

import biz.c24.io.api.ParserException;
import biz.c24.io.api.data.ComplexDataObject;
import biz.c24.io.api.data.Element;
import biz.c24.io.api.presentation.JsonSink;
import biz.c24.io.api.presentation.Source;
import biz.c24.io.api.presentation.XMLSink;
import biz.c24.io.mongodb.fix.C24MessageParser;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static biz.c24.io.mongodb.fix.impl.C24StandardMessages.MANDATORY_ARGUMENT_MISSING;

/**
 * Produced on behalf of C24 Technologies Ltd.
 * 
 * @author Matt Vickery - matt.vickery@incept5.com
 * @since 31/08/2012
 */
public class C24MessageParserImpl<E extends Element, S extends Source>
        implements C24MessageParser<E, S> {

    private S source;
    private E element;
    private static JsonSink jsonSink;
    private static XMLSink xmlSink;

    public C24MessageParserImpl(S source, E element) {
        Assert.notNull(source, MANDATORY_ARGUMENT_MISSING);
        this.source = source;
        this.element = element;
    }

    public ComplexDataObject bind(String message) throws ParserException {

        Assert.isTrue(StringUtils.isNotEmpty(message));
        try {
            source.setReader(new StringReader(message));
            return source.readObject(element);
        } catch (IOException e) {
            throw new ParserException(e, message);
        }
    }

    public String asJson(ComplexDataObject complexDataObject) {

        Assert.notNull(complexDataObject, MANDATORY_ARGUMENT_MISSING);
        try {
            if (jsonSink == null)
                jsonSink = new JsonSink();
            Writer writer = new StringWriter();
            jsonSink.setWriter(writer);
            jsonSink.writeObject(complexDataObject);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String asXML(ComplexDataObject complexDataObject) {

        Assert.notNull(complexDataObject, MANDATORY_ARGUMENT_MISSING);
        try {
            if (xmlSink == null)
                xmlSink = new XMLSink();
            Writer writer = new StringWriter();
            xmlSink.setWriter(writer);
            xmlSink.writeObject(complexDataObject);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DBObject asMongoDBObject(ComplexDataObject complexDataObject) {
        
        Assert.notNull(complexDataObject, MANDATORY_ARGUMENT_MISSING);
        return (DBObject) JSON.parse(asJson(complexDataObject));
    }
}