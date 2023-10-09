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

package com.eischet.ews.api.property.complex;

import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.XmlAttributeNames;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

/**
 * Defines the EwsXmlReader class.
 */
public final class InternetMessageHeader extends ComplexProperty {

    /**
     * The name.
     */
    private String name;

    /**
     * The value.
     */
    private String value;

    /**
     * Initializes a new instance of the EwsXmlReader class.
     */
    protected InternetMessageHeader() {
    }

    /**
     * Reads the attribute from XML.
     *
     * @param reader the reader
     */
    public void readAttributesFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        this.name = reader.readAttributeValue(XmlAttributeNames.HeaderName);
    }

    /**
     * Reads the text value from XML.
     *
     * @param reader the reader
     */
    public void readTextValueFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        this.value = reader.readValue();
    }

    /**
     * Writes the attribute to XML.
     *
     * @param writer the writer
     */
    public void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeAttributeValue(XmlAttributeNames.HeaderName, this.name);
    }

    /**
     * Writes elements to XML.
     *
     * @param writer the writer
     */
    public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeValue(this.value, this.name);
    }

    /**
     * Obtains a string representation of the header.
     *
     * @return The string representation of the header.
     */
    public String toString() {
        return String.format("%s=%s", this.name, this.value);
    }

    /**
     * The name of the header.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * The value of the header.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
