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
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.enumeration.property.StandardUser;
import com.eischet.ews.api.core.exception.service.local.ExchangeValidationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

/**
 * Represents a delegate user.
 */
public final class DelegateUser extends ComplexProperty {

    /**
     * The permissions.
     */
    private final DelegatePermissions permissions = new DelegatePermissions();
    /**
     * The user id.
     */
    private UserId userId = new UserId();
    /**
     * The receive copies of meeting messages.
     */
    private boolean receiveCopiesOfMeetingMessages;

    /**
     * The view private item.
     */
    private boolean viewPrivateItems;

    /**
     * Initializes a new instance of the <see cref="DelegateUser"/> class.
     */
    public DelegateUser() {
        super();
        this.receiveCopiesOfMeetingMessages = false;
        this.viewPrivateItems = false;
    }

    /**
     * Initializes a new instance of the <see cref="DelegateUser"/> class.
     *
     * @param primarySmtpAddress the primary smtp address
     */
    public DelegateUser(String primarySmtpAddress) {
        this();
        this.userId.setPrimarySmtpAddress(primarySmtpAddress);
    }

    /**
     * Initializes a new instance of the <see cref="DelegateUser"/> class.
     *
     * @param standardUser the standard user
     */
    public DelegateUser(StandardUser standardUser) {
        this();

        this.userId.setStandardUser(standardUser);
    }

    /**
     * Gets the user Id of the delegate user.
     *
     * @return the user id
     */
    public UserId getUserId() {
        return this.userId;
    }

    /**
     * Gets the list of delegate user's permissions.
     *
     * @return the permissions
     */
    public DelegatePermissions getPermissions() {
        return this.permissions;
    }

    /**
     * Gets  a value indicating if the delegate user should receive
     * copies of meeting request.
     *
     * @return the receive copies of meeting messages
     */
    public boolean getReceiveCopiesOfMeetingMessages() {
        return this.receiveCopiesOfMeetingMessages;

    }

    /**
     * Sets the receive copies of meeting messages.
     *
     * @param value the new receive copies of meeting messages
     */
    public void setReceiveCopiesOfMeetingMessages(boolean value) {
        this.receiveCopiesOfMeetingMessages = value;
    }

    /**
     * Gets  a value indicating if the delegate user should be
     * able to view the principal's private item.
     *
     * @return the view private item
     */
    public boolean getViewPrivateItems() {
        return this.viewPrivateItems;

    }

    /**
     * Gets  a value indicating if the delegate user should be able to
     * view the principal's private item.
     *
     * @param value the new view private item
     */
    public void setViewPrivateItems(boolean value) {

        this.viewPrivateItems = value;
    }

    /**
     * Tries to read element from XML.
     *
     * @param reader the reader
     * @return true, if successful
     */
    public boolean tryReadElementFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        if (reader.getLocalName().equals(XmlElementNames.UserId)) {

            this.userId = new UserId();
            this.userId.loadFromXml(reader, reader.getLocalName());
            return true;
        } else if (reader.getLocalName().equals(XmlElementNames.UserId)) {

            this.permissions.reset();
            this.permissions.loadFromXml(reader, reader.getLocalName());
            return true;
        } else if (reader.getLocalName().equals(
                XmlElementNames.ReceiveCopiesOfMeetingMessages)) {

            this.receiveCopiesOfMeetingMessages = reader
                    .readElementValue(Boolean.class);
            return true;
        } else if (reader.getLocalName().equals(
                XmlElementNames.ViewPrivateItems)) {

            this.viewPrivateItems = reader.readElementValue(Boolean.class);
            return true;
        } else {

            return false;
        }
    }

    /**
     * Writes elements to XML.
     *
     * @param writer the writer
     */
    public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        this.getUserId().writeToXml(writer, XmlElementNames.UserId);
        this.getPermissions().writeToXml(writer, XmlElementNames.DelegatePermissions);
        writer.writeElementValue(XmlNamespace.Types, XmlElementNames.ReceiveCopiesOfMeetingMessages, this.receiveCopiesOfMeetingMessages);
        writer.writeElementValue(XmlNamespace.Types, XmlElementNames.ViewPrivateItems, this.viewPrivateItems);
    }

    /**
     * Validates this instance.
     *
     * @throws ExchangeValidationException the service validation exception
     */
    protected void internalValidate() throws ExchangeValidationException {
        if (this.getUserId() == null) {
            throw new ExchangeValidationException("The UserId in the DelegateUser hasn't been specified.");
        } else if (!this.getUserId().isValid()) {
            throw new ExchangeValidationException(
                    "The UserId in the DelegateUser is invalid. The StandardUser, PrimarySmtpAddress or SID property must be set.");
        }
    }

    protected void validateAddDelegate() throws ExchangeValidationException {
        this.permissions.validateAddDelegate();
    }

    public void validateUpdateDelegate() throws Exception {
        this.permissions.validateUpdateDelegate();
    }
}
