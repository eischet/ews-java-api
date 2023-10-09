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
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.enumeration.property.time.DayOfTheWeek;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.misc.availability.LegacyAvailabilityTimeZone;
import com.eischet.ews.api.property.complex.ComplexProperty;
import com.eischet.ews.api.property.complex.time.TimeZoneDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents the working hours for a specific time zone.
 */
public final class WorkingHours extends ComplexProperty {

    /**
     * The time zone.
     */
    private TimeZoneDefinition timeZone;

    /**
     * The days of the week.
     */
    private final Collection<DayOfTheWeek> daysOfTheWeek =
            new ArrayList<DayOfTheWeek>();

    /**
     * The start time.
     */
    private long startTime;

    /**
     * The end time.
     */
    private long endTime;

    /**
     * Instantiates a new working hours.
     */
    public WorkingHours() {
        super();
    }

    /**
     * Tries to read element from XML.
     *
     * @param reader accepts EwsServiceXmlReader
     * @return True if element was read
     */
    @Override
    public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        if (reader.getLocalName().equals(XmlElementNames.TimeZone)) {
            LegacyAvailabilityTimeZone legacyTimeZone =
                    new LegacyAvailabilityTimeZone();
            legacyTimeZone.loadFromXml(reader, reader.getLocalName());

            this.timeZone = legacyTimeZone.toTimeZoneInfo();

            return true;
        }
        if (reader.getLocalName().equals(XmlElementNames.WorkingPeriodArray)) {
            List<WorkingPeriod> workingPeriods = new ArrayList<>();

            do {
                reader.read();

                if (reader.isStartElement(XmlNamespace.Types,
                        XmlElementNames.WorkingPeriod)) {
                    WorkingPeriod workingPeriod = new WorkingPeriod();

                    workingPeriod.loadFromXml(reader, reader.getLocalName());

                    workingPeriods.add(workingPeriod);
                }
            } while (!reader.isEndElement(XmlNamespace.Types,
                    XmlElementNames.WorkingPeriodArray));

            // Availability supports a structure that can technically represent
            // different working
            // times for each day of the week. This is apparently how the
            // information is stored in
            // Exchange. However, no client (Outlook, OWA) either will let you
            // specify different
            // working times for each day of the week, and Outlook won't either
            // honor that complex
            // structure if it happens to be in Exchange (OWA goes through XSO
            // which doesn't either
            // honor the structure).
            // So here we'll do what Outlook and OWA do: we'll use the start and
            // end times of the
            // first working period, but we'll use the week days of all the
            // periods.

            this.startTime = workingPeriods.get(0).getStartTime();
            this.endTime = workingPeriods.get(0).getEndTime();

            for (WorkingPeriod workingPeriod : workingPeriods) {
                // TODO check: can it possibly be right to ignore the loop var here an instead use the first element? I don't think so, but the API is so obtuse in parts I'm really not sure.
                for (DayOfTheWeek dayOfWeek : workingPeriods.get(0).getDaysOfWeek()) {
                    if (!this.daysOfTheWeek.contains(dayOfWeek)) {
                        this.daysOfTheWeek.add(dayOfWeek);
                    }
                }
            }

            return true;
        } else {
            return false;
        }

    }

    /**
     * Gets the time zone to which the working hours apply.
     *
     * @return the time zone
     */
    public TimeZoneDefinition getTimeZone() {
        return timeZone;
    }

    /**
     * Gets the working days of the attendees.
     *
     * @return the days of the week
     */
    public Collection<DayOfTheWeek> getDaysOfTheWeek() {
        return daysOfTheWeek;
    }

    /**
     * Gets the time of the day the attendee starts working.
     *
     * @return the start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Gets the time of the day the attendee stops working.
     *
     * @return the end time
     */
    public long getEndTime() {
        return endTime;
    }

}
