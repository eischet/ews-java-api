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

import com.eischet.ews.api.ISelfValidate;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.EwsUtilities;
import com.eischet.ews.api.core.XmlAttributeNames;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.enumeration.search.AggregateType;
import com.eischet.ews.api.core.enumeration.search.SortDirection;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.property.definition.PropertyDefinitionBase;

import javax.xml.stream.XMLStreamException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents grouping options in item search operations.
 */
public final class Grouping implements ISelfValidate {

    private static final Logger LOG = Logger.getLogger(Grouping.class.getCanonicalName());

    /**
     * The sort direction.
     */
    private SortDirection sortDirection = SortDirection.Ascending;

    /**
     * The group on.
     */
    private PropertyDefinitionBase groupOn;

    /**
     * The aggregate on.
     */
    private PropertyDefinitionBase aggregateOn;

    /**
     * The aggregate type.
     */
    private AggregateType aggregateType = AggregateType.Minimum;

    /**
     * Initializes a new instance of the "Grouping" class.
     */
    public Grouping() {

    }

    /**
     * Initializes a new instance of the "Grouping" class.
     *
     * @param groupOn       The property to group on
     * @param sortDirection The sort direction.
     * @param aggregateOn   The property to aggregate on.
     * @param aggregateType The type of aggregate to calculate.
     * @throws Exception the exception
     */
    public Grouping(PropertyDefinitionBase groupOn,
                    SortDirection sortDirection, PropertyDefinitionBase aggregateOn,
                    AggregateType aggregateType) throws Exception {
        this();
        EwsUtilities.validateParam(groupOn, "groupOn");
        EwsUtilities.validateParam(aggregateOn, "aggregateOn");

        this.groupOn = groupOn;
        this.sortDirection = sortDirection;
        this.aggregateOn = aggregateOn;
        this.aggregateType = aggregateType;
    }

    /**
     * Validates this grouping.
     *
     * @throws Exception the exception
     */
    private void internalValidate() throws Exception {
        EwsUtilities.validateParam(this.groupOn, "GroupOn");
        EwsUtilities.validateParam(this.aggregateOn, "AggregateOn");
    }

    /**
     * Writes to XML.
     *
     * @param writer the writer
     * @throws XMLStreamException               the XML stream exception
     * @throws ServiceXmlSerializationException the service xml serialization exception
     */
    protected void writeToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeStartElement(XmlNamespace.Messages, XmlElementNames.GroupBy);
        writer.writeAttributeValue(XmlAttributeNames.Order, this.sortDirection);

        this.groupOn.writeToXml(writer);

        writer.writeStartElement(XmlNamespace.Types, XmlElementNames.AggregateOn);
        writer.writeAttributeValue(XmlAttributeNames.Aggregate, this.aggregateType);

        this.aggregateOn.writeToXml(writer);

        writer.writeEndElement(); // AggregateOn

        writer.writeEndElement(); // GroupBy
    }

    /**
     * Gets the Sort Direction.
     *
     * @return the sort direction
     */
    public SortDirection getSortDirection() {
        return sortDirection;
    }

    /**
     * Sets the Sort Direction.
     *
     * @param sortDirection the new sort direction
     */
    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    /**
     * Gets the property to group on.
     *
     * @return the group on
     */
    public PropertyDefinitionBase getGroupOn() {
        return groupOn;
    }

    /**
     * sets the property to group on.
     *
     * @param groupOn the new group on
     */
    public void setGroupOn(PropertyDefinitionBase groupOn) {
        this.groupOn = groupOn;
    }

    /**
     * Gets the property to aggregateOn.
     *
     * @return the aggregate on
     */
    public PropertyDefinitionBase getAggregateOn() {
        return aggregateOn;
    }

    /**
     * Sets the property to aggregateOn.
     *
     * @param aggregateOn the new aggregate on
     */
    public void setAggregateOn(PropertyDefinitionBase aggregateOn) {
        this.aggregateOn = aggregateOn;
    }

    /**
     * Gets the types of aggregate to calculate.
     *
     * @return the aggregate type
     */
    public AggregateType getAggregateType() {
        return aggregateType;
    }

    /**
     * Sets the types of aggregate to calculate.
     *
     * @param aggregateType the new aggregate type
     */
    public void setAggregateType(AggregateType aggregateType) {
        this.aggregateType = aggregateType;
    }

    /**
     * Implements ISelfValidate.Validate. Validates this grouping.
     */
    @Override
    public void validate() {
        try {
            this.internalValidate();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "validation error", e);
        }

    }
}
