package biz.c24.io.mongodb.fix;

import biz.c24.io.api.ParserException;
import biz.c24.io.api.data.ComplexDataObject;
import biz.c24.io.api.data.Element;
import biz.c24.io.api.presentation.Source;

/**
 * Produced on behalf of C24 Technologies Ltd.
 * 
 * @author Matt Vickery - matt.vickery@incept5.com
 * @since 31/08/2012
 */
public interface C24ParseTemplate<E extends Element, S extends Source> extends C24MessageFormatEmitter {
    public ComplexDataObject bind(String message) throws ParserException;
}