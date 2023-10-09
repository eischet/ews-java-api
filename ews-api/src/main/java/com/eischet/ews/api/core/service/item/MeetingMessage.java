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

import com.eischet.ews.api.attribute.EditorBrowsable;
import com.eischet.ews.api.attribute.ServiceObjectDefinition;
import com.eischet.ews.api.core.ExchangeService;
import com.eischet.ews.api.core.PropertySet;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.attribute.EditorBrowsableState;
import com.eischet.ews.api.core.enumeration.misc.ExchangeVersion;
import com.eischet.ews.api.core.enumeration.property.MeetingResponseType;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.service.schema.MeetingMessageSchema;
import com.eischet.ews.api.core.service.schema.ServiceObjectSchema;
import com.eischet.ews.api.property.complex.ItemAttachment;
import com.eischet.ews.api.property.complex.ItemId;

import java.time.LocalDateTime;


/**
 * Represents a meeting-related message. Properties available on meeting
 * messages are defined in the MeetingMessageSchema class.
 */

@ServiceObjectDefinition(xmlElementName = XmlElementNames.MeetingMessage)
@EditorBrowsable(state = EditorBrowsableState.Never)
public class MeetingMessage extends EmailMessage {

    /**
     * Initializes a new instance of the "MeetingMessage" class.
     *
     * @param parentAttachment the parent attachment
     * @throws Exception the exception
     */
    public MeetingMessage(ItemAttachment parentAttachment) throws ExchangeXmlException {
        super(parentAttachment);
    }

    /**
     * Initializes a new instance of the "MeetingMessage" class.
     *
     * @param service EWS service to which this object belongs.
     * @throws Exception the exception
     */
    public MeetingMessage(ExchangeService service) throws ExchangeXmlException {
        super(service);
    }

    /**
     * Binds to an existing meeting message and loads the specified set of
     * property. Calling this method results in a call to EWS.
     *
     * @param service     The service to use to bind to the meeting message.
     * @param id          The Id of the meeting message to bind to.
     * @param propertySet The set of property to load.
     * @return A MeetingMessage instance representing the meeting message
     * corresponding to the specified Id.
     * @throws Exception the exception
     */
    public static MeetingMessage bind(ExchangeService service, ItemId id,
                                      PropertySet propertySet) throws Exception {
        return (MeetingMessage) service.bindToItem(id, propertySet);
    }

    /**
     * Binds to an existing meeting message and loads its first class
     * property. Calling this method results in a call to EWS.
     *
     * @param service The service to use to bind to the meeting message.
     * @param id      The Id of the meeting message to bind to.
     * @return A MeetingMessage instance representing the meeting message
     * corresponding to the specified Id.
     * @throws Exception the exception
     */
    public static MeetingMessage bind(ExchangeService service, ItemId id)
            throws Exception {
        return MeetingMessage.bind(service, id, PropertySet
                .getFirstClassProperties());
    }

    /**
     * Internal method to return the schema associated with this type of object.
     *
     * @return The schema associated with this type of object.
     */
    @Override
    public ServiceObjectSchema getSchema() {
        return MeetingMessageSchema.getInstance();
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

    /**
     * Gets the associated appointment ID.
     *
     * @return the associated appointment ID.
     */
    public ItemId getAssociatedAppointmentId() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                MeetingMessageSchema.AssociatedAppointmentId);
    }

    /**
     * Gets whether the meeting message has been processed.
     *
     * @return whether the meeting message has been processed.
     */
    public Boolean getHasBeenProcessed() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                MeetingMessageSchema.HasBeenProcessed);
    }

    /**
     * Gets the response type indicated by this meeting message.
     *
     * @return the response type indicated by this meeting message.
     */
    public MeetingResponseType getResponseType() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                MeetingMessageSchema.ResponseType);
    }

    /**
     * Gets the ICalendar Uid.
     *
     * @return the ical uid
     */
    public String getICalUid() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                MeetingMessageSchema.ICalUid);
    }

    /**
     * Gets the ICalendar RecurrenceId.
     *
     * @return the ical recurrence id
     */
    public LocalDateTime getICalRecurrenceId() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(MeetingMessageSchema.ICalRecurrenceId);
    }

    /**
     * Gets the ICalendar DateTimeStamp.
     *
     * @return the ical date time stamp
     */
    public LocalDateTime getICalDateTimeStamp() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(MeetingMessageSchema.ICalDateTimeStamp);
    }

    /**
     * Gets the IsDelegated property.
     *
     * @return True if delegated; false otherwise.
     */
    public Boolean getIsDelegated() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(MeetingMessageSchema.IsDelegated);
    }

    /**
     * Gets the IsOutOfDate property.
     *
     * @return True if out of date; false otherwise.
     */
    public Boolean getIsOutOfDate() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(MeetingMessageSchema.IsOutOfDate);
    }

}
