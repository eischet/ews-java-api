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

package com.eischet.ews.api.property.complex.recurrence.range;

import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.property.complex.recurrence.pattern.Recurrence;

import javax.xml.stream.XMLStreamException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

/**
 * Represents recurrent range with an end date.
 */
public final class EndDateRecurrenceRange extends RecurrenceRange {

    /**
     * The end date.
     */
    private LocalDate endDate;

    /**
     * Initializes a new instance.
     */
    public EndDateRecurrenceRange() {
        super();
    }

    /**
     * Initializes a new instance.
     *
     * @param startDate the start date
     * @param endDate   the end date
     */
    public EndDateRecurrenceRange(LocalDate startDate, LocalDate endDate) {
        super(startDate);
        this.endDate = endDate;
    }

    /**
     * Gets the name of the XML element.
     *
     * @return The name of the XML element
     */
    public String getXmlElementName() {
        return XmlElementNames.EndDateRecurrence;
    }

    /**
     * Setups the recurrence.
     *
     * @param recurrence the new up recurrence
     */
    public void setupRecurrence(Recurrence recurrence) throws ExchangeXmlException {
        super.setupRecurrence(recurrence);
        recurrence.setEndDate(this.endDate);
    }

    /**
     * Writes the elements to XML.
     *
     * @param writer the writer
     */
    public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        LocalDate d = this.endDate;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedString = df.format(d);

        super.writeElementsToXml(writer);

        writer.writeElementValue(XmlNamespace.Types, XmlElementNames.EndDate,
                formattedString);
    }

    /**
     * Tries to read element from XML.
     *
     * @param reader the reader
     * @return True if element was read
     */
    public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        if (super.tryReadElementFromXml(reader)) {
            return true;
        } else {
            if (reader.getLocalName().equals(XmlElementNames.EndDate)) {

                LocalDate temp = reader.readElementValueAsUnspecifiedDate();

                if (temp != null) {
                    this.endDate = temp;
                }
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Gets the end date.
     *
     * @return endDate
     */
    public LocalDate getEndDate() {
        return this.endDate;
    }

    /**
     * sets the end date.
     *
     * @param value the new end date
     */
    public void setEndDate(LocalDate value) {
        this.canSetFieldValue(this.endDate, value);
    }

}
