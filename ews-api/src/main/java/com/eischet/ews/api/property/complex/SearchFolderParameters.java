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

import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.XmlAttributeNames;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.enumeration.search.SearchFolderTraversal;
import com.eischet.ews.api.core.exception.service.local.ExchangeValidationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.search.filter.SearchFilter;

/**
 * Represents the parameters associated with a search folder.
 */
public final class SearchFolderParameters extends ComplexProperty implements IComplexPropertyChangedDelegate {

    /**
     * The traversal.
     */
    private SearchFolderTraversal traversal;

    /**
     * The root folder ids.
     */
    private final FolderIdCollection rootFolderIds = new FolderIdCollection();

    /**
     * The search filter.
     */
    private SearchFilter searchFilter;

    /**
     * Initializes a new instance of the SearchFolderParameters class.
     */
    public SearchFolderParameters() {
        super();
        this.rootFolderIds.addOnChangeEvent(this);
    }

    /**
     * Complex property changed.
     *
     * @param complexProperty the complex property
     */
    @Override
    public void complexPropertyChanged(ComplexProperty complexProperty) {
        this.propertyChanged(complexProperty);
    }

    /**
     * Property changed.
     *
     * @param complexProperty the complex property
     */
    private void propertyChanged(ComplexProperty complexProperty) {
        this.changed();
    }

    /**
     * Tries to read element from XML.
     *
     * @param reader the reader
     * @return True if element was read.
     */
    @Override
    public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        if (reader.getLocalName().equalsIgnoreCase(
                XmlElementNames.BaseFolderIds)) {
            this.rootFolderIds.internalClear();
            this.rootFolderIds.loadFromXml(reader, reader.getLocalName());
            return true;
        } else if (reader.getLocalName().equalsIgnoreCase(
                XmlElementNames.Restriction)) {
            reader.read();
            this.searchFilter = SearchFilter.loadFromXml(reader);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Reads the attribute from XML.
     *
     * @param reader the reader
     */
    @Override
    public void readAttributesFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        this.traversal = reader.readAttributeValue(SearchFolderTraversal.class, XmlAttributeNames.Traversal);
    }

    /**
     * Writes the attribute to XML.
     *
     * @param writer the writer
     */
    @Override
    public void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeAttributeValue(XmlAttributeNames.Traversal, this.traversal);
    }

    /**
     * Writes elements to XML.
     *
     * @param writer the writer
     */
    @Override
    public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        if (this.searchFilter != null) {
            writer.writeStartElement(XmlNamespace.Types,
                    XmlElementNames.Restriction);
            this.searchFilter.writeToXml(writer);
            writer.writeEndElement(); // Restriction
        }

        this.rootFolderIds.writeToXml(writer, XmlElementNames.BaseFolderIds);
    }

    /**
     * Validates this instance.
     *
     */
    public void validate() throws ExchangeValidationException {
        // Search folder must have at least one root folder id.
        if (this.rootFolderIds.getCount() == 0) {
            throw new ExchangeValidationException("SearchParameters must contain at least one folder id.");
        }

        // Validate the search filter
        if (this.searchFilter != null) {
            this.searchFilter.internalValidate();
        }
    }

    /**
     * Gets the traversal mode for the search folder.
     *
     * @return the traversal
     */
    public SearchFolderTraversal getTraversal() {
        return traversal;
    }

    /**
     * Sets the traversal.
     *
     * @param traversal the new traversal
     */
    public void setTraversal(SearchFolderTraversal traversal) {
        if (this.canSetFieldValue(this.traversal, traversal)) {
            this.traversal = traversal;
            this.changed();
        }
    }

    /**
     * Gets the list of root folder the search folder searches in.
     *
     * @return the root folder ids
     */
    public FolderIdCollection getRootFolderIds() {
        return rootFolderIds;
    }

    /**
     * Gets the search filter associated with the search folder.
     * Available search filter classes include SearchFilter.IsEqualTo,
     * SearchFilter.ContainsSubstring and SearchFilter.SearchFilterCollection.
     *
     * @return the search filter
     */
    public SearchFilter getSearchFilter() {
        return searchFilter;
    }

    /**
     * Sets the search filter.
     *
     * @param searchFilter the new search filter
     */
    public void setSearchFilter(SearchFilter searchFilter) {

        if (this.searchFilter != null) {
            this.searchFilter.removeChangeEvent(this);
        }

        if (this.canSetFieldValue(this.searchFilter, searchFilter)) {
            this.searchFilter = searchFilter;
            this.changed();
        }
        if (this.searchFilter != null) {
            this.searchFilter.addOnChangeEvent(this);
        }
    }

}
