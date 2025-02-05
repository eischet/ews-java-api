/*
 * The MIT License
 * Copyright (c) 2012 Microsoft Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.eischet.ews.api.property.definition;

import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.PropertyBag;
import com.eischet.ews.api.core.enumeration.misc.ExchangeVersion;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.enumeration.property.PropertyDefinitionFlags;
import com.eischet.ews.api.core.exception.service.local.ServiceLocalException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

import javax.xml.stream.XMLStreamException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.EnumSet;

/**
 * Represents typed property definition.
 */
abstract class TypedPropertyDefinition<T extends Serializable> extends PropertyDefinition {

    /**
     * The is nullable.
     */
    private boolean isNullable;

    /**
     * Initializes a new instance.
     *
     * @param xmlElementName Name of the XML element.
     * @param uri            The URI.
     * @param version        The version.
     */
    protected TypedPropertyDefinition(String xmlElementName, String uri,
                                      ExchangeVersion version) {
        super(xmlElementName, uri, version);
        this.isNullable = false;
    }

    /**
     * Initializes a new instance.
     *
     * @param xmlElementName Name of the XML element.
     * @param uri            The URI.
     * @param flags          The flags.
     * @param version        The version.
     */
    protected TypedPropertyDefinition(String xmlElementName, String uri,
                                      EnumSet<PropertyDefinitionFlags> flags, ExchangeVersion version) {
        super(xmlElementName, uri, flags, version);
    }

    /**
     * Initializes a new instance.
     *
     * @param xmlElementName Name of the XML element.
     * @param uri            The URI.
     * @param flags          The flags.
     * @param version        The version.
     * @param isNullable     Indicates that this property definition is for a nullable
     *                       property.
     */
    protected TypedPropertyDefinition(String xmlElementName, String uri,
                                      EnumSet<PropertyDefinitionFlags> flags, ExchangeVersion version,
                                      boolean isNullable) {
        super(xmlElementName, uri, flags, version);
        this.isNullable = isNullable;
    }

    /**
     * Parses the specified value.
     *
     * @param value The value.
     * @return Typed value.
     */
    protected abstract T parse(String value) throws ExchangeXmlException;

    /**
     * Gets a value indicating whether this property definition is for a
     * nullable type.
     *
     * @return always true
     */
    @Override
    public boolean isNullable() {
        return this.isNullable;
    }

    /**
     * Convert instance to string.
     *
     * @param value The value.
     * @return String representation of property value.
     */
    protected String toString(T value) {
        return value.toString();
    }

    /**
     * Loads from XML.
     *
     * @param reader      The reader.
     * @param propertyBag The property bag.
     */
    @Override
    public void loadPropertyValueFromXml(EwsServiceXmlReader reader, PropertyBag propertyBag) throws ExchangeXmlException {
        String value = reader.readElementValue(XmlNamespace.Types, this.getXmlElement());

        if (value != null && !value.isEmpty()) {
            propertyBag.setObjectFromPropertyDefinition(this, this.parse(value));
        }
    }

    /**
     * Writes the property value to XML.
     *
     * @param writer            The writer.
     * @param propertyBag       The property bag.
     * @param isUpdateOperation Indicates whether the context is an update operation.
     */
    @Override
    public void writePropertyValueToXml(EwsServiceXmlWriter writer, PropertyBag propertyBag,
                                        boolean isUpdateOperation) throws ExchangeXmlException {
        T value = propertyBag.getObjectFromPropertyDefinition(this);
        if (value != null) {
            writer.writeElementValue(XmlNamespace.Types, this.getXmlElement(), this.getName(), value);
        }

    }
}
