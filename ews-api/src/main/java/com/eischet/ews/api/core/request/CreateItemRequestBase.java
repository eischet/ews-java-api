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
import com.eischet.ews.api.core.enumeration.service.MessageDisposition;
import com.eischet.ews.api.core.enumeration.service.SendInvitationsMode;
import com.eischet.ews.api.core.enumeration.service.error.ServiceErrorHandling;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.response.ServiceResponse;
import com.eischet.ews.api.core.service.ServiceObject;

import java.util.Collection;

/**
 * Represents an abstract CreateItem request.
 *
 * @param <TServiceObject> The type of the service object.
 * @param <TResponse>      The type of the response.
 */
abstract class CreateItemRequestBase<TServiceObject extends ServiceObject, TResponse extends ServiceResponse>
        extends CreateRequest<TServiceObject, TResponse> {

    /**
     * The message disposition.
     */
    private MessageDisposition messageDisposition = null;

    /**
     * The send invitations mode.
     */
    private SendInvitationsMode sendInvitationsMode = null;

    /**
     * Initializes a new instance.
     *
     * @param service           The service.
     * @param errorHandlingMode Indicates how errors should be handled.
     * @throws Exception on errors
     */
    protected CreateItemRequestBase(ExchangeService service, ServiceErrorHandling errorHandlingMode) throws Exception {
        super(service, errorHandlingMode);
    }

    /**
     * Validate the request.
     *
     * @throws Exception the exception
     */
    @Override
    protected void validate() throws Exception {
        super.validate();
        EwsUtilities.validateParam(this.getItems(), "Items");
    }

    /**
     * Gets the name of the XML element.
     *
     * @return XML element name.
     */
    @Override
    public String getXmlElementName() {
        return XmlElementNames.CreateItem;
    }

    /**
     * Gets the name of the response XML element.
     *
     * @return XML element name.
     */
    @Override
    protected String getResponseXmlElementName() {
        return XmlElementNames.CreateItemResponse;
    }

    /**
     * Gets the name of the response message XML element. XML element name.
     *
     * @return the response message xml element name
     */
    @Override
    protected String getResponseMessageXmlElementName() {
        return XmlElementNames.CreateItemResponseMessage;
    }

    /**
     * Gets the name of the parent folder XML element.
     *
     * @return XML element name.
     */
    @Override
    protected String getParentFolderXmlElementName() {
        return XmlElementNames.SavedItemFolderId;
    }

    /**
     * Gets the name of the object collection XML element.
     *
     * @return XML element name.
     */
    @Override
    protected String getObjectCollectionXmlElementName() {
        return XmlElementNames.Items;
    }

    /**
     * Writes the attribute to XML.
     *
     * @param writer The writer.
     */
    @Override
    protected void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        super.writeAttributesToXml(writer);
        if (this.messageDisposition != null) {
            writer.writeAttributeValue(XmlAttributeNames.MessageDisposition, this.getMessageDisposition());
        }
        if (this.sendInvitationsMode != null) {
            writer.writeAttributeValue(XmlAttributeNames.SendMeetingInvitations,
                    this.sendInvitationsMode);
        }
    }

    /**
     * Gets the message disposition.
     *
     * @return the message disposition
     */
    public MessageDisposition getMessageDisposition() {
        return messageDisposition;
    }

    /**
     * Sets the message disposition.
     *
     * @param value the new message disposition
     */
    public void setMessageDisposition(MessageDisposition value) {
        messageDisposition = value;
    }

    /**
     * Gets  the send invitations mode.
     *
     * @return the send invitations mode
     */
    public SendInvitationsMode getSendInvitationsMode() {
        return sendInvitationsMode;
    }

    /**
     * Sets the send invitations mode.
     *
     * @param value the new send invitations mode
     */
    public void setSendInvitationsMode(SendInvitationsMode value) {
        sendInvitationsMode = value;
    }

    /**
     * Gets  the item.
     *
     * @param value the new item
     */
    public void setItems(Collection<TServiceObject> value) {
        this.setObjects(value);
    }

    /**
     * Gets the item.
     *
     * @return the item
     */
    public Iterable<TServiceObject> getItems() {
        return this.getObjects();
    }

}
