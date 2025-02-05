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

import com.eischet.ews.api.core.*;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.misc.Time;
import com.eischet.ews.api.misc.TimeSpan;
import com.eischet.ews.api.util.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * Represents a change of time for a time zone.
 */
public final class TimeChange extends ComplexProperty {

    /**
     * The time zone name.
     */
    private String timeZoneName;

    /**
     * The offset.
     */
    private TimeSpan offset;

    /**
     * The time.
     */
    private Time time;

    /**
     * The absolute date.
     */
    private LocalDateTime absoluteDate;

    /**
     * The recurrence.
     */
    private TimeChangeRecurrence recurrence;

    /**
     * Initializes a new instance of the "TimeChange" class.
     */
    public TimeChange() {
        super();
    }

    /**
     * Initializes a new instance of the <see cref="TimeChange"/> class.
     *
     * @param offset The offset since the beginning of the year when the change
     *               occurs.
     */
    public TimeChange(TimeSpan offset) {
        this();
        this.offset = offset;
    }

    /**
     * Initializes a new instance of the "TimeChange" class.
     *
     * @param offset The offset since the beginning of the year when the change
     *               occurs.
     * @param time   The time at which the change occurs.
     */
    public TimeChange(TimeSpan offset, Time time) {
        this(offset);
        this.time = time;
    }

    /**
     * Gets the name of the associated time zone.
     *
     * @return the timeZoneName
     */
    public String getTimeZoneName() {
        return timeZoneName;
    }

    /**
     * Sets the name of the associated time zone.
     *
     * @param timeZoneName the timeZoneName to set
     */
    public void setTimeZoneName(String timeZoneName) {
        this.timeZoneName = timeZoneName;
    }

    /**
     * Gets the offset since the beginning of the year when the change occurs.
     *
     * @return the offset
     */
    public TimeSpan getOffset() {
        return offset;
    }

    /**
     * Sets the offset since the beginning of the year when the change occurs.
     *
     * @param offset the offset to set
     */
    public void setOffset(TimeSpan offset) {
        this.offset = offset;
    }

    /**
     * Gets the time.
     *
     * @return the time
     */
    public Time getTime() {
        return time;
    }

    /**
     * Sets the time.
     *
     * @param time the time to set
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * Gets the absolute date.
     *
     * @return the absoluteDate
     */
    public LocalDateTime getAbsoluteDate() {
        return absoluteDate;
    }

    /**
     * Sets the absolute date.
     *
     * @param absoluteDate the absoluteDate to set
     */
    public void setAbsoluteDate(LocalDateTime absoluteDate) {
        this.absoluteDate = absoluteDate;
        if (absoluteDate != null) {
            this.recurrence = null;
        }
    }

    /**
     * Gets the recurrence.
     *
     * @return the recurrence
     */
    public TimeChangeRecurrence getRecurrence() {
        return recurrence;
    }

    /**
     * Sets the recurrence.
     *
     * @param recurrence the recurrence to set
     */
    public void setRecurrence(TimeChangeRecurrence recurrence) {
        this.recurrence = recurrence;
        if (this.recurrence != null) {
            this.absoluteDate = null;
        }
    }

    /**
     * Tries to read element from XML.
     *
     * @param reader accepts EwsServiceXmlReader
     * @return True if element was read
     */
    @Override
    public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {

        if (reader.getLocalName().equalsIgnoreCase(XmlElementNames.Offset)) {
            this.offset = EwsUtilities.getXSDurationToTimeSpan(reader.readElementValue());
            return true;
        } else if (reader.getLocalName().equalsIgnoreCase(
                XmlElementNames.RelativeYearlyRecurrence)) {
            this.recurrence = new TimeChangeRecurrence();
            this.recurrence.loadFromXml(reader, reader.getLocalName());
            return true;
        } else if (reader.getLocalName().equalsIgnoreCase(XmlElementNames.AbsoluteDate)) {
            this.absoluteDate = DateTimeUtils.parseDateTime(reader.readElementValue());
            return true;
        } else if (reader.getLocalName().equalsIgnoreCase(XmlElementNames.Time)) {
            this.time = new Time(DateTimeUtils.parseTime(reader.readElementValue()));
            // Calendar cal = DatatypeConverter.parseTime(reader.readElementValue());
            // this.time = new Time(cal.getTime());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Reads the attribute from XML.
     *
     * @param reader accepts EwsServiceXmlReader
     */
    @Override
    public void readAttributesFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        this.timeZoneName = reader.readAttributeValue(XmlAttributeNames.TimeZoneName);
    }

    /**
     * Writes the attribute to XML.
     *
     * @param writer accepts EwsServiceXmlWriter
     */
    @Override
    public void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeAttributeValue(XmlAttributeNames.TimeZoneName, this.timeZoneName);
    }

    /**
     * Writes elements to XML.
     *
     * @param writer accepts EwsServiceXmlWriter
     */
    @Override
    public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        if (this.offset != null) {
            writer.writeElementValue(XmlNamespace.Types, XmlElementNames.Offset, EwsUtilities.getTimeSpanToXSDuration(this.getOffset()));
        }

        if (this.recurrence != null) {
            this.recurrence.writeToXml(writer,
                    XmlElementNames.RelativeYearlyRecurrence);
        }

        if (this.absoluteDate != null) {
            writer.writeElementValue(XmlNamespace.Types,
                    XmlElementNames.AbsoluteDate, EwsUtilities
                            .dateTimeToXSDate(this.getAbsoluteDate()));
        }

        if (this.time != null) {
            writer.writeElementValue(XmlNamespace.Types, XmlElementNames.Time,
                    this.getTime().toXSTime());
        }
    }

}
