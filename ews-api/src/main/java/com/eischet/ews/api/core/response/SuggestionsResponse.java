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

package com.eischet.ews.api.core.response;

import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.property.complex.availability.Suggestion;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents the base response class to subscription creation operations.
 */
public final class SuggestionsResponse extends ServiceResponse {

    /**
     * The day suggestions.
     */
    private final Collection<Suggestion> daySuggestions = new ArrayList<Suggestion>();

    /**
     * Initializes a new instance of the SuggestionsResponse class.
     */
    public SuggestionsResponse() {
        super();
    }

    /**
     * Loads the suggested days from XML.
     *
     * @param reader the reader
     * @throws Exception the exception
     */
    public void loadSuggestedDaysFromXml(EwsServiceXmlReader reader)
            throws Exception {
        reader.readStartElement(XmlNamespace.Messages,
                XmlElementNames.SuggestionDayResultArray);

        do {
            reader.read();

            if (reader.isStartElement(XmlNamespace.Types,
                    XmlElementNames.SuggestionDayResult)) {
                Suggestion daySuggestion = new Suggestion();

                daySuggestion.loadFromXml(reader, reader.getLocalName());

                this.daySuggestions.add(daySuggestion);
            }
        } while (!reader.isEndElement(XmlNamespace.Messages,
                XmlElementNames.SuggestionDayResultArray));
    }

    /**
     * Gets a list of suggested days.
     *
     * @return the suggestions
     */
    public Collection<Suggestion> getSuggestions() {
        return this.daySuggestions;
    }
}
