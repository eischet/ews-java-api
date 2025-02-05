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
import com.eischet.ews.api.core.EwsUtilities;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.property.MeetingResponseType;
import com.eischet.ews.api.core.exception.service.local.ExchangeValidationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

import java.time.LocalDateTime;

/**
 * Represents an attendee to a meeting.
 */

public final class Attendee extends EmailAddress {

    /**
     * The response type.
     */
    private MeetingResponseType responseType;

    /**
     * The last response time.
     */
    private LocalDateTime lastResponseTime;

    /**
     * Initializes a new instance of the Attendee class.
     */
    public Attendee() {
        super();
    }

    /**
     * Initializes a new instance of the Attendee class.
     *
     * @param smtpAddress the smtp address
     * @throws Exception the exception
     */
    public Attendee(String smtpAddress) throws ExchangeValidationException {
        super(smtpAddress);
        EwsUtilities.validateParam(smtpAddress, "smtpAddress");
    }

    /**
     * Initializes a new instance of the Attendee class.
     *
     * @param name        the name
     * @param smtpAddress the smtp address
     */
    public Attendee(String name, String smtpAddress) {
        super(name, smtpAddress);
    }

    /**
     * Initializes a new instance of the Attendee class.
     *
     * @param name        the name
     * @param smtpAddress the smtp address
     * @param routingType the routing type
     */
    public Attendee(String name, String smtpAddress, String routingType) {
        super(name, smtpAddress, routingType);
    }

    /**
     * Initializes a new instance of the Attendee class.
     *
     * @param mailbox the mailbox
     * @throws Exception the exception
     */
    public Attendee(EmailAddress mailbox) throws Exception {
        super(mailbox);
    }

    /**
     * Gets the type of response the attendee gave to the meeting invitation
     * it received.
     *
     * @return the response type
     */
    public MeetingResponseType getResponseType() {
        return responseType;
    }

    /**
     * Gets the last response time.
     *
     * @return the last response time
     */
    public LocalDateTime getLastResponseTime() {
        return lastResponseTime;
    }

    /**
     * Tries to read element from XML.
     *
     * @param reader the reader
     * @return True if element was read.
     */
    public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        if (reader.getLocalName().equalsIgnoreCase(XmlElementNames.Mailbox)) {
            this.loadFromXml(reader, reader.getLocalName());
            return true;
        } else if (reader.getLocalName().equalsIgnoreCase(
                XmlElementNames.ResponseType)) {
            this.responseType = reader
                    .readElementValue(MeetingResponseType.class);
            return true;
        } else if (reader.getLocalName().equalsIgnoreCase(
                XmlElementNames.LastResponseTime)) {
            this.lastResponseTime = reader.readElementValueAsDateTime();
            return true;
        } else {
            return super.tryReadElementFromXml(reader);
        }
    }

    /**
     * Writes the elements to XML.
     *
     * @param writer the writer
     */
    public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeStartElement(this.getNamespace(), XmlElementNames.Mailbox);
        super.writeElementsToXml(writer);
        writer.writeEndElement();
    }
}
