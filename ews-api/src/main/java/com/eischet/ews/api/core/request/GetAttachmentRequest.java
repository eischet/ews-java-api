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

import com.eischet.ews.api.core.*;
import com.eischet.ews.api.core.enumeration.misc.ExchangeVersion;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.enumeration.property.BodyType;
import com.eischet.ews.api.core.enumeration.service.error.ServiceErrorHandling;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.response.GetAttachmentResponse;
import com.eischet.ews.api.property.complex.Attachment;
import com.eischet.ews.api.property.definition.PropertyDefinitionBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a GetAttachment request.
 */
public final class GetAttachmentRequest extends MultiResponseServiceRequest<GetAttachmentResponse> {

    private final List<Attachment> attachments = new ArrayList<>();

    private final List<PropertyDefinitionBase> additionalProperties = new ArrayList<>();

    private BodyType bodyType;

    /**
     * Initializes a new instance of the GetAttachmentRequest class.
     *
     * @param service           The service.
     * @param errorHandlingMode Indicates how errors should be handled.
     * @throws Exception on errors
     */
    public GetAttachmentRequest(ExchangeService service, ServiceErrorHandling errorHandlingMode)
            throws Exception {
        super(service, errorHandlingMode);
    }

    /**
     * Validate request.
     *
     * @throws Exception the exception
     */
    @Override
    protected void validate() throws Exception {
        super.validate();
        EwsUtilities.validateParamCollection(this.getAttachments().iterator(), "Attachments");
        for (int i = 0; i < this.getAdditionalProperties().size(); i++) {
            EwsUtilities.validateParam(this.getAdditionalProperties().get(i),
                    String.format("AdditionalProperties[%d]", i));
        }
    }

    /**
     * Creates the service response.
     *
     * @param service       The service.
     * @param responseIndex Index of the response.
     * @return Service response.
     */
    @Override
    protected GetAttachmentResponse createServiceResponse(
            ExchangeService service, int responseIndex) {
        return new GetAttachmentResponse(this.getAttachments().get(
                responseIndex));
    }

    /**
     * Gets the expected response message count.
     *
     * @return Number of expected response messages.
     */
    @Override
    protected int getExpectedResponseMessageCount() {
        return this.getAttachments().size();
    }

    /**
     * Gets the name of the XML element.
     *
     * @return XML element name
     */
    @Override
    public String getXmlElementName() {
        return XmlElementNames.GetAttachment;
    }

    /**
     * Gets the name of the response XML element.
     *
     * @return XML element name
     */
    @Override
    protected String getResponseXmlElementName() {
        return XmlElementNames.GetAttachmentResponse;
    }

    /**
     * Gets the name of the response message XML element.
     *
     * @return XML element name
     */
    @Override
    protected String getResponseMessageXmlElementName() {
        return XmlElementNames.GetAttachmentResponseMessage;
    }

    @Override
    protected void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        if ((this.getBodyType() != null)
                || !this.getAdditionalProperties().isEmpty()) {
            writer.writeStartElement(XmlNamespace.Messages,
                    XmlElementNames.AttachmentShape);

            if (this.getBodyType() != null) {
                writer.writeElementValue(XmlNamespace.Types,
                        XmlElementNames.BodyType, this.getBodyType());
            }

            if (!this.getAdditionalProperties().isEmpty()) {
                PropertySet.writeAdditionalPropertiesToXml(writer, this.getAdditionalProperties().iterator());
            }

            writer.writeEndElement(); // AttachmentShape
        }

        writer.writeStartElement(XmlNamespace.Messages,
                XmlElementNames.AttachmentIds);

        for (Attachment attachment : this.getAttachments()) {
            writer.writeStartElement(XmlNamespace.Types,
                    XmlElementNames.AttachmentId);
            writer
                    .writeAttributeValue(XmlAttributeNames.Id, attachment
                            .getId());
            writer.writeEndElement();
        }

        writer.writeEndElement();
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
     * Gets the attachments.
     *
     * @return the attachments
     */
    public List<Attachment> getAttachments() {
        return this.attachments;
    }

    /**
     * Gets the additional property.
     *
     * @return the additional property
     */
    public List<PropertyDefinitionBase> getAdditionalProperties() {
        return this.additionalProperties;
    }

    /**
     * Gets  the type of the body.
     *
     * @return the body type
     */
    public BodyType getBodyType() {

        return this.bodyType;

    }

    /**
     * Sets the body type.
     *
     * @param bodyType the new body type
     */
    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

}
