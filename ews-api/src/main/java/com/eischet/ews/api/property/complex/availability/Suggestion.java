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
import com.eischet.ews.api.core.enumeration.availability.SuggestionQuality;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.property.complex.ComplexProperty;
import com.eischet.ews.api.util.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a suggestion for a specific date.
 */
public final class Suggestion extends ComplexProperty {

    /**
     * The date.
     */
    private LocalDateTime date;

    /**
     * The quality.
     */
    private SuggestionQuality quality;

    /**
     * The time suggestions.
     */
    private final Collection<TimeSuggestion> timeSuggestions = new ArrayList<>();

    /**
     * Initializes a new instance of the Suggestion class.
     */
    public Suggestion() {
        super();
    }

    /**
     * Tries to read element from XML.
     *
     * @param reader the reader
     * @return True if appropriate element was read.
     */
    @Override
    public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        if (reader.getLocalName().equals(XmlElementNames.Date)) {
            this.date = DateTimeUtils.parseDateTime(reader.readElementValue());
            return true;
        } else if (reader.getLocalName().equals(XmlElementNames.DayQuality)) {
            this.quality = reader.readElementValue(SuggestionQuality.class);
            return true;
        } else if (reader.getLocalName()
                .equals(XmlElementNames.SuggestionArray)) {
            if (!reader.isEmptyElement()) {
                do {
                    reader.read();

                    if (reader.isStartElement(XmlNamespace.Types,
                            XmlElementNames.Suggestion)) {
                        TimeSuggestion timeSuggestion = new TimeSuggestion();

                        timeSuggestion.loadFromXml(reader, reader
                                .getLocalName());

                        this.timeSuggestions.add(timeSuggestion);
                    }
                } while (!reader.isEndElement(XmlNamespace.Types,
                        XmlElementNames.SuggestionArray));
            }

            return true;
        } else {
            return false;
        }

    }

    /**
     * Gets the date and time of the suggestion.
     *
     * @return the date
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Gets the quality of the suggestion.
     *
     * @return the quality
     */
    public SuggestionQuality getQuality() {
        return quality;
    }

    /**
     * Gets a collection of suggested times within the suggested day.
     *
     * @return the time suggestions
     */
    public Collection<TimeSuggestion> getTimeSuggestions() {
        return timeSuggestions;
    }

}
