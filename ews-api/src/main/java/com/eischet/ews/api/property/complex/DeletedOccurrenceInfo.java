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
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

import java.time.LocalDateTime;

/**
 * Encapsulates information on the deleted occurrence of a recurring
 * appointment.
 */
public class DeletedOccurrenceInfo extends ComplexProperty {

    /**
     * The original start date and time of the deleted occurrence. The EWS
     * schema contains a Start property for deleted occurrences but it's really
     * the original start date and time of the occurrence.
     */
    private LocalDateTime originalStart;

    /**
     * Initializes a new instance of the "DeletedOccurrenceInfo" class.
     */
    protected DeletedOccurrenceInfo() {
    }

    /**
     * Tries to read element from XML.
     *
     * @param reader The reader.
     * @return True if element was read.
     */
    @Override
    public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        if (reader.getLocalName().equalsIgnoreCase(XmlElementNames.Start)) {
            this.originalStart = reader.readElementValueAsDateTime();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the original start date and time of the deleted occurrence.
     *
     * @return the original start
     */
    public LocalDateTime getOriginalStart() {
        return this.originalStart;
    }

}
