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
import com.eischet.ews.api.core.enumeration.property.LegacyFreeBusyStatus;
import com.eischet.ews.api.core.enumeration.property.MeetingResponseType;
import com.eischet.ews.api.core.enumeration.service.MeetingRequestType;
import com.eischet.ews.api.core.enumeration.service.calendar.AppointmentType;
import com.eischet.ews.api.core.exception.service.local.ServiceLocalException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.service.response.AcceptMeetingInvitationMessage;
import com.eischet.ews.api.core.service.response.DeclineMeetingInvitationMessage;
import com.eischet.ews.api.core.service.schema.AppointmentSchema;
import com.eischet.ews.api.core.service.schema.MeetingRequestSchema;
import com.eischet.ews.api.core.service.schema.ServiceObjectSchema;
import com.eischet.ews.api.misc.CalendarActionResults;
import com.eischet.ews.api.misc.TimeSpan;
import com.eischet.ews.api.property.complex.*;
import com.eischet.ews.api.property.complex.recurrence.pattern.Recurrence;
import com.eischet.ews.api.property.complex.time.TimeZoneDefinition;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a meeting request that an attendee can accept
 * or decline. Properties available on meeting
 * request are defined in the MeetingRequestSchema class.
 */
@ServiceObjectDefinition(xmlElementName = XmlElementNames.MeetingRequest)
public class MeetingRequest extends MeetingMessage implements ICalendarActionProvider {

    private static final Logger LOG = Logger.getLogger(MeetingRequest.class.getCanonicalName());

    /**
     * Initializes a new instance of the class.
     *
     * @param parentAttachment The parent attachment
     * @throws Exception throws Exception
     */
    public MeetingRequest(ItemAttachment parentAttachment) throws ExchangeXmlException {
        super(parentAttachment);
    }

    /**
     * Initializes a new instance of the class.
     *
     * @param service EWS service to which this object belongs.
     * @throws Exception throws Exception
     */
    public MeetingRequest(ExchangeService service) throws ExchangeXmlException {
        super(service);
    }

    /**
     * Binds to an existing meeting response and loads the specified set of
     * property. Calling this method results in a call to EWS.
     *
     * @param service     The service to use to bind to the meeting request.
     * @param id          The Id of the meeting request to bind to.
     * @param propertySet The set of property to load.
     * @return A MeetingResponse instance representing the meeting request
     * corresponding to the specified Id.
     */
    public static MeetingRequest bind(ExchangeService service, ItemId id,
                                      PropertySet propertySet) {
        try {
            return service.bindToItem(MeetingRequest.class, id, propertySet);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error binding meeting request", e);
            return null;
        }
    }

    /**
     * Binds to an existing meeting response and loads the specified set of
     * property. Calling this method results in a call to EWS.
     *
     * @param service The service to use to bind to the meeting request.
     * @param id      The Id of the meeting request to bind to.
     * @return A MeetingResponse instance representing the meeting request
     * corresponding to the specified Id.
     */
    public static MeetingRequest bind(ExchangeService service, ItemId id) {
        return MeetingRequest.bind(service, id, PropertySet
                .getFirstClassProperties());
    }

    /**
     * Internal method to return the schema associated with this type of object.
     *
     * @return The schema associated with this type of object.
     */
    @Override
    public ServiceObjectSchema getSchema() {
        return MeetingRequestSchema.Instance;
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
     * Creates a local meeting acceptance message that can be customized and
     * sent.
     *
     * @param tentative Specifies whether the meeting will be tentatively accepted.
     * @return An AcceptMeetingInvitationMessage representing the meeting
     * acceptance message.
     */
    public AcceptMeetingInvitationMessage createAcceptMessage(boolean
                                                                      tentative) {
        try {
            return new AcceptMeetingInvitationMessage(this, tentative);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error creating accept message", e);
            return null;
        }
    }

    /**
     * Creates a local meeting declination message that can be customized and
     * sent.
     *
     * @return A DeclineMeetingInvitation representing the meeting declination
     * message.
     */
    public DeclineMeetingInvitationMessage createDeclineMessage() {
        try {
            return new DeclineMeetingInvitationMessage(this);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error creating decline message", e);
            return null;
        }
    }

    /**
     * Accepts the meeting. Calling this method results in a call to EWS.
     *
     * @param sendResponse Indicates whether to send a response to the organizer.
     * @return A CalendarActionResults object containing the various item that
     * were created or modified as a results of this operation.
     * @throws Exception throws Exception
     */
    public CalendarActionResults accept(boolean sendResponse) throws Exception {
        return this.internalAccept(false, sendResponse);
    }

    /**
     * Tentatively accepts the meeting. Calling this method results in a call to
     * EWS.
     *
     * @param sendResponse Indicates whether to send a response to the organizer.
     * @return A CalendarActionResults object containing the various item that
     * were created or modified as a results of this operation.
     * @throws Exception throws Exception
     */
    public CalendarActionResults acceptTentatively(boolean sendResponse)
            throws Exception {
        return this.internalAccept(true, sendResponse);
    }

    /**
     * Accepts the meeting.
     *
     * @param tentative    True if tentative accept.
     * @param sendResponse Indicates whether to send a response to the organizer.
     * @return A CalendarActionResults object containing the various item that
     * were created or modified as a results of this operation.
     * @throws Exception throws Exception
     */
    protected CalendarActionResults internalAccept(boolean tentative,
                                                   boolean sendResponse) throws Exception {
        AcceptMeetingInvitationMessage accept = this
                .createAcceptMessage(tentative);

        if (sendResponse) {
            return accept.calendarSendAndSaveCopy();
        } else {
            return accept.calendarSave();

        }
    }

    /**
     * Declines the meeting invitation. Calling this method results in a call to
     * EWS.
     *
     * @param sendResponse Indicates whether to send a response to the organizer.
     * @return A CalendarActionResults object containing the various item that
     * were created or modified as a results of this operation.
     * @throws Exception throws Exception
     */
    public CalendarActionResults decline(boolean sendResponse)
            throws Exception {
        DeclineMeetingInvitationMessage decline = this.createDeclineMessage();

        if (sendResponse) {
            return decline.calendarSendAndSaveCopy();
        } else {
            return decline.calendarSave();
        }
    }

    /**
     * Gets the type of this meeting request.
     *
     * @return the meeting request type
     */
    public MeetingRequestType getMeetingRequestType() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(MeetingRequestSchema.MeetingRequestType);
    }

    /**
     * Gets the a value representing the intended free/busy status of the
     * meeting.
     *
     * @return the intended free busy status
     */
    public LegacyFreeBusyStatus getIntendedFreeBusyStatus() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(MeetingRequestSchema.IntendedFreeBusyStatus);
    }

    /**
     * Gets the start time of the appointment.
     *
     * @return the start
     */
    public LocalDateTime getStart() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(AppointmentSchema.Start);
    }

    /**
     * Gets the end time of the appointment.
     *
     * @return the end
     */
    public LocalDateTime getEnd() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(AppointmentSchema.End);
    }

    /**
     * Gets the original start time of the appointment.
     *
     * @return the original start
     */
    public LocalDateTime getOriginalStart() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(AppointmentSchema.OriginalStart);
    }

    /**
     * Gets a value indicating whether this appointment is an all day event.
     *
     * @return the checks if is all day event
     */
    public boolean getIsAllDayEvent() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.IsAllDayEvent) != null;
    }

    /**
     * Gets a value indicating the free/busy status of the owner of this
     * appointment.
     *
     * @return the legacy free busy status
     */
    public LegacyFreeBusyStatus legacyFreeBusyStatus()
            throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.LegacyFreeBusyStatus);
    }

    /**
     * Gets  the location of this appointment.
     *
     * @return the location
     * @throws ServiceLocalException the service local exception
     */
    public String getLocation() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.Location);
    }

    /**
     * Gets a text indicating when this appointment occurs. The text returned by
     * When is localized using the Exchange Server culture or using the culture
     * specified in the PreferredCulture property of the ExchangeService object
     * this appointment is bound to.
     *
     * @return the when
     */
    public String getWhen() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.When);
    }

    /**
     * Gets a value indicating whether the appointment is a meeting.
     *
     * @return the checks if is meeting
     * @throws ServiceLocalException the service local exception
     */
    public boolean getIsMeeting() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.IsMeeting) != null;
    }

    /**
     * Gets a value indicating whether the appointment has been cancelled.
     *
     * @return the checks if is cancelled
     * @throws ServiceLocalException the service local exception
     */
    public boolean getIsCancelled() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.IsCancelled) != null;
    }

    /**
     * Gets a value indicating whether the appointment is recurring.
     *
     * @return the checks if is recurring
     * @throws ServiceLocalException the service local exception
     */
    public boolean getIsRecurring() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.IsRecurring) != null;
    }

    /**
     * Gets a value indicating whether the meeting request has already been
     * sent.
     *
     * @return the meeting request was sent
     * @throws ServiceLocalException the service local exception
     */
    public boolean getMeetingRequestWasSent() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.MeetingRequestWasSent) != null;
    }

    /**
     * Gets a value indicating the type of this appointment.
     *
     * @return the appointment type
     * @throws ServiceLocalException the service local exception
     */
    public AppointmentType getAppointmentType() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.AppointmentType);
    }

    /**
     * Gets a value indicating what was the last response of the user that
     * loaded this meeting.
     *
     * @return the my response type
     * @throws ServiceLocalException the service local exception
     */
    public MeetingResponseType getMyResponseType()
            throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.MyResponseType);
    }

    /**
     * Gets the organizer of this meeting.
     *
     * @return the organizer
     * @throws ServiceLocalException the service local exception
     */
    public EmailAddress getOrganizer() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.Organizer);
    }

    /**
     * Gets a list of required attendees for this meeting.
     *
     * @return the required attendees
     * @throws ServiceLocalException the service local exception
     */
    public AttendeeCollection getRequiredAttendees()
            throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.RequiredAttendees);
    }

    /**
     * Gets a list of optional attendeed for this meeting.
     *
     * @return the optional attendees
     * @throws ServiceLocalException the service local exception
     */
    public AttendeeCollection getOptionalAttendees()
            throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.OptionalAttendees);
    }

    /**
     * Gets a list of resources for this meeting.
     *
     * @return the resources
     * @throws ServiceLocalException the service local exception
     */
    public AttendeeCollection getResources() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.Resources);
    }

    /**
     * Gets the number of calendar entries that conflict with
     * this appointment in the authenticated user's calendar.
     *
     * @return the conflicting meeting count
     * @throws NumberFormatException the number format exception
     * @throws ServiceLocalException the service local exception
     */
    public int getConflictingMeetingCount() throws NumberFormatException,
            ServiceLocalException, ExchangeXmlException {
        return (Integer.parseInt(this.getPropertyBag()
                .getObjectFromPropertyDefinition(
                        AppointmentSchema.ConflictingMeetingCount).toString()));
    }

    /**
     * Gets the number of calendar entries that are adjacent to
     * this appointment in the authenticated user's calendar.
     *
     * @return the adjacent meeting count
     * @throws NumberFormatException the number format exception
     * @throws ServiceLocalException the service local exception
     */
    public int getAdjacentMeetingCount() throws NumberFormatException,
            ServiceLocalException, ExchangeXmlException {
        return (Integer.parseInt(this.getPropertyBag()
                .getObjectFromPropertyDefinition(
                        AppointmentSchema.AdjacentMeetingCount).toString()));
    }

    /**
     * Gets a list of meetings that conflict with
     * this appointment in the authenticated user's calendar.
     *
     * @return the conflicting meetings
     * @throws ServiceLocalException the service local exception
     */
    public ItemCollection<Appointment> getConflictingMeetings()
            throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.ConflictingMeetings);
    }

    /**
     * Gets a list of meetings that are adjacent with this
     * appointment in the authenticated user's calendar.
     *
     * @return the adjacent meetings
     * @throws ServiceLocalException the service local exception
     */
    public ItemCollection<Appointment> getAdjacentMeetings()
            throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.AdjacentMeetings);
    }

    /**
     * Gets the duration of this appointment.
     *
     * @return the duration
     * @throws ServiceLocalException the service local exception
     */
    public TimeSpan getDuration() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.Duration);
    }

    /**
     * Gets the name of the time zone this appointment is defined in.
     *
     * @return the time zone
     * @throws ServiceLocalException the service local exception
     */
    public String getTimeZone() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.TimeZone);
    }

    /**
     * Gets the time when the attendee replied to the meeting request.
     *
     * @return the appointment reply time
     * @throws ServiceLocalException the service local exception
     */
    public LocalDateTime getAppointmentReplyTime() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(AppointmentSchema.AppointmentReplyTime);
    }

    /**
     * Gets the sequence number of this appointment.
     *
     * @return the appointment sequence number
     * @throws NumberFormatException the number format exception
     * @throws ServiceLocalException the service local exception
     */
    public int getAppointmentSequenceNumber() throws NumberFormatException,
            ServiceLocalException, ExchangeXmlException {
        return (Integer
                .parseInt(this.getPropertyBag()
                        .getObjectFromPropertyDefinition(
                                AppointmentSchema.AppointmentSequenceNumber)
                        .toString()));
    }

    /**
     * Gets the state of this appointment.
     *
     * @return the appointment state
     * @throws NumberFormatException the number format exception
     * @throws ServiceLocalException the service local exception
     */
    public int getAppointmentState() throws NumberFormatException,
            ServiceLocalException, ExchangeXmlException {
        return (Integer.parseInt(this.getPropertyBag()
                .getObjectFromPropertyDefinition(
                        AppointmentSchema.AppointmentState).toString()));
    }

    /**
     * Gets the recurrence pattern for this meeting request.
     *
     * @return the recurrence
     * @throws ServiceLocalException the service local exception
     */
    public Recurrence getRecurrence() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.Recurrence);
    }

    /**
     * Gets an OccurrenceInfo identifying the first occurrence of this meeting.
     *
     * @return the first occurrence
     * @throws ServiceLocalException the service local exception
     */
    public OccurrenceInfo getFirstOccurrence() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.FirstOccurrence);
    }

    /**
     * Gets an OccurrenceInfo identifying the last occurrence of this meeting.
     *
     * @return the last occurrence
     * @throws ServiceLocalException the service local exception
     */
    public OccurrenceInfo getLastOccurrence() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.FirstOccurrence);
    }

    /**
     * Gets a list of modified occurrences for this meeting.
     *
     * @return the modified occurrences
     * @throws ServiceLocalException the service local exception
     */
    public OccurrenceInfoCollection getModifiedOccurrences()
            throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.ModifiedOccurrences);
    }

    /**
     * Gets a list of deleted occurrences for this meeting.
     *
     * @return the deleted occurrences
     * @throws ServiceLocalException the service local exception
     */
    public DeletedOccurrenceInfoCollection getDeletedOccurrences()
            throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.DeletedOccurrences);
    }

    /**
     * Gets  time zone of the start property of this meeting request.
     *
     * @return the start time zone
     * @throws ServiceLocalException the service local exception
     */
    public TimeZoneDefinition getStartTimeZone() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.StartTimeZone);
    }

    /**
     * Gets  time zone of the end property of this meeting request.
     *
     * @return the end time zone
     * @throws ServiceLocalException the service local exception
     */
    public TimeZoneDefinition getEndTimeZone() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.EndTimeZone);
    }

    /**
     * Gets the type of conferencing that will be used during the meeting.
     *
     * @return the conference type
     * @throws NumberFormatException the number format exception
     * @throws ServiceLocalException the service local exception
     */
    public int getConferenceType() throws NumberFormatException,
            ServiceLocalException, ExchangeXmlException {
        return (Integer.parseInt(this.getPropertyBag()
                .getObjectFromPropertyDefinition(
                        AppointmentSchema.ConferenceType).toString()));
    }

    /**
     * Gets a value indicating whether new time
     * proposals are allowed for attendees of this meeting.
     *
     * @return the allow new time proposal
     * @throws ServiceLocalException the service local exception
     */
    public boolean getAllowNewTimeProposal() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().<Boolean>getObjectFromPropertyDefinition(
                AppointmentSchema.AllowNewTimeProposal);
    }

    /**
     * Gets a value indicating whether this is an online meeting.
     *
     * @return the checks if is online meeting
     * @throws ServiceLocalException the service local exception
     */
    public boolean getIsOnlineMeeting() throws ServiceLocalException, ExchangeXmlException {
        return getPropertyBag().<Boolean>getObjectFromPropertyDefinition(
                AppointmentSchema.IsOnlineMeeting);
    }

    /**
     * Gets the URL of the meeting workspace. A meeting
     * workspace is a shared Web site for
     * planning meetings and tracking results.
     *
     * @return the meeting workspace url
     */
    public String getMeetingWorkspaceUrl() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.MeetingWorkspaceUrl);
    }

    /**
     * Gets the URL of the Microsoft NetShow online meeting.
     *
     * @return the net show url
     */
    public String getNetShowUrl() throws ExchangeXmlException {
        return getPropertyBag().getObjectFromPropertyDefinition(
                AppointmentSchema.NetShowUrl);
    }
}
