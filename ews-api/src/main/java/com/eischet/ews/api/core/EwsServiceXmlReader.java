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

package com.eischet.ews.api.core;

import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.response.IGetObjectInstanceDelegate;
import com.eischet.ews.api.core.service.ServiceObject;
import com.eischet.ews.api.util.DateTimeUtils;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * XML reader.
 */
public class EwsServiceXmlReader extends EwsXmlReader {

    /**
     * The service.
     */
    private ExchangeService service;

    /**
     * Initializes a new instance of the EwsXmlReader class.
     *
     * @param stream  the stream
     * @param service the service
     */
    public EwsServiceXmlReader(InputStream stream, ExchangeService service) throws ExchangeXmlException {
        super(stream);
        this.service = service;
    }

    /**
     * Reads the element value as date time.
     *
     * @return Element value
     * @throws Exception the exception
     */
    public LocalDateTime readElementValueAsDateTime() throws ExchangeXmlException {
        return DateTimeUtils.parseDateTime(readElementValue());
    }

    /**
     * Reads the element value as unspecified date.
     *
     * @return element value
     */
    public LocalDate readElementValueAsUnspecifiedDate() throws ExchangeXmlException {
        return DateTimeUtils.parseDateOnly(readElementValue());
    }

    /**
     * Reads the element value as date time, assuming it is unbiased (e.g.
     * 2009/01/01T08:00) and scoped to service's time zone.
     *
     * @return Date
     * @throws Exception the exception
     */
    public LocalDateTime readElementValueAsUnbiasedDateTimeScopedToServiceTimeZone() throws ExchangeXmlException {
        return DateTimeUtils.parseDateTime(this.readElementValue());
    }

    /**
     * Reads the element value as date time.
     *
     * @param xmlNamespace the xml namespace
     * @param localName    the local name
     * @return the date
     * @throws Exception the exception
     */
    public LocalDateTime readElementValueAsDateTime(XmlNamespace xmlNamespace, String localName) throws ExchangeXmlException {
        return DateTimeUtils.parseDateTime(readElementValue(xmlNamespace, localName));
    }

    /**
     * Reads the service objects collection from XML.
     *
     * @param <TServiceObject>          the generic type
     * @param collectionXmlElementName  the collection xml element name
     * @param getObjectInstanceDelegate the get object instance delegate
     * @param clearPropertyBag          the clear property bag
     * @param requestedPropertySet      the requested property set
     * @param summaryPropertiesOnly     the summary property only
     * @return the list
     */
    public <TServiceObject extends ServiceObject> List<TServiceObject>
    readServiceObjectsCollectionFromXml(
            String collectionXmlElementName,
            IGetObjectInstanceDelegate<ServiceObject>
                    getObjectInstanceDelegate,
            boolean clearPropertyBag, PropertySet requestedPropertySet,
            boolean summaryPropertiesOnly) throws ExchangeXmlException {

        List<TServiceObject> serviceObjects = new ArrayList<>();
        TServiceObject serviceObject;

        this.readStartElement(XmlNamespace.Messages, collectionXmlElementName);

        if (!this.isEmptyElement()) {
            do {
                this.read();

                if (this.isStartElement()) {
                    serviceObject = (TServiceObject) getObjectInstanceDelegate.getObjectInstanceDelegate(this.getService(), this.getLocalName());
                    if (serviceObject == null) {
                        this.skipCurrentElement();
                    } else {
                        if (!(this.getLocalName()).equals(serviceObject
                                .getXmlElementName())) {

                            throw new ExchangeXmlException(String.format(
                                            "The type of the " + "object in " +
                                                    "the store (%s)" +
                                                    " does not match that" +
                                                    " of the " +
                                                    "local object (%s).",
                                            this.getLocalName(), serviceObject
                                                    .getXmlElementName()));
                        }
                        serviceObject.loadFromXml(this, clearPropertyBag,
                                requestedPropertySet, summaryPropertiesOnly);

                        serviceObjects.add(serviceObject);
                    }
                }
            } while (!this.isEndElement(XmlNamespace.Messages,
                    collectionXmlElementName));
        } else {
            // For empty elements read End Element tag
            // i.e. position cursor on End Element
            this.read();
        }

        return serviceObjects;

    }

    /**
     * Gets the service.
     *
     * @return the service
     */
    public ExchangeService getService() {
        return service;
    }

    /**
     * Sets the service.
     *
     * @param service the new service
     */
    public void setService(ExchangeService service) {
        this.service = service;
    }

}
