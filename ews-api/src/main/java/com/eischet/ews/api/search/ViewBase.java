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

package com.eischet.ews.api.search;

import com.eischet.ews.api.attribute.EditorBrowsable;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.PropertySet;
import com.eischet.ews.api.core.XmlAttributeNames;
import com.eischet.ews.api.core.enumeration.attribute.EditorBrowsableState;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.enumeration.service.ServiceObjectType;
import com.eischet.ews.api.core.exception.service.local.ExchangeValidationException;
import com.eischet.ews.api.core.exception.service.local.ServiceVersionException;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.request.ServiceRequestBase;

import javax.xml.stream.XMLStreamException;

/**
 * Represents the base view class for search operations.
 */
@EditorBrowsable(state = EditorBrowsableState.Never)
public abstract class ViewBase {

    /**
     * The property set.
     */
    private PropertySet propertySet;

    /**
     * Initializes a new instance of the "ViewBase" class.
     */
    ViewBase() {
    }

    /**
     * Validates this view.
     *
     * @param request The request using this view.
     * @throws ExchangeValidationException the service validation exception
     * @throws ServiceVersionException    the service version exception
     */
    public void internalValidate(ServiceRequestBase request)
            throws ExchangeValidationException, ServiceVersionException {
        if (this.getPropertySet() != null) {
            this.getPropertySet().internalValidate();
            this.getPropertySet().validateForRequest(
                    request,
                    true /* summaryPropertiesOnly */);
        }
    }

    protected void internalWriteViewToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        Integer maxEntriesReturned = this.getMaxEntriesReturned();
        if (maxEntriesReturned != null) {
            writer.writeAttributeValue(XmlAttributeNames.MaxEntriesReturned,
                    maxEntriesReturned);
        }
    }

    /**
     * Writes the search settings to XML.
     *
     * @param writer  the writer
     * @param groupBy the group by clause
     * @throws XMLStreamException               the XML stream exception
     * @throws ServiceXmlSerializationException the service xml serialization exception
     */
    protected abstract void internalWriteSearchSettingsToXml(
            EwsServiceXmlWriter writer, Grouping groupBy)
            throws XMLStreamException, ServiceXmlSerializationException, ExchangeXmlException;

    /**
     * Writes OrderBy property to XML.
     *
     * @param writer the writer
     * @throws XMLStreamException               the XML stream exception
     * @throws ServiceXmlSerializationException the service xml serialization exception
     */
    public abstract void writeOrderByToXml(EwsServiceXmlWriter writer)
            throws XMLStreamException, ServiceXmlSerializationException, ExchangeXmlException;

    /**
     * Gets the name of the view XML element.
     *
     * @return TheXml Element name
     */
    protected abstract String getViewXmlElementName();

    /**
     * Gets the maximum number of item or folder the search operation should
     * return.
     *
     * @return The maximum number of item or folder that should be returned by
     * the search operation.
     */
    protected abstract Integer getMaxEntriesReturned();

    /**
     * Gets the type of service object this view applies to.
     *
     * @return A ServiceObjectType value.
     */
    protected abstract ServiceObjectType getServiceObjectType();

    /**
     * Writes the attribute to XML.
     *
     * @param writer The writer.
     * @throws ServiceXmlSerializationException the service xml serialization exception
     */
    public abstract void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException;

    /**
     * Writes to XML.
     *
     * @param writer  The writer.
     * @param groupBy The group by clause.
     * @throws Exception the exception
     */
    public void writeToXml(EwsServiceXmlWriter writer, Grouping groupBy)
            throws Exception {
        this.getPropertySetOrDefault().writeToXml(writer,
                this.getServiceObjectType());
        writer.writeStartElement(XmlNamespace.Messages, this
                .getViewXmlElementName());
        this.internalWriteViewToXml(writer);
        writer.writeEndElement(); // this.GetViewXmlElementName()
        this.internalWriteSearchSettingsToXml(writer, groupBy);
    }

    /**
     * Gets the property set or the default.
     *
     * @return PropertySet
     */
    public PropertySet getPropertySetOrDefault() {
        if (this.getPropertySet() == null) {
            return PropertySet.getFirstClassProperties();
        } else {
            return this.getPropertySet();
        }
    }

    /**
     * Gets the property set. PropertySet determines which property will be
     * loaded on found item. If PropertySet is null, all first class property
     * are loaded on found item.
     *
     * @return the property set
     */
    public PropertySet getPropertySet() {
        return propertySet;
    }

    /**
     * Sets the property set. PropertySet determines which property will be
     * loaded on found item. If PropertySet is null, all first class property
     * are loaded on found item.
     *
     * @param propertySet The property set
     */
    public void setPropertySet(PropertySet propertySet) {
        this.propertySet = propertySet;
    }

}
