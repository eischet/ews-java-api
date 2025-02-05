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

import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.XmlAttributeNames;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

/**
 * Represents the Id of an occurrence of a recurring appointment.
 */
public final class RecurringAppointmentMasterId extends ItemId {

    /**
     * Represents the Id of an occurrence of a recurring appointment.
     *
     * @param occurrenceId the occurrence id
     * @throws Exception the exception
     */
    public RecurringAppointmentMasterId(String occurrenceId) throws Exception {
        super(occurrenceId);
    }

    /**
     * Gets the name of the XML element.
     *
     * @return XML element name.
     */
    @Override
    public String getXmlElementName() {
        return XmlElementNames.RecurringMasterItemId;
    }

    /**
     * Writes attribute to XML.
     *
     * @param writer the writer
     */
    @Override
    public void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeAttributeValue(XmlAttributeNames.OccurrenceId, this.getUniqueId());
        writer.writeAttributeValue(XmlAttributeNames.ChangeKey, this.getChangeKey());
    }

}
