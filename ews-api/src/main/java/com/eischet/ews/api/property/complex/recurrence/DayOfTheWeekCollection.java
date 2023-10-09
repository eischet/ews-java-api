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

package com.eischet.ews.api.property.complex.recurrence;

import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.EwsUtilities;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.enumeration.property.time.DayOfTheWeek;
import com.eischet.ews.api.core.exception.misc.ArgumentOutOfRangeException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.property.complex.ComplexProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a collection of DayOfTheWeek values.
 */
public final class DayOfTheWeekCollection extends ComplexProperty implements
        Iterable<DayOfTheWeek> {

    /**
     * The item.
     */
    private final List<DayOfTheWeek> items = new ArrayList<>();

    /**
     * Initializes a new instance of the class.
     */
    public DayOfTheWeekCollection() {
    }

    /**
     * Convert to string.
     *
     * @param separator the separator
     * @return String representation of collection.
     */
    private String toString(String separator) {
        if (this.getCount() == 0) {
            return "";
        } else {
            // String[] daysOfTheWeekArray = new String[this.getCount()];
            StringBuilder daysOfTheWeekstr = new StringBuilder();

            for (int i = 0; i < this.getCount(); i++) {
                // daysOfTheWeekArray[i] = item.get(i).toString();
                if (daysOfTheWeekstr.length() == 0) {
                    daysOfTheWeekstr.append(items.get(i).toString());
                } else {
                    daysOfTheWeekstr.append(separator);
                    daysOfTheWeekstr.append(items.get(i).toString());
                }
            }

            return daysOfTheWeekstr.toString();
        }
    }

    /**
     * Loads from XML.
     *
     * @param reader         The reader.
     * @param xmlElementName Name of the XML element.
     */
    public void loadFromXml(EwsServiceXmlReader reader, String xmlElementName) throws ExchangeXmlException {
        reader.ensureCurrentNodeIsStartElement(XmlNamespace.Types,
                xmlElementName);
        EwsUtilities.parseEnumValueList(DayOfTheWeek.class, this.items, reader.readElementValue(), ' ');
    }

    /**
     * Gets the request version.
     *
     * @param writer         the writer
     * @param xmlElementName the xml element name
     */
    @Override
    public void writeToXml(EwsServiceXmlWriter writer, String xmlElementName)
            throws ExchangeXmlException {
        String daysOfWeekAsString = this.toString(" ");

        if (!daysOfWeekAsString.isEmpty()) {
            writer.writeElementValue(XmlNamespace.Types,
                    XmlElementNames.DaysOfWeek, daysOfWeekAsString);
        }
    }

    /**
     * Builds string representation of the collection.
     *
     * @return A comma-delimited string representing the collection.
     */
    @Override
    public String toString() {
        return this.toString(",");
    }

    /**
     * Adds a day to the collection if it is not already present.
     *
     * @param dayOfTheWeek The day to add.
     */
    public void add(DayOfTheWeek dayOfTheWeek) {
        if (!this.items.contains(dayOfTheWeek)) {
            this.items.add(dayOfTheWeek);
            this.changed();
        }
    }

    /**
     * Adds multiple days to the collection if they are not already present.
     *
     * @param daysOfTheWeek The days to add.
     */
    public void addRange(Iterator<DayOfTheWeek> daysOfTheWeek) {
        while (daysOfTheWeek.hasNext()) {
            this.add(daysOfTheWeek.next());
        }
    }

    /**
     * Clears the collection.
     */
    public void clear() {
        if (this.getCount() > 0) {
            this.items.clear();
            this.changed();
        }
    }

    /**
     * Remove a specific day from the collection.
     *
     * @param dayOfTheWeek the day of the week
     * @return True if the day was removed from the collection, false otherwise.
     */
    public boolean remove(DayOfTheWeek dayOfTheWeek) {
        boolean result = this.items.remove(dayOfTheWeek);

        if (result) {
            this.changed();
        }
        return result;
    }

    /**
     * Removes the day at a specific index.
     *
     * @param index the index
     * @throws ArgumentOutOfRangeException the argument out of range exception
     */
    public void removeAt(int index) throws ArgumentOutOfRangeException {
        if (index < 0 || index >= this.getCount()) {
            throw new ArgumentOutOfRangeException("index", "index is out of range.");
        }

        this.items.remove(index);
        this.changed();
    }

    /**
     * Gets the DayOfTheWeek at a specific index in the collection.
     *
     * @param index the index
     * @return DayOfTheWeek at index
     */
    public DayOfTheWeek getWeekCollectionAtIndex(int index) {
        return this.items.get(index);
    }

    /**
     * Gets the number of days in the collection.
     *
     * @return the count
     */
    public int getCount() {
        return this.items.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<DayOfTheWeek> iterator() {
        return this.items.iterator();
    }

}
