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
import com.eischet.ews.api.core.response.ServiceResponse;
import com.eischet.ews.api.misc.UserConfiguration;

/**
 * Represents a CreateUserConfiguration request.
 */
public class CreateUserConfigurationRequest extends
        MultiResponseServiceRequest<ServiceResponse> {

    /**
     * The user configuration.
     */
    protected UserConfiguration userConfiguration;

    /**
     * Validate request.
     *
     * @throws Exception the exception
     */
    @Override
    protected void validate() throws Exception {
        super.validate();
        EwsUtilities.validateParam(this.userConfiguration, "userConfiguration");
    }

    /**
     * Creates the service response.
     *
     * @param service       The service.
     * @param responseIndex Index of the response.
     * @return Service response.
     */
    @Override
    protected ServiceResponse createServiceResponse(ExchangeService service,
                                                    int responseIndex) {
        return new ServiceResponse();
    }

    /**
     * Gets the request version.
     *
     * @return Earliest Exchange version in which this request is supported.
     */
    @Override
    protected ExchangeVersion getMinimumRequiredServerVersion() {
        return ExchangeVersion.Exchange2010;
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
     * @return XML element name,
     */
    @Override
    public String getXmlElementName() {
        return XmlElementNames.CreateUserConfiguration;
    }

    /**
     * Gets the name of the response XML element.
     *
     * @return XML element name,
     */
    @Override
    protected String getResponseXmlElementName() {
        return XmlElementNames.CreateUserConfigurationResponse;
    }

    /**
     * Gets the name of the response message XML element.
     *
     * @return XML element name,
     */
    @Override
    protected String getResponseMessageXmlElementName() {
        return XmlElementNames.CreateUserConfigurationResponseMessage;
    }

    /**
     * Writes XML elements.
     *
     * @param writer the writer
     * @throws Exception the exception
     */
    @Override
    protected void writeElementsToXml(EwsServiceXmlWriter writer)
            throws Exception {
        // Write UserConfiguation element
        this.userConfiguration.writeToXml(writer, XmlNamespace.Messages,
                XmlElementNames.UserConfiguration);
    }

    /**
     * Initializes a new instance of the <see
     * cref="CreateUserConfigurationRequest"/> class.
     *
     * @param service The service.
     * @throws Exception
     */
    public CreateUserConfigurationRequest(ExchangeService service)
            throws Exception {
        super(service, ServiceErrorHandling.ThrowOnError);
    }

    /**
     * Gets  the user configuration.
     *
     * @return The userConfiguration.
     */
    public UserConfiguration getUserConfiguration() {
        return this.userConfiguration;

    }

    /**
     * Sets the user configuration.
     *
     * @param value the new user configuration
     */
    public void setUserConfiguration(UserConfiguration value) {
        this.userConfiguration = value;
    }
}
