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

package com.eischet.ews.api.messaging;

import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.XmlAttributeNames;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.property.complex.ComplexProperty;

/**
 * Represents the Id of a phone call.
 */
public final class PhoneCallId extends ComplexProperty {

    /**
     * The id.
     */
    private String id;

    /**
     * Initializes a new instance of the PhoneCallId class.
     */
    public PhoneCallId() {
    }

    /**
     * Initializes a new instance of the PhoneCallId class.
     *
     * @param id the id
     */
    protected PhoneCallId(String id) {
        this.id = id;
    }

    /**
     * Reads attribute from XML.
     *
     * @param reader the reader
     */
    @Override
    public void readAttributesFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        this.id = reader.readAttributeValue(XmlAttributeNames.Id);
    }

    /**
     * Writes attribute to XML.
     *
     * @param writer the writer
     */
    @Override
    public void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeAttributeValue(XmlAttributeNames.Id, this.id);
    }

    /**
     * Writes to XML.
     *
     * @param writer the writer
     * @throws Exception the exception
     */
    protected void writeToXml(EwsServiceXmlWriter writer) throws Exception {
        this.writeToXml(writer, XmlElementNames.PhoneCallId);
    }

    /**
     * Gets the Id of the phone call.
     *
     * @return the id
     */
    protected String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    protected void setId(String id) {
        this.id = id;
    }

}
