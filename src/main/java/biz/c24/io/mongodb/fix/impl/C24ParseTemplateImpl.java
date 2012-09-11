package biz.c24.io.mongodb.fix.impl;

import biz.c24.io.api.ParserException;
import biz.c24.io.api.data.*;
import biz.c24.io.api.presentation.JsonSink;
import biz.c24.io.api.presentation.Source;
import biz.c24.io.api.presentation.XMLSink;
import biz.c24.io.mongodb.fix.C24ParseTemplate;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static biz.c24.io.mongodb.fix.impl.C24StandardMessages.MANDATORY_ARGUMENT_MISSING;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.state;

/**
 * Produced on behalf of C24 Technologies Ltd.
 *
 * @author Matt Vickery - matt.vickery@incept5.com
 * @since 31/08/2012
 */
public class C24ParseTemplateImpl<E extends Element, S extends Source>
        implements C24ParseTemplate<E, S> {

    private S source;
    private E element;
    private static JsonSink jsonSink = new JsonSink();
    private static XMLSink xmlSink = new XMLSink();

    public C24ParseTemplateImpl(S source, E element) {
        notNull(source, MANDATORY_ARGUMENT_MISSING);
        notNull(element, MANDATORY_ARGUMENT_MISSING);
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

    public void validateByException(ComplexDataObject complexDataObject) throws ValidationException {

        notNull(complexDataObject, MANDATORY_ARGUMENT_MISSING);
        
        new ValidationManager().validateByException(complexDataObject);
    }

    public List<ValidationEvent>[] validateByEvent(ComplexDataObject complexDataObject) {
        
        notNull(complexDataObject, MANDATORY_ARGUMENT_MISSING);
        
        ValidationManager validationManager = new ValidationManager();
        final List<ValidationEvent> validationPass = new ArrayList<ValidationEvent>();
        final List<ValidationEvent> validationFail = new ArrayList<ValidationEvent>();
        
        validationManager.addValidationListener(new ValidationListener() {
            public void validationPassed(ValidationEvent validationEvent) {
                validationPass.add(validationEvent);
            }

            public void validationFailed(ValidationEvent validationEvent) {
                validationFail.add(validationEvent);
            }
        });
        validationManager.validateByEvents(complexDataObject);
        return (List<ValidationEvent>[]) new Object[]{validationPass, validationFail};
        
    }

    public String asJson(ComplexDataObject complexDataObject) {

        notNull(complexDataObject, MANDATORY_ARGUMENT_MISSING);
        state(jsonSink != null);
        try {
            Writer writer = new StringWriter();
            jsonSink.setWriter(writer);
            jsonSink.writeObject(complexDataObject);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String asXML(ComplexDataObject complexDataObject) {

        notNull(complexDataObject, MANDATORY_ARGUMENT_MISSING);
        state(xmlSink != null);
        
        try {
            Writer writer = new StringWriter();
            writer.flush();
            xmlSink.setWriter(writer);
            xmlSink.writeObject(complexDataObject);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DBObject asMongoDBObject(ComplexDataObject complexDataObject) {

        notNull(complexDataObject, MANDATORY_ARGUMENT_MISSING);
        return (DBObject) JSON.parse(asJson(complexDataObject));
    }
}