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

package com.eischet.ews.api.autodiscover.request;

import com.eischet.ews.api.autodiscover.AutodiscoverService;
import com.eischet.ews.api.autodiscover.enumeration.AutodiscoverErrorCode;
import com.eischet.ews.api.autodiscover.enumeration.DomainSettingName;
import com.eischet.ews.api.autodiscover.response.AutodiscoverResponse;
import com.eischet.ews.api.autodiscover.response.GetDomainSettingsResponseCollection;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.EwsUtilities;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.ExchangeVersion;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.exception.service.local.ExchangeValidationException;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

import javax.xml.stream.XMLStreamException;
import java.net.URI;
import java.util.List;

/**
 * Represents a GetDomainSettings request.
 */
public class GetDomainSettingsRequest extends AutodiscoverRequest {

    /**
     * Action Uri of Autodiscover.GetDomainSettings method.
     */
    private static final String GetDomainSettingsActionUri = EwsUtilities.AutodiscoverSoapNamespace + "/Autodiscover/GetDomainSettings";

    /**
     * The domains.
     */
    private List<String> domains;

    /**
     * The settings.
     */
    private List<DomainSettingName> settings;

    private ExchangeVersion requestedVersion;

    /**
     * Initializes a new instance of the {@link GetDomainSettingsRequest} class.
     *
     * @param service the service
     * @param url     the url
     */
    public GetDomainSettingsRequest(AutodiscoverService service, URI url) {
        super(service, url);
    }

    /**
     * Validates the request.
     *
     * @throws Exception the exception
     */
    @Override
    protected void validate() throws Exception {
        super.validate();

        EwsUtilities.validateParam(this.getDomains(), "domains");
        EwsUtilities.validateParam(this.getSettings(), "settings");

        if (this.getSettings().size() == 0) {
            throw new ExchangeValidationException("At least one setting must be requested.");
        }

        if (domains.size() == 0) {
            throw new ExchangeValidationException("At least one domain name must be requested.");
        }

        for (String domain : this.getDomains()) {
            if (domain == null || domain.isEmpty()) {
                throw new ExchangeValidationException("The domain name must be specified.");
            }
        }
    }

    /**
     * Executes this instance.
     *
     * @return the gets the domain settings response collection
     * @throws Exception the exception
     */
    public GetDomainSettingsResponseCollection execute() throws Exception {
        GetDomainSettingsResponseCollection responses = (GetDomainSettingsResponseCollection) this.internalExecute();
        if (responses.getErrorCode() == AutodiscoverErrorCode.NoError) {
            this.PostProcessResponses(responses);
        }
        return responses;
    }

    /**
     * Post-process response to GetDomainSettings.
     *
     * @param responses The GetDomainSettings response.
     */
    private void PostProcessResponses(GetDomainSettingsResponseCollection responses) {
        // Note:The response collection may not include all of the requested
        // domains if the request has been throttled.
        for (int index = 0; index < responses.getCount(); index++) {
            responses.getResponses().get(index).setDomain(this.getDomains().get(index));
        }
    }

    /**
     * Gets the name of the request XML element.
     *
     * @return Request XML element name.
     */
    @Override
    protected String getRequestXmlElementName() {
        return XmlElementNames.GetDomainSettingsRequestMessage;
    }

    /**
     * Gets the name of the response XML element.
     *
     * @return Response XML element name.
     */
    @Override
    protected String getResponseXmlElementName() {
        return XmlElementNames.GetDomainSettingsResponseMessage;
    }

    /**
     * Gets the WS-Addressing action name.
     *
     * @return WS-Addressing action name.
     */
    @Override
    protected String getWsAddressingActionName() {
        return GetDomainSettingsActionUri;
    }

    /**
     * Creates the service response.
     *
     * @return AutodiscoverResponse
     */
    @Override
    protected AutodiscoverResponse createServiceResponse() {
        return new GetDomainSettingsResponseCollection();
    }

    /**
     * Writes the attribute to XML.
     *
     * @param writer The writer.
     * @throws ServiceXmlSerializationException the service xml serialization exception
     */
    @Override
    protected void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeAttributeValue("xmlns", EwsUtilities.AutodiscoverSoapNamespacePrefix, EwsUtilities.AutodiscoverSoapNamespace);
    }

    /**
     * Writes request to XML.
     *
     * @param writer the writer
     * @throws XMLStreamException               the XML stream exception
     * @throws ServiceXmlSerializationException the service xml serialization exception
     */
    @Override
    protected void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeStartElement(XmlNamespace.Autodiscover, XmlElementNames.Request);

        writer.writeStartElement(XmlNamespace.Autodiscover, XmlElementNames.Domains);

        for (String domain : this.getDomains()) {
            if (!(domain == null || domain.isEmpty())) {
                writer.writeElementValue(XmlNamespace.Autodiscover, XmlElementNames.Domain, domain);
            }
        }
        writer.writeEndElement(); // Domains

        writer.writeStartElement(XmlNamespace.Autodiscover, XmlElementNames.RequestedSettings);
        for (DomainSettingName setting : settings) {
            writer.writeElementValue(XmlNamespace.Autodiscover, XmlElementNames.Setting, setting);
        }

        writer.writeEndElement(); // RequestedSettings

        if (this.requestedVersion != null) {
            writer.writeElementValue(XmlNamespace.Autodiscover, XmlElementNames.RequestedVersion, this.requestedVersion);
        }

        writer.writeEndElement(); // Request
    }

    /**
     * Gets  the domains.
     *
     * @return the domains
     */
    protected List<String> getDomains() {
        return domains;
    }

    /**
     * Sets the domains.
     *
     * @param value the new domains
     */
    public void setDomains(List<String> value) {
        domains = value;
    }

    /**
     * Gets or sets the settings.
     *
     * @return the settings
     */
    protected List<DomainSettingName> getSettings() {
        return settings;
    }

    /**
     * Sets the settings.
     *
     * @param value the new settings
     */
    public void setSettings(List<DomainSettingName> value) {
        settings = value;
    }

    /**
     * Gets or sets the requestedVersion.
     *
     * @return the requestedVersion
     */
    protected ExchangeVersion getRequestedVersion() {
        return requestedVersion;
    }

    /**
     * Sets the requestedVersion.
     *
     * @param value the new requestedVersion
     */
    public void setRequestedVersion(ExchangeVersion value) {
        requestedVersion = value;
    }

}
