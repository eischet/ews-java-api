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

package com.eischet.ews.api.property.complex.time;

import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.util.DateTimeUtils;

import javax.xml.stream.XMLStreamException;
import java.text.ParseException;
import java.time.LocalDateTime;

/**
 * Represents a time zone period transition that occurs on a fixed (absolute)
 * date.
 */
public class AbsoluteDateTransition extends TimeZoneTransition {

    /**
     * The date time.
     */
    private LocalDateTime dateTime;

    /**
     * Gets the XML element name associated with the transition.
     *
     * @return The XML element name associated with the transition.
     */
    @Override
    protected String getXmlElementName() {
        return XmlElementNames.AbsoluteDateTransition;
    }

    /**
     * Tries to read element from XML.
     *
     * @param reader the reader
     * @return True if element was read.
     */
    @Override
    public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        boolean result = super.tryReadElementFromXml(reader);
        if (!result) {
            if (reader.getLocalName().equals(XmlElementNames.DateTime)) {
                this.dateTime = DateTimeUtils.parseDateTime(reader.readElementValue());
                result = true;
            }
        }
        return result;
    }

    /**
     * Writes elements to XML.
     *
     * @param writer the writer
     */
    @Override
    public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        super.writeElementsToXml(writer);

        writer.writeElementValue(XmlNamespace.Types, XmlElementNames.DateTime,
                this.dateTime);
    }

    /**
     * Initializes a new instance of the AbsoluteDateTransition class.
     *
     * @param timeZoneDefinition , The time zone definition the transition will belong to.
     */
    protected AbsoluteDateTransition(TimeZoneDefinition timeZoneDefinition) {
        super(timeZoneDefinition);
    }

    /**
     * Initializes a new instance of the AbsoluteDateTransition class.
     *
     * @param timeZoneDefinition The time zone definition the transition will belong to.
     * @param targetGroup        the target group
     */
    protected AbsoluteDateTransition(TimeZoneDefinition timeZoneDefinition, TimeZoneTransitionGroup targetGroup) {
        super(timeZoneDefinition, targetGroup);
    }

    /**
     * Gets the absolute date and time when the transition occurs.
     *
     * @return the date time
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Sets the date time.
     *
     * @param dateTime the new date time
     */
    protected void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
