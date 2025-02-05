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
import com.eischet.ews.api.core.EwsUtilities;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

import javax.xml.stream.XMLStreamException;

/**
 * Represents a time zone period transition that occurs on a specific day of a
 * specific month.
 */
class AbsoluteDayOfMonthTransition extends AbsoluteMonthTransition {

    /**
     * The day of month.
     */
    private int dayOfMonth;

    /**
     * Gets the XML element name associated with the transition.
     *
     * @return The XML element name associated with the transition.
     */
    @Override
    protected String getXmlElementName() {
        return XmlElementNames.RecurringDateTransition;
    }

    /**
     * Tries to read element from XML.
     *
     * @param reader returns True if element was read.
     * @return true
     * @throws Exception throws Exception
     */
    @Override
    public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        if (super.tryReadElementFromXml(reader)) {
            return true;
        } else {
            if (reader.getLocalName().equals(XmlElementNames.Day)) {
                this.dayOfMonth = reader.readElementValue(Integer.class);

                EwsUtilities.ewsAssert(this.dayOfMonth > 0 && this.dayOfMonth <= 31,
                        "AbsoluteDayOfMonthTransition.TryReadElementFromXml",
                        "dayOfMonth is not in the valid 1 - 31 range.");

                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Writes elements to XML.
     *
     * @param writer the writer
     */
    @Override
    public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        super.writeElementsToXml(writer);
        writer.writeElementValue(XmlNamespace.Types, XmlElementNames.Day, this.dayOfMonth);
    }

    /**
     * Initializes a new instance of the AbsoluteDayOfMonthTransition class.
     *
     * @param timeZoneDefinition the time zone definition
     */
    protected AbsoluteDayOfMonthTransition(TimeZoneDefinition timeZoneDefinition) {
        super(timeZoneDefinition);
    }

    /**
     * Initializes a new instance of the AbsoluteDayOfMonthTransition class.
     *
     * @param timeZoneDefinition the time zone definition
     * @param targetPeriod       the target period
     */

    protected AbsoluteDayOfMonthTransition(TimeZoneDefinition timeZoneDefinition, TimeZonePeriod targetPeriod) {
        super(timeZoneDefinition, targetPeriod);
    }

    /**
     * Gets the day of then month when this transition occurs.
     *
     * @return the day of month
     */
    protected int getDayOfMonth() {
        return this.dayOfMonth;
    }
}
