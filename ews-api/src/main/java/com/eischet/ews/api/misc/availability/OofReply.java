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

package com.eischet.ews.api.misc.availability;

import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.EwsUtilities;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

import javax.xml.stream.XMLStreamException;

/**
 * Represents an Out of Office response.
 */
public final class OofReply {

    /**
     * The culture.
     */
    private String culture = "en-US";

    /**
     * The message.
     */
    private String message;

    /**
     * Writes an empty OofReply to XML.
     *
     * @param writer         the writer
     * @param xmlElementName the xml element name
     */
    public static void writeEmptyReplyToXml(EwsServiceXmlWriter writer, String xmlElementName) throws ExchangeXmlException {
        writer.writeStartElement(XmlNamespace.Types, xmlElementName);
        writer.writeEndElement(); // xmlElementName
    }

    /**
     * Initializes a new instance of the class.
     */
    public OofReply() {
    }

    /**
     * Initializes a new instance of the class.
     *
     * @param message the message
     */
    public OofReply(String message) {
        this.message = message;
    }

    /**
     * Initializes a new instance of the class.
     *
     * @param message the message
     * @return the oof reply from string
     */
    public static OofReply getOofReplyFromString(String message) {
        return new OofReply(message);
    }

    /**
     * Gets the string from oof reply.
     *
     * @param oofReply the oof reply
     * @return the string from oof reply
     * @throws Exception the exception
     */
    public static String getStringFromOofReply(OofReply oofReply)
            throws Exception {
        EwsUtilities.validateParam(oofReply, "oofReply");
        return oofReply.message;
    }

    /**
     * Loads from XML.
     *
     * @param reader         the reader
     * @param xmlElementName the xml element name
     * @throws Exception the exception
     */
    public void loadFromXml(EwsServiceXmlReader reader, String xmlElementName) throws ExchangeXmlException {
        reader.ensureCurrentNodeIsStartElement(XmlNamespace.Types, xmlElementName);

        if (reader.hasAttributes()) {
            this.setCulture(reader.readAttributeValue("xml:lang"));
        }

        this.message = reader.readElementValue(XmlNamespace.Types, XmlElementNames.Message);

        reader.readEndElement(XmlNamespace.Types, xmlElementName);
    }

    /**
     * Writes to XML.
     *
     * @param writer         the writer
     * @param xmlElementName the xml element name
     */
    public void writeToXml(EwsServiceXmlWriter writer, String xmlElementName) throws ExchangeXmlException {
        writer.writeStartElement(XmlNamespace.Types, xmlElementName);

        if (this.culture != null) {
            writer.writeAttributeValue("xml", "lang", this.culture);
        }

        writer.writeElementValue(XmlNamespace.Types, XmlElementNames.Message,
                this.message);

        writer.writeEndElement(); // xmlElementName
    }

    /**
     * Obtains a string representation of the reply.
     *
     * @return A string containing the reply message.
     */
    public String toString() {
        return this.message;
    }

    /**
     * Gets the culture of the reply.
     *
     * @return the culture
     */
    public String getCulture() {
        return this.culture;

    }

    /**
     * Sets the culture.
     *
     * @param culture the new culture
     */
    public void setCulture(String culture) {
        this.culture = culture;
    }

    /**
     * Gets  the the reply message.
     *
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
