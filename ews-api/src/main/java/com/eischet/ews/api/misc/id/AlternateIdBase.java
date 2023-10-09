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

package com.eischet.ews.api.misc.id;

import com.eischet.ews.api.ISelfValidate;
import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.XmlAttributeNames;
import com.eischet.ews.api.core.enumeration.misc.IdFormat;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.exception.service.local.ExchangeValidationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

/**
 * Represents the base class for Id expressed in a specific format.
 */
public abstract class AlternateIdBase implements ISelfValidate {

    /**
     * Id format.
     */
    private IdFormat format;

    /**
     * Initializes a new instance of the class.
     */
    protected AlternateIdBase() {
    }

    /**
     * Initializes a new instance of the class.
     *
     * @param format the format
     */
    protected AlternateIdBase(IdFormat format) {
        super();
        this.format = format;
    }

    /**
     * Gets the format in which the Id in expressed.
     *
     * @return the format
     */
    public IdFormat getFormat() {
        return this.format;
    }

    /**
     * Sets the format.
     *
     * @param format the new format
     */
    public void setFormat(IdFormat format) {
        this.format = format;
    }

    /**
     * Gets the name of the XML element.
     *
     * @return XML element name.
     */
    protected abstract String getXmlElementName();

    protected void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeAttributeValue(XmlAttributeNames.Format, this.getFormat());
    }

    public void loadAttributesFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        this.setFormat(reader.readAttributeValue(IdFormat.class, XmlAttributeNames.Format));
    }

    public void writeToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeStartElement(XmlNamespace.Types, this.getXmlElementName());
        this.writeAttributesToXml(writer);
        writer.writeEndElement(); // this.GetXmlElementName()
    }

    protected void internalValidate() throws ExchangeValidationException {
        // nothing to do.
    }

    public void validate() throws ExchangeValidationException {
        this.internalValidate();
    }

}
