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

package com.eischet.ews.api.core.request;

import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.EwsUtilities;
import com.eischet.ews.api.core.ExchangeService;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.ExchangeVersion;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.enumeration.service.error.ServiceErrorHandling;
import com.eischet.ews.api.core.exception.service.local.ServiceLocalException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.response.ServiceResponse;
import com.eischet.ews.api.http.ExchangeHttpClient;

/**
 * The Class UnsubscribeRequest.
 */
public class UnsubscribeRequest extends MultiResponseServiceRequest<ServiceResponse> {

    /**
     * The subscription id.
     */
    private String subscriptionId;

    /**
     * Instantiates a new unsubscribe request.
     *
     * @param service the service
     * @throws Exception
     */
    public UnsubscribeRequest(ExchangeService service)
            throws Exception {
        super(service, ServiceErrorHandling.ThrowOnError);
    }

    /**
     * Creates service response.
     *
     * @param service       the service
     * @param responseIndex the response index
     * @return Service response.
     */
    @Override
    protected ServiceResponse createServiceResponse(ExchangeService service,
                                                    int responseIndex) {
        return new ServiceResponse();
    }

    /**
     * Gets the expected response message count.
     *
     * @return Number of expected response messages.
     */
    @Override
    protected int getExpectedResponseMessageCount() {
        return 1;
    }

    /**
     * Gets the name of the XML element.
     *
     * @return Xml element name.
     */
    @Override
    public String getXmlElementName() {
        return XmlElementNames.Unsubscribe;
    }

    /**
     * Gets the name of the response XML element.
     *
     * @return Xml element name.
     */
    @Override
    protected String getResponseXmlElementName() {
        return XmlElementNames.UnsubscribeResponse;
    }

    /**
     * Gets the name of the response message XML element.
     *
     * @return Xml element name.
     */
    @Override
    protected String getResponseMessageXmlElementName() {
        return XmlElementNames.UnsubscribeResponseMessage;
    }

    /**
     * Validate the request.
     *
     * @throws ServiceLocalException the service local exception
     * @throws Exception             the exception
     */
    @Override
    protected void validate() throws ServiceLocalException, Exception {
        super.validate();
        EwsUtilities.validateNonBlankStringParam(this.
                getSubscriptionId(), "SubscriptionId");

    }

    @Override
    protected void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeElementValue(XmlNamespace.Messages,
                XmlElementNames.SubscriptionId, this.getSubscriptionId());
    }

    /**
     * Gets the request version.
     *
     * @return Earliest Exchange version in which this request is supported.
     */
    @Override
    protected ExchangeVersion getMinimumRequiredServerVersion() {
        return ExchangeVersion.Exchange2007_SP1;
    }

    /**
     * Gets the subscription id.
     *
     * @return the subscription id
     */
    public String getSubscriptionId() {
        return this.subscriptionId;
    }

    /**
     * Sets the subscription id.
     *
     * @param subscriptionId the new subscription id
     */
    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    @Override
    protected ExchangeHttpClient.Request buildEwsHttpWebRequest() throws Exception {
        return super.buildEwsHttpPoolingWebRequest();
    }
}
