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
import com.eischet.ews.api.property.complex.ComplexProperty;
import com.eischet.ews.api.property.complex.recurrence.pattern.Recurrence;

import javax.xml.stream.XMLStreamException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

/**
 * Represents recurrence range with start and end dates.
 */
public abstract class RecurrenceRange extends ComplexProperty {

    /**
     * The start date.
     */
    private LocalDate startDate;

    /**
     * The recurrence.
     */
    private Recurrence recurrence;

    /**
     * Initializes a new instance.
     */
    protected RecurrenceRange() {
        super();
    }

    /**
     * Initializes a new instance.
     *
     * @param startDate the start date
     */
    protected RecurrenceRange(LocalDate startDate) {
        this();
        this.startDate = startDate;
    }

    /**
     * Changes handler.
     */
    public void changed() {
        if (this.recurrence != null) {
            this.recurrence.changed();
        }
    }

    /**
     * Setup the recurrence.
     *
     * @param recurrence the new up recurrence
     * @throws Exception the exception
     */
    public void setupRecurrence(Recurrence recurrence) throws Exception {
        recurrence.setStartDate(this.getStartDate());
    }

    /**
     * Writes elements to XML..
     *
     * @param writer the writer
     * @throws XMLStreamException               the XML stream exception
     * @throws ServiceXmlSerializationException the service xml serialization exception
     */
    public void writeElementsToXml(EwsServiceXmlWriter writer)
            throws XMLStreamException, ServiceXmlSerializationException {
        LocalDate d = this.startDate;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedString = df.format(d);
        writer.writeElementValue(XmlNamespace.Types, XmlElementNames.StartDate, formattedString);
    }

    /**
     * Tries to read element from XML.
     *
     * @param reader the reader
     * @return True if element was read
     * @throws Exception the exception
     */
    public boolean tryReadElementFromXml(EwsServiceXmlReader reader)
            throws Exception {
        if (reader.getLocalName().equals(XmlElementNames.StartDate)) {
            //this.startDate = reader.readElementValueAsDateTime();
            LocalDate startDate = reader.readElementValueAsUnspecifiedDate();
            if (startDate != null) {
                this.startDate = startDate;
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * Gets the name of the XML element.
     *
     * @return recurrence
     */
    public abstract String getXmlElementName();

    /**
     * Gets or sets the recurrence.
     *
     * @return recurrence
     */
    protected Recurrence getRecurrence() {
        return this.recurrence;
    }

    /**
     * Sets the recurrence.
     *
     * @param value the new recurrence
     */
    protected void setRecurrence(Recurrence value) {
        this.recurrence = value;
    }

    /**
     * Gets the start date.
     *
     * @return startDate
     */
    protected LocalDate getStartDate() {
        return this.startDate;

    }

    /**
     * Sets the start date.
     *
     * @param value the new start date
     */
    protected void setStartDate(LocalDate value) {
        this.canSetFieldValue(this.startDate, value);
    }

}
