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

import com.eischet.ews.api.core.*;
import com.eischet.ews.api.core.enumeration.property.BodyType;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

/**
 * Represents the body part of an item that is unique to the conversation the
 * item is part of.
 */
public final class UniqueBody extends ComplexProperty {

    /**
     * The body type.
     */
    private BodyType bodyType;

    /**
     * The text.
     */
    private String text;

    /**
     * Initializes a new instance.
     */
    public UniqueBody() {
    }

    /**
     * Defines an implicit conversion of UniqueBody into a string.
     *
     * @param messageBody the message body
     * @return string containing the text of the UniqueBody
     * @throws Exception the exception
     */
    public static String getStringFromUniqueBody(UniqueBody messageBody) throws Exception {
        EwsUtilities.validateParam(messageBody, "messageBody");
        return messageBody.text;
    }

    /**
     * Reads attribute from XML.
     *
     * @param reader the reader
     */
    public void readAttributesFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        this.bodyType = reader.readAttributeValue(BodyType.class, XmlAttributeNames.BodyType);
    }

    /**
     * Reads attribute from XML.
     *
     * @param reader the reader
     */
    public void readTextValueFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        this.text = reader.readValue();
    }

    /**
     * Writes attributes from XML.
     *
     * @param writer the writer
     */
    public void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeAttributeValue(XmlAttributeNames.BodyType, this.bodyType);
    }

    /**
     * Writes elements to XML.
     *
     * @param writer the writer
     */
    public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        if (!(this.text == null || this.text.isEmpty())) {
            writer.writeValue(this.text, XmlElementNames.UniqueBody);
        }
    }

    /**
     * Gets the type of the unique body's text.
     *
     * @return bodytype
     */
    public BodyType getBodyType() {
        return this.bodyType;
    }

    /**
     * Gets the text of the unique body.
     *
     * @return text
     */
    public String getText() {
        return this.text;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return (this.getText() == null) ? "" : this.getText();
    }

}
