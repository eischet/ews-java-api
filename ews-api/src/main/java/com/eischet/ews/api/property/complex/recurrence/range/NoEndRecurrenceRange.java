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

import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.property.complex.recurrence.pattern.Recurrence;

import java.time.LocalDate;

/**
 * Represents recurrence range with no end date.
 */
public final class NoEndRecurrenceRange extends RecurrenceRange {

    /**
     * Initializes a new instance.
     */
    public NoEndRecurrenceRange() {
        super();
    }

    /**
     * Initializes a new instance.
     *
     * @param startDate the start date
     */
    public NoEndRecurrenceRange(LocalDate startDate) {
        super(startDate);
    }

    /**
     * Gets the name of the XML element.
     *
     * @return The name of the XML element
     */
    public String getXmlElementName() {
        return XmlElementNames.NoEndRecurrence;
    }

    /**
     * Setups the recurrence.
     *
     * @param recurrence the new up recurrence
     */
    public void setupRecurrence(Recurrence recurrence) throws ExchangeXmlException {
        super.setupRecurrence(recurrence);

        recurrence.neverEnds();
    }

}
