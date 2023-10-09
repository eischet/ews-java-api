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

package com.eischet.ews.api.property.definition;

import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.ExchangeService;
import com.eischet.ews.api.core.PropertyBag;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.ExchangeVersion;
import com.eischet.ews.api.core.enumeration.property.PropertyDefinitionFlags;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.service.schema.AppointmentSchema;
import com.eischet.ews.api.property.complex.MeetingTimeZone;
import com.eischet.ews.api.property.complex.time.TimeZoneDefinition;

import javax.xml.stream.XMLStreamException;
import java.util.EnumSet;
import java.util.List;

/**
 * Represents a property definition for property of type TimeZoneInfo.
 */
public class StartTimeZonePropertyDefinition extends TimeZonePropertyDefinition {

    /**
     * Initializes a new instance of the StartTimeZonePropertyDefinition
     * class.
     *
     * @param xmlElementName the xml element name
     * @param uri            the uri
     * @param flags          the flags
     * @param version        the version
     */
    public StartTimeZonePropertyDefinition(String xmlElementName, String uri,
                                           EnumSet<PropertyDefinitionFlags> flags, ExchangeVersion version) {
        super(xmlElementName, uri, flags, version);
    }

    /**
     * Registers associated internal property.
     *
     * @param properties the property
     */
    protected void registerAssociatedInternalProperties(
            List<PropertyDefinition> properties) {
        super.registerAssociatedInternalProperties(properties);

        properties.add(AppointmentSchema.MeetingTimeZone);
    }

    /**
     * Writes to XML.
     *
     * @param writer            the writer
     * @param propertyBag       the property bag
     * @param isUpdateOperation the is update operation
     */
    public void writePropertyValueToXml(EwsServiceXmlWriter writer, PropertyBag propertyBag,
                                        boolean isUpdateOperation) throws ExchangeXmlException {
        Object value = propertyBag.getObjectFromPropertyDefinition(this);

        if (value != null) {
            final ExchangeService service = (ExchangeService) writer.getService();
            if (service.getRequestedServerVersion() == ExchangeVersion.Exchange2007_SP1) {
                if (!service.getExchange2007CompatibilityMode()) {
                    MeetingTimeZone meetingTimeZone = new MeetingTimeZone((TimeZoneDefinition) value);
                    meetingTimeZone.writeToXml(writer, XmlElementNames.MeetingTimeZone);
                }
            } else {
                super.writePropertyValueToXml(writer, propertyBag, isUpdateOperation);
            }
        }
    }

    /**
     * Writes to XML.
     *
     * @param writer the writer
     */
    public void writeToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        if (writer.getService().getRequestedServerVersion() == ExchangeVersion.Exchange2007_SP1) {
            AppointmentSchema.MeetingTimeZone.writeToXml(writer);
        } else {
            super.writeToXml(writer);
        }
    }

    /**
     * Determines whether the specified flag is set.
     *
     * @param flag    The flag.
     * @param version Requested version.
     * @return <c>true</c> if the specified
     * flag is set; otherwise, <c>false</c>.
     */
    @Override
    public boolean hasFlag(PropertyDefinitionFlags flag, ExchangeVersion version) {
        if (version != null && (version == ExchangeVersion.Exchange2007_SP1)) {
            return AppointmentSchema.MeetingTimeZone.hasFlag(flag, version);
        } else {
            return super.hasFlag(flag, version);
        }
    }

}
