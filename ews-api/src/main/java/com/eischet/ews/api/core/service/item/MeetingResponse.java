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

package com.eischet.ews.api.core.service.item;

import com.eischet.ews.api.attribute.ServiceObjectDefinition;
import com.eischet.ews.api.core.ExchangeService;
import com.eischet.ews.api.core.PropertySet;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.ExchangeVersion;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.property.complex.ItemAttachment;
import com.eischet.ews.api.property.complex.ItemId;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a response to a meeting request. Properties available on meeting
 * messages are defined in the MeetingMessageSchema class.
 */
@ServiceObjectDefinition(xmlElementName = XmlElementNames.MeetingResponse)
public class MeetingResponse extends MeetingMessage {

    private static final Logger LOG = Logger.getLogger(MeetingResponse.class.getCanonicalName());

    /**
     * Initializes a new instance of the class.
     *
     * @param parentAttachment The parentAttachment
     * @throws Exception the exception
     */
    public MeetingResponse(ItemAttachment parentAttachment) throws ExchangeXmlException {
        super(parentAttachment);
    }

    /**
     * Initializes a new instance of the class.
     *
     * @param service EWS service to which this object belongs.
     * @throws Exception the exception
     */
    public MeetingResponse(ExchangeService service) throws ExchangeXmlException {
        super(service);
    }

    /**
     * Binds to an existing meeting response and loads the specified set of
     * property. Calling this method results in a call to EWS.
     *
     * @param service     The service to use to bind to the meeting response.
     * @param id          The Id of the meeting response to bind to.
     * @param propertySet The set of property to load.
     * @return A MeetingResponse instance representing the meeting response
     * corresponding to the specified Id.
     */
    public static MeetingResponse bind(ExchangeService service, ItemId id, PropertySet propertySet) {
        try {
            return service.bindToItem(MeetingResponse.class, id, propertySet);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error binding meeting response", e);
            return null;
        }
    }

    /**
     * Binds to an existing meeting response and loads the specified set of
     * property. Calling this method results in a call to EWS.
     *
     * @param service The service to use to bind to the meeting response.
     * @param id      The Id of the meeting response to bind to.
     * @return A MeetingResponse instance representing the meeting response
     * corresponding to the specified Id.
     */
    public static MeetingResponse bind(ExchangeService service, ItemId id) {
        return MeetingResponse.bind(service, id, PropertySet.getFirstClassProperties());
    }

    /**
     * Gets the minimum required server version.
     *
     * @return Earliest Exchange version in which this service object type is
     * supported.
     */
    @Override
    public ExchangeVersion getMinimumRequiredServerVersion() {
        return ExchangeVersion.Exchange2007_SP1;
    }
}
