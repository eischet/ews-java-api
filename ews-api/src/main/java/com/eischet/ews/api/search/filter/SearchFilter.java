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

package com.eischet.ews.api.search.filter;

import com.eischet.ews.api.attribute.EditorBrowsable;
import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.XmlAttributeNames;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.attribute.EditorBrowsableState;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.enumeration.search.ComparisonMode;
import com.eischet.ews.api.core.enumeration.search.ContainmentMode;
import com.eischet.ews.api.core.enumeration.search.LogicalOperator;
import com.eischet.ews.api.core.exception.service.local.ExchangeValidationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.misc.OutParam;
import com.eischet.ews.api.property.complex.ComplexProperty;
import com.eischet.ews.api.property.complex.IComplexPropertyChangedDelegate;
import com.eischet.ews.api.property.definition.PropertyDefinitionBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Represents the base search filter class. Use descendant search filter classes
 * such as SearchFilter.IsEqualTo, SearchFilter.ContainsSubstring and
 * SearchFilter.SearchFilterCollection to define search filter.
 */
public abstract class SearchFilter extends ComplexProperty {

    /**
     * Initializes a new instance of the SearchFilter class.
     */
    protected SearchFilter() {
    }


    /**
     * Loads from XML.
     *
     * @param reader the reader
     * @return SearchFilter
     * @throws Exception the exception
     */
    public static SearchFilter loadFromXml(EwsServiceXmlReader reader)
            throws ExchangeXmlException {
        reader.ensureCurrentNodeIsStartElement();

        SearchFilter searchFilter = null;

        if (reader.getLocalName().equalsIgnoreCase(XmlElementNames.Exists)) {
            searchFilter = new Exists();
        } else if (reader.getLocalName().equalsIgnoreCase(
                XmlElementNames.Contains)) {
            searchFilter = new ContainsSubstring();
        } else if (reader.getLocalName().equalsIgnoreCase(
                XmlElementNames.Excludes)) {
            searchFilter = new ExcludesBitmask();
        } else if (reader.getLocalName().equalsIgnoreCase(XmlElementNames.Not)) {
            searchFilter = new Not();
        } else if (reader.getLocalName().equalsIgnoreCase(XmlElementNames.And)) {
            searchFilter = new SearchFilterCollection(
                    LogicalOperator.And);
        } else if (reader.getLocalName().equalsIgnoreCase(XmlElementNames.Or)) {
            searchFilter = new SearchFilterCollection(
                    LogicalOperator.Or);
        } else if (reader.getLocalName().equalsIgnoreCase(
                XmlElementNames.IsEqualTo)) {
            searchFilter = new IsEqualTo();
        } else if (reader.getLocalName().equalsIgnoreCase(
                XmlElementNames.IsNotEqualTo)) {
            searchFilter = new IsNotEqualTo();
        } else if (reader.getLocalName().equalsIgnoreCase(
                XmlElementNames.IsGreaterThan)) {
            searchFilter = new IsGreaterThan();
        } else if (reader.getLocalName().equalsIgnoreCase(
                XmlElementNames.IsGreaterThanOrEqualTo)) {
            searchFilter = new IsGreaterThanOrEqualTo();
        } else if (reader.getLocalName().equalsIgnoreCase(
                XmlElementNames.IsLessThan)) {
            searchFilter = new IsLessThan();
        } else if (reader.getLocalName().equalsIgnoreCase(
                XmlElementNames.IsLessThanOrEqualTo)) {
            searchFilter = new IsLessThanOrEqualTo();
        } else {
            searchFilter = null;
        }

        if (searchFilter != null) {
            searchFilter.loadFromXml(reader, reader.getLocalName());
        }

        return searchFilter;
    }

    /**
     * Gets the name of the XML element.
     *
     * @return the xml element name
     */
    protected abstract String getXmlElementName();

    /**
     * Writes to XML.
     *
     * @param writer the writer
     * @throws Exception the exception
     */
    public void writeToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        super.writeToXml(writer, this.getXmlElementName());
    }

    /**
     * Represents a search filter that checks for the presence of a substring
     * inside a text property. Applications can use ContainsSubstring to define
     * conditions such as "Field CONTAINS Value" or
     * "Field IS PREFIXED WITH Value".
     */
    public static final class ContainsSubstring extends PropertyBasedFilter {

        /**
         * The containment mode.
         */
        private ContainmentMode containmentMode = ContainmentMode.Substring;

        /**
         * The comparison mode.
         */
        private ComparisonMode comparisonMode = ComparisonMode.IgnoreCase;

        /**
         * The value.
         */
        private String value;

        /**
         * Initializes a new instance of the class.
         */
        public ContainsSubstring() {
            super();
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition The definition of the property that is being compared.
         * @param value              The value to compare with.
         */
        public ContainsSubstring(PropertyDefinitionBase propertyDefinition,
                                 String value) {
            super(propertyDefinition);
            this.value = value;
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition The definition of the property that is being compared.
         * @param value              The value to compare with.
         * @param containmentMode    The containment mode.
         * @param comparisonMode     The comparison mode.
         */
        public ContainsSubstring(PropertyDefinitionBase propertyDefinition,
                                 String value, ContainmentMode containmentMode,
                                 ComparisonMode comparisonMode) {
            this(propertyDefinition, value);
            this.containmentMode = containmentMode;
            this.comparisonMode = comparisonMode;
        }

        /**
         * validates instance.
         *
         * @throws ExchangeValidationException the service validation exception
         */
        @Override
        protected void internalValidate() throws ExchangeValidationException {
            super.internalValidate();
            if ((this.value == null) || this.value.isEmpty()) {
                throw new ExchangeValidationException("The Value property must be set.");
            }
        }

        /**
         * Gets the name of the XML element.
         *
         * @return the xml element name
         */
        @Override
        protected String getXmlElementName() {
            return XmlElementNames.Contains;
        }

        /**
         * Tries to read element from XML.
         *
         * @param reader the reader
         * @return True if element was read.
         */
        @Override
        public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
            boolean result = super.tryReadElementFromXml(reader);

            if (!result) {
                if (reader.getLocalName().equals(XmlElementNames.Constant)) {
                    this.value = reader.readAttributeValue(XmlAttributeNames.Value);
                    result = true;
                }
            }
            return result;
        }

        /**
         * Reads the attribute of Xml.
         *
         * @param reader the reader
         */
        @Override
        public void readAttributesFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {

            super.readAttributesFromXml(reader);
            this.containmentMode = reader.readAttributeValue(
                    ContainmentMode.class, XmlAttributeNames.ContainmentMode);
            try {
                this.comparisonMode = reader.readAttributeValue(
                        ComparisonMode.class,
                        XmlAttributeNames.ContainmentComparison);
            } catch (IllegalArgumentException ile) {
                // This will happen if we receive a value that is defined in the
                // EWS
                // schema but that is not defined
                // in the API. We map that
                // value to IgnoreCaseAndNonSpacingCharacters.
                this.comparisonMode = ComparisonMode.
                        IgnoreCaseAndNonSpacingCharacters;
            }
        }

        /**
         * Writes the attribute to XML.
         *
         * @param writer the writer
         */
        @Override
        public void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
            super.writeAttributesToXml(writer);
            writer.writeAttributeValue(XmlAttributeNames.ContainmentMode, this.containmentMode);
            writer.writeAttributeValue(XmlAttributeNames.ContainmentComparison, this.comparisonMode);
        }

        /**
         * Writes the elements to Xml.
         *
         * @param writer the writer
         */
        @Override
        public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
            super.writeElementsToXml(writer);
            writer.writeStartElement(XmlNamespace.Types, XmlElementNames.Constant);
            writer.writeAttributeValue(XmlAttributeNames.Value, this.value);
            writer.writeEndElement(); // Constant
        }

        /**
         * Gets the containment mode.
         *
         * @return ContainmentMode
         */
        public ContainmentMode getContainmentMode() {
            return containmentMode;
        }

        /**
         * sets the ContainmentMode.
         *
         * @param containmentMode the new containment mode
         */
        public void setContainmentMode(ContainmentMode containmentMode) {
            this.containmentMode = containmentMode;
        }

        /**
         * Gets the comparison mode.
         *
         * @return ComparisonMode
         */
        public ComparisonMode getComparisonMode() {
            return comparisonMode;
        }

        /**
         * sets the comparison mode.
         *
         * @param comparisonMode the new comparison mode
         */
        public void setComparisonMode(ComparisonMode comparisonMode) {
            this.comparisonMode = comparisonMode;
        }

        /**
         * gets the value to compare the specified property with.
         *
         * @return String
         */
        public String getValue() {
            return value;
        }

        /**
         * sets the value to compare the specified property with.
         *
         * @param value the new value
         */
        public void setValue(String value) {
            this.value = value;
        }
    }


    /**
     * Represents a bitmask exclusion search filter. Applications can use
     * ExcludesBitExcludesBitmaskFilter to define conditions such as
     * "(OrdinalField and 0x0010) != 0x0010"
     */
    public static class ExcludesBitmask extends PropertyBasedFilter {

        /**
         * The bitmask.
         */
        private int bitmask;

        /**
         * Initializes a new instance of the class.
         */
        public ExcludesBitmask() {
            super();
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition the property definition
         * @param bitmask            the bitmask
         */
        public ExcludesBitmask(PropertyDefinitionBase propertyDefinition,
                               int bitmask) {
            super(propertyDefinition);
            this.bitmask = bitmask;
        }

        /**
         * Gets the name of the XML element.
         *
         * @return XML element name
         */
        @Override
        public String getXmlElementName() {
            return XmlElementNames.Excludes;
        }

        /**
         * Tries to read element from XML.
         *
         * @param reader the reader
         * @return true if element was read
         */
        @Override
        public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
            boolean result = super.tryReadElementFromXml(reader);

            if (!result) {
                if (reader.getLocalName().equals(XmlElementNames.Bitmask)) {
                    // EWS always returns the Bitmask value in hexadecimal
                    this.bitmask = Integer.parseInt(reader.readAttributeValue(XmlAttributeNames.Value));
                }
            }

            return result;
        }

        /**
         * Writes the elements to XML.
         *
         * @param writer the writer
         */
        @Override
        public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
            super.writeElementsToXml(writer);

            writer.writeStartElement(XmlNamespace.Types, XmlElementNames.Bitmask);
            writer.writeAttributeValue(XmlAttributeNames.Value, this.bitmask);
            writer.writeEndElement(); // Bitmask
        }

        /**
         * Gets the bitmask to compare the property with.
         *
         * @return bitmask
         */
        public int getBitmask() {
            return bitmask;
        }

        /**
         * Sets the bitmask to compare the property with.
         *
         * @param bitmask the new bitmask
         */
        public void setBitmask(int bitmask) {
            this.bitmask = bitmask;
        }

    }


    /**
     * Represents a search filter checking if a field is set. Applications can
     * use ExistsFilter to define conditions such as "Field IS SET".
     */
    public static final class Exists extends PropertyBasedFilter {

        /**
         * Initializes a new instance of the class.
         */
        public Exists() {
            super();
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition the property definition
         */
        public Exists(PropertyDefinitionBase propertyDefinition) {
            super(propertyDefinition);
        }

        /**
         * Gets the name of the XML element.
         *
         * @return the xml element name
         */
        @Override
        protected String getXmlElementName() {
            return XmlElementNames.Exists;
        }
    }


    /**
     * Represents a search filter that checks if a property is equal to a given
     * value or other property.
     */
    public static class IsEqualTo extends RelationalFilter {

        /**
         * Initializes a new instance of the class.
         */
        public IsEqualTo() {
            super();
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition      The definition of the property that is being compared.
         * @param otherPropertyDefinition The definition of the property to compare with.
         */
        public IsEqualTo(PropertyDefinitionBase propertyDefinition,
                         PropertyDefinitionBase otherPropertyDefinition) {
            super(propertyDefinition, otherPropertyDefinition);
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition The definition of the property that is being compared.
         * @param value              The value of the property to compare with.
         */
        public IsEqualTo(PropertyDefinitionBase propertyDefinition,
                         Object value) {
            super(propertyDefinition, value);
        }

        /**
         * Gets the name of the XML element.
         *
         * @return the xml element name
         */
        @Override
        protected String getXmlElementName() {
            return XmlElementNames.IsEqualTo;
        }

    }


    /**
     * Represents a search filter that checks if a property is greater than a
     * given value or other property.
     */
    public static class IsGreaterThan extends RelationalFilter {

        /**
         * Initializes a new instance of the class.
         */
        public IsGreaterThan() {
            super();
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition      The definition of the property that is being compared.
         * @param otherPropertyDefinition The definition of the property to compare with.
         */
        public IsGreaterThan(PropertyDefinitionBase propertyDefinition,
                             PropertyDefinitionBase otherPropertyDefinition) {
            super(propertyDefinition, otherPropertyDefinition);
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition The definition of the property that is being compared.
         * @param value              The value of the property to compare with.
         */
        public IsGreaterThan(PropertyDefinitionBase propertyDefinition,
                             Object value) {
            super(propertyDefinition, value);
        }

        /**
         * Gets the name of the XML element.
         *
         * @return XML element name.
         */
        @Override
        protected String getXmlElementName() {
            return XmlElementNames.IsGreaterThan;
        }
    }


    /**
     * Represents a search filter that checks if a property is greater than or
     * equal to a given value or other property.
     */
    public static class IsGreaterThanOrEqualTo extends RelationalFilter {

        /**
         * Initializes a new instance of the class.
         */
        public IsGreaterThanOrEqualTo() {
            super();
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition      The definition of the property that is being compared.
         * @param otherPropertyDefinition The definition of the property to compare with.
         */
        public IsGreaterThanOrEqualTo(
                PropertyDefinitionBase propertyDefinition,
                PropertyDefinitionBase otherPropertyDefinition) {
            super(propertyDefinition, otherPropertyDefinition);
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition The definition of the property that is being compared.
         * @param value              The value of the property to compare with.
         */
        public IsGreaterThanOrEqualTo(
                PropertyDefinitionBase propertyDefinition, Object value) {
            super(propertyDefinition, value);
        }

        /**
         * Gets the name of the XML element. XML element name.
         *
         * @return the xml element name
         */
        @Override
        protected String getXmlElementName() {
            return XmlElementNames.IsGreaterThanOrEqualTo;
        }

    }


    /**
     * Represents a search filter that checks if a property is less than a given
     * value or other property.
     */
    public static class IsLessThan extends RelationalFilter {

        /**
         * Initializes a new instance of the class.
         */
        public IsLessThan() {
            super();
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition      The definition of the property that is being compared.
         * @param otherPropertyDefinition The definition of the property to compare with.
         */
        public IsLessThan(PropertyDefinitionBase propertyDefinition,
                          PropertyDefinitionBase otherPropertyDefinition) {
            super(propertyDefinition, otherPropertyDefinition);
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition The definition of the property that is being compared.
         * @param value              The value of the property to compare with.
         */
        public IsLessThan(PropertyDefinitionBase propertyDefinition,
                          Object value) {
            super(propertyDefinition, value);
        }

        /**
         * Gets the name of the XML element. XML element name.
         *
         * @return the xml element name
         */
        @Override
        protected String getXmlElementName() {
            return XmlElementNames.IsLessThan;
        }

    }


    /**
     * Represents a search filter that checks if a property is less than or
     * equal to a given value or other property.
     */
    public static class IsLessThanOrEqualTo extends RelationalFilter {

        /**
         * Initializes a new instance of the class.
         */
        public IsLessThanOrEqualTo() {
            super();
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition      The definition of the property that is being compared.
         * @param otherPropertyDefinition The definition of the property to compare with.
         */
        public IsLessThanOrEqualTo(PropertyDefinitionBase propertyDefinition,
                                   PropertyDefinitionBase otherPropertyDefinition) {
            super(propertyDefinition, otherPropertyDefinition);
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition The definition of the property that is being compared.
         * @param value              The value of the property to compare with.
         */
        public IsLessThanOrEqualTo(PropertyDefinitionBase propertyDefinition,
                                   Object value) {
            super(propertyDefinition, value);
        }

        /**
         * Gets the name of the XML element. XML element name.
         *
         * @return the xml element name
         */
        @Override
        protected String getXmlElementName() {
            return XmlElementNames.IsLessThanOrEqualTo;
        }

    }


    /**
     * Represents a search filter that checks if a property is not equal to a
     * given value or other property.
     */
    public static class IsNotEqualTo extends RelationalFilter {

        /**
         * Initializes a new instance of the class.
         */
        public IsNotEqualTo() {
            super();
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition      The definition of the property that is being compared.
         * @param otherPropertyDefinition The definition of the property to compare with.
         */
        public IsNotEqualTo(PropertyDefinitionBase propertyDefinition,
                            PropertyDefinitionBase otherPropertyDefinition) {
            super(propertyDefinition, otherPropertyDefinition);
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition The definition of the property that is being compared.
         * @param value              The value of the property to compare with.
         */
        public IsNotEqualTo(PropertyDefinitionBase propertyDefinition,
                            Object value) {
            super(propertyDefinition, value);
        }

        /**
         * Gets the name of the XML element.
         *
         * @return XML element name.
         */
        @Override
        protected String getXmlElementName() {
            return XmlElementNames.IsNotEqualTo;
        }

    }


    /**
     * Represents a search filter that negates another. Applications can use
     * NotFilter to define conditions such as "NOT(other filter)".
     */
    public static class Not extends SearchFilter implements IComplexPropertyChangedDelegate {

        /**
         * The search filter.
         */
        private SearchFilter searchFilter;

        /**
         * Initializes a new instance of the class.
         */
        public Not() {
            super();
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param searchFilter the search filter
         */
        public Not(SearchFilter searchFilter) {
            super();
            this.searchFilter = searchFilter;
        }

        /**
         * Search filter changed.
         *
         * @param complexProperty the complex property
         */
        private void searchFilterChanged(ComplexProperty complexProperty) {
            this.changed();
        }

        /**
         * validates the instance.
         *
         * @throws ExchangeValidationException the service validation exception
         */
        @Override
        protected void internalValidate() throws ExchangeValidationException {
            if (this.searchFilter == null) {
                throw new ExchangeValidationException("The SearchFilter property must be set.");
            }
        }

        /**
         * Gets the name of the XML element.
         *
         * @return the xml element name
         */
        @Override
        protected String getXmlElementName() {
            return XmlElementNames.Not;
        }

        /**
         * Tries to read element from XML.
         *
         * @param reader the reader
         * @return true if the element was read
         */
        @Override
        public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
            this.searchFilter = SearchFilter.loadFromXml(reader);
            return true;
        }

        /**
         * Writes the elements to XML.
         *
         * @param writer the writer
         */
        @Override
        public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
            this.searchFilter.writeToXml(writer);
        }

        /**
         * Gets  the search filter to negate. Available search filter
         * classes include SearchFilter.IsEqualTo,
         * SearchFilter.ContainsSubstring and
         * SearchFilter.SearchFilterCollection.
         *
         * @return SearchFilter
         */
        public SearchFilter getSearchFilter() {
            return searchFilter;
        }

        /**
         * Sets the search filter to negate. Available search filter classes
         * include SearchFilter.IsEqualTo, SearchFilter.ContainsSubstring and
         * SearchFilter.SearchFilterCollection.
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

        /*
         * (non-Javadoc)
         *
         * @see
         * microsoft.exchange.webservices.
         * ComplexPropertyChangedDelegateInterface#
         * complexPropertyChanged(microsoft.exchange.webservices.ComplexProperty
         * )
         */
        @Override
        public void complexPropertyChanged(ComplexProperty complexProperty) {
            searchFilterChanged(complexProperty);

        }
    }


    /**
     * Represents a search filter where an item or folder property is involved.
     */
    @EditorBrowsable(state = EditorBrowsableState.Never)
    public static abstract class PropertyBasedFilter extends SearchFilter {

        /**
         * The property definition.
         */
        private PropertyDefinitionBase propertyDefinition;

        /**
         * Initializes a new instance of the class.
         */
        PropertyBasedFilter() {
            super();
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition the property definition
         */
        PropertyBasedFilter(PropertyDefinitionBase propertyDefinition) {
            super();
            this.propertyDefinition = propertyDefinition;
        }

        /**
         * validate instance.
         *
         * @throws ExchangeValidationException the service validation exception
         */
        @Override
        protected void internalValidate() throws ExchangeValidationException {
            if (this.propertyDefinition == null) {
                throw new ExchangeValidationException("The PropertyDefinition property must be set.");
            }
        }

        /**
         * Tries to read element from XML.
         *
         * @param reader the reader
         * @return true if element was read
         * @throws Exception the exception
         */
        @Override
        public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
            OutParam<PropertyDefinitionBase> outParam =
                    new OutParam<PropertyDefinitionBase>();
            outParam.setParam(this.propertyDefinition);

            return PropertyDefinitionBase.tryLoadFromXml(reader, outParam);
        }

        /**
         * Writes the elements to XML.
         *
         * @param writer the writer
         */
        @Override
        public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
            this.propertyDefinition.writeToXml(writer);
        }

        /**
         * Gets the definition of the property that is involved in the search
         * filter.
         *
         * @return propertyDefinition
         */
        public PropertyDefinitionBase getPropertyDefinition() {
            return this.propertyDefinition;
        }

        /**
         * Sets the definition of the property that is involved in the search
         * filter.
         *
         * @param propertyDefinition the new property definition
         */
        public void setPropertyDefinition(
                PropertyDefinitionBase propertyDefinition) {
            this.propertyDefinition = propertyDefinition;
        }
    }


    /**
     * Represents the base class for relational filter (for example, IsEqualTo,
     * IsGreaterThan or IsLessThanOrEqualTo).
     */
    @EditorBrowsable(state = EditorBrowsableState.Never)
    public abstract static class RelationalFilter extends PropertyBasedFilter {

        /**
         * The other property definition.
         */
        private PropertyDefinitionBase otherPropertyDefinition;

        /**
         * The value.
         */
        private Object value;

        /**
         * Initializes a new instance of the class.
         */
        RelationalFilter() {
            super();
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition      The definition of the property that is being compared.
         * @param otherPropertyDefinition The definition of the property to compare with
         */
        RelationalFilter(PropertyDefinitionBase propertyDefinition,
                         PropertyDefinitionBase otherPropertyDefinition) {
            super(propertyDefinition);
            this.otherPropertyDefinition = otherPropertyDefinition;
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param propertyDefinition The definition of the property that is being compared.
         * @param value              The value to compare with.
         */
        RelationalFilter(PropertyDefinitionBase propertyDefinition,
                         Object value) {
            super(propertyDefinition);
            this.value = value;
        }

        /**
         * validates the instance.
         *
         * @throws ExchangeValidationException the service validation exception
         */
        @Override
        protected void internalValidate() throws ExchangeValidationException {
            super.internalValidate();

            if (this.otherPropertyDefinition == null && this.value == null) {
                throw new ExchangeValidationException(
                        "Either the OtherPropertyDefinition or the Value property must be set.");
            }
        }

        /**
         * Tries to read element from XML.
         *
         * @param reader the reader
         * @return true if element was read
         * @throws Exception the exception
         */
        @Override
        public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
            boolean result = super.tryReadElementFromXml(reader);
            if (!result) {
                if (reader.getLocalName().equals(XmlElementNames.FieldURIOrConstant)) {
                    reader.read();
                    reader.ensureCurrentNodeIsStartElement();
                    if (reader.isStartElement(XmlNamespace.Types, XmlElementNames.Constant)) {
                        this.value = reader.readAttributeValue(XmlAttributeNames.Value);
                        result = true;
                    } else {
                        OutParam<PropertyDefinitionBase> outParam = new OutParam<PropertyDefinitionBase>();
                        outParam.setParam(this.otherPropertyDefinition);
                        result = PropertyDefinitionBase.tryLoadFromXml(reader, outParam);
                    }
                }
            }

            return result;
        }

        /**
         * Writes the elements to XML.
         *
         * @param writer the writer
         */
        @Override
        public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
            super.writeElementsToXml(writer);

            writer.writeStartElement(XmlNamespace.Types,
                    XmlElementNames.FieldURIOrConstant);

            if (this.value != null) {
                writer.writeStartElement(XmlNamespace.Types,
                        XmlElementNames.Constant);
                writer.writeAttributeValue(XmlAttributeNames.Value,
                        true /* alwaysWriteEmptyString */, this.value);
                writer.writeEndElement(); // Constant
            } else {
                this.otherPropertyDefinition.writeToXml(writer);
            }

            writer.writeEndElement(); // FieldURIOrConstant
        }

        /**
         * Gets the definition of the property to compare with.
         *
         * @return otherPropertyDefinition
         */
        public PropertyDefinitionBase getOtherPropertyDefinition() {
            return this.otherPropertyDefinition;
        }

        /**
         * Sets the definition of the property to compare with.
         *
         * @param OtherPropertyDefinition the new other property definition
         */
        public void setOtherPropertyDefinition(
                PropertyDefinitionBase OtherPropertyDefinition) {
            this.otherPropertyDefinition = OtherPropertyDefinition;
            this.value = null;
        }

        /**
         * Gets the value of the property to compare with.
         *
         * @return the value
         */
        public Object getValue() {
            return value;
        }

        /**
         * Sets the value of the property to compare with.
         *
         * @param value the new value
         */
        public void setValue(Object value) {
            this.value = value;
            this.otherPropertyDefinition = null;
        }

        /**
         * gets Xml Element name.
         *
         * @return the xml element name
         */
        @Override
        protected String getXmlElementName() {
            return null;
        }
    }


    /**
     * Represents a collection of search filter linked by a logical operator.
     * Applications can use SearchFilterCollection to define complex search
     * filter such as "Condition1 AND Condition2".
     */
    public static class SearchFilterCollection extends SearchFilter implements
            Iterable<SearchFilter>, IComplexPropertyChangedDelegate {

        /**
         * The logical operator.
         */
        private LogicalOperator logicalOperator = LogicalOperator.And;

        /**
         * The search filter.
         */
        private final ArrayList<SearchFilter> searchFilters =
                new ArrayList<SearchFilter>();

        /**
         * Initializes a new instance of the class.
         */
        public SearchFilterCollection() {
            super();
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param logicalOperator The logical operator used to initialize the collection.
         */
        public SearchFilterCollection(LogicalOperator logicalOperator) {
            this.logicalOperator = logicalOperator;
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param logicalOperator The logical operator used to initialize the collection.
         * @param searchFilters   The search filter to add to the collection.
         */
        public SearchFilterCollection(LogicalOperator logicalOperator,
                                      SearchFilter... searchFilters) {
            this(logicalOperator);
            for (SearchFilter search : searchFilters) {
                Iterable<SearchFilter> searchFil = java.util.Arrays
                        .asList(search);
                this.addRange(searchFil);
            }
        }

        /**
         * Initializes a new instance of the class.
         *
         * @param logicalOperator The logical operator used to initialize the collection.
         * @param searchFilters   The search filter to add to the collection.
         */
        public SearchFilterCollection(LogicalOperator logicalOperator,
                                      Iterable<SearchFilter> searchFilters) {
            this(logicalOperator);
            this.addRange(searchFilters);
        }

        /**
         * Validate instance.
         *
         */
        @Override
        protected void internalValidate() throws ExchangeValidationException {
            for (int i = 0; i < this.getCount(); i++) {
                try {
                    this.searchFilters.get(i).internalValidate();
                } catch (ExchangeValidationException e) {
                    throw new ExchangeValidationException(String.format("The search filter at index %d is invalid.", i), e);
                }
            }
        }

        /**
         * A search filter has changed.
         *
         * @param complexProperty The complex property
         */
        private void searchFilterChanged(ComplexProperty complexProperty) {
            this.changed();
        }

        /**
         * Gets the name of the XML element.
         *
         * @return xml element name
         */
        @Override
        protected String getXmlElementName() {
            return this.logicalOperator.toString();
        }

        /**
         * Tries to read element from XML.
         *
         * @param reader the reader
         * @return true, if successful
         */
        @Override
        public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {

            this.add(SearchFilter.loadFromXml(reader));
            return true;
        }

        /**
         * Writes the elements to XML.
         *
         * @param writer the writer
         */
        @Override
        public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
            for (SearchFilter searchFilter : this.searchFilters) {
                searchFilter.writeToXml(writer);
            }
        }

        /**
         * Writes to XML.
         *
         * @param writer the writer
         */
        @Override
        public void writeToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
            // If there is only one filter in the collection, which developers
            // tend
            // to do,
            // we need to not emit the collection and instead only emit the one
            // filter within
            // the collection. This is to work around the fact that EWS does not
            // allow filter
            // collections that have less than two elements.
            if (this.getCount() == 1) {
                this.searchFilters.get(0).writeToXml(writer);
            } else {
                super.writeToXml(writer);
            }
        }

        /**
         * Adds a search filter of any type to the collection.
         *
         * @param searchFilter >The search filter to add. Available search filter classes
         *                     include SearchFilter.IsEqualTo,
         *                     SearchFilter.ContainsSubstring and
         *                     SearchFilter.SearchFilterCollection.
         */
        public void add(SearchFilter searchFilter) {
            if (searchFilter == null) {
                throw new IllegalArgumentException("searchFilter");
            }
            searchFilter.addOnChangeEvent(this);
            this.searchFilters.add(searchFilter);
            this.changed();
        }

        /**
         * Adds multiple search filter to the collection.
         *
         * @param searchFilters The search filter to add. Available search filter classes
         *                      include SearchFilter.IsEqualTo,
         *                      SearchFilter.ContainsSubstring and
         *                      SearchFilter.SearchFilterCollection
         */
        public void addRange(Iterable<SearchFilter> searchFilters) {
            if (searchFilters == null) {
                throw new IllegalArgumentException("searchFilters");
            }

            for (SearchFilter searchFilter : searchFilters) {
                searchFilter.addOnChangeEvent(this);
                this.searchFilters.add(searchFilter);
            }
            this.changed();
        }

        /**
         * Clears the collection.
         */
        public void clear() {
            if (this.getCount() > 0) {
                for (SearchFilter searchFilter : this.searchFilters) {
                    searchFilter.removeChangeEvent(this);
                }
                this.searchFilters.clear();
                this.changed();
            }
        }

        /**
         * Determines whether a specific search filter is in the collection.
         *
         * @param searchFilter The search filter to locate in the collection.
         * @return True is the search filter was found in the collection, false
         * otherwise.
         */
        public boolean contains(SearchFilter searchFilter) {
            return this.searchFilters.contains(searchFilter);
        }

        /**
         * Removes a search filter from the collection.
         *
         * @param searchFilter The search filter to remove
         */
        public void remove(SearchFilter searchFilter) {
            if (searchFilter == null) {
                throw new IllegalArgumentException("searchFilter");
            }

            if (this.contains(searchFilter)) {
                searchFilter.removeChangeEvent(this);
                this.searchFilters.remove(searchFilter);
                this.changed();
            }
        }

        /**
         * Removes the search filter at the specified index from the collection.
         *
         * @param index The zero-based index of the search filter to remove.
         */
        public void removeAt(int index) {
            if (index < 0 || index >= this.getCount()) {
                throw new IllegalArgumentException(
                        String.format("index %d is out of range [0..%d[.", index, this.getCount()));
            }

            this.searchFilters.get(index).removeChangeEvent(this);
            this.searchFilters.remove(index);
            this.changed();
        }

        /**
         * Gets the total number of search filter in the collection.
         *
         * @return the count
         */
        public int getCount() {

            return this.searchFilters.size();
        }

        /**
         * Gets the search filter at the specified index.
         *
         * @param index the index
         * @return The search filter at the specified index.
         */
        public SearchFilter getSearchFilter(int index) {
            if (index < 0 || index >= this.getCount()) {
                throw new IllegalArgumentException(
                        String.format("index %d is out of range [0..%d[.", index, this.getCount())
                );
            }
            return this.searchFilters.get(index);
        }

        /**
         * Sets the search filter at the specified index.
         *
         * @param index        the index
         * @param searchFilter the search filter
         */
        public void setSearchFilter(int index, SearchFilter searchFilter) {
            if (index < 0 || index >= this.getCount()) {
                throw new IllegalArgumentException(
                        String.format("index %d is out of range [0..%d[.", index, this.getCount())
                );
            }
            this.searchFilters.add(index, searchFilter);
        }

        /**
         * Gets the logical operator that links the serach filter in this
         * collection.
         *
         * @return LogicalOperator
         */
        public LogicalOperator getLogicalOperator() {
            return logicalOperator;
        }

        /**
         * Sets the logical operator that links the serach filter in this
         * collection.
         *
         * @param logicalOperator the new logical operator
         */
        public void setLogicalOperator(LogicalOperator logicalOperator) {
            this.logicalOperator = logicalOperator;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * microsoft.exchange.webservices.
         * ComplexPropertyChangedDelegateInterface#
         * complexPropertyChanged(microsoft.exchange.webservices.ComplexProperty
         * )
         */
        @Override
        public void complexPropertyChanged(ComplexProperty complexProperty) {
            searchFilterChanged(complexProperty);
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Iterable#iterator()
         */
        @Override
        public Iterator<SearchFilter> iterator() {
            return this.searchFilters.iterator();
        }

    }
}
