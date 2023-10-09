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

package com.eischet.ews.api.property.complex.availability;

import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsUtilities;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.property.time.DayOfTheWeek;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.property.complex.ComplexProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a working period.
 */
final class WorkingPeriod extends ComplexProperty {

    /**
     * The days of week.
     */
    private final List<DayOfTheWeek> daysOfWeek = new ArrayList<DayOfTheWeek>();

    /**
     * The start time.
     */
    private long startTime;

    /**
     * The end time.
     */
    private long endTime;

    /**
     * Initializes a new instance of the WorkingPeriod class.
     */
    protected WorkingPeriod() {
        super();
    }

    /**
     * Tries to read element from XML.
     *
     * @param reader the reader
     * @return true, if successful
     */
    @Override
    public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        if (reader.getLocalName().equals(XmlElementNames.DayOfWeek)) {
            EwsUtilities.parseEnumValueList(DayOfTheWeek.class, this.daysOfWeek, reader.readElementValue(), ' ');
            return true;
        } else if (reader.getLocalName().equals(
                XmlElementNames.StartTimeInMinutes)) {
            this.startTime = reader.readElementValue(Integer.class);
            return true;
        } else if (reader.getLocalName().equals(
                XmlElementNames.EndTimeInMinutes)) {
            this.endTime = reader.readElementValue(Integer.class);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Gets a collection of work days.
     *
     * @return the days of week
     */
    public List<DayOfTheWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    /**
     * Gets the start time of the period.
     *
     * @return the start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the period.
     *
     * @return the end time
     */
    public long getEndTime() {
        return endTime;
    }

}
