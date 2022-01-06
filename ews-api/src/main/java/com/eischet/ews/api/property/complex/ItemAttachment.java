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
import com.eischet.ews.api.core.EwsUtilities;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.property.BodyType;
import com.eischet.ews.api.core.exception.service.local.ServiceLocalException;
import com.eischet.ews.api.core.exception.service.local.ServiceValidationException;
import com.eischet.ews.api.core.service.ServiceObject;
import com.eischet.ews.api.core.service.item.Item;
import com.eischet.ews.api.property.definition.PropertyDefinitionBase;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents an item attachment.
 */
public class ItemAttachment extends Attachment implements IServiceObjectChangedDelegate {

    private static final Logger LOG = Logger.getLogger(ItemAttachment.class.getCanonicalName());

    /**
     * The item.
     */
    private Item item;

    /**
     * Initializes a new instance of the class.
     *
     * @param owner The owner of the attachment
     */
    protected ItemAttachment(Item owner) {
        super(owner);
    }

    /**
     * Gets the item associated with the attachment.
     *
     * @return the item
     */
    public Item getItem() {
        return this.item;
    }

    /**
     * Sets the item associated with the attachment.
     *
     * @param item the new item
     */
    protected void setItem(Item item) {
        this.throwIfThisIsNotNew();

        if (this.item != null) {

            this.item.removeServiceObjectChangedEvent(this);
        }
        this.item = item;
        if (this.item != null) {
            this.item.addServiceObjectChangedEvent(this);
        }
    }

    /**
     * Implements the OnChange event handler for the item associated with the
     * attachment.
     *
     * @param serviceObject ,The service object that triggered the OnChange event.
     */
    private void itemChanged(ServiceObject serviceObject) {
        this.item.getPropertyBag().changed();
    }

    /**
     * Obtains EWS XML element name for this object.
     *
     * @return The XML element name.
     */
    @Override
    public String getXmlElementName() {
        return XmlElementNames.ItemAttachment;
    }

    /**
     * Tries to read the element at the current position of the reader.
     *
     * @param reader the reader
     * @return True if the element was read, false otherwise.
     * @throws Exception the exception
     */
    @Override
    public boolean tryReadElementFromXml(EwsServiceXmlReader reader)
            throws Exception {
        boolean result = super.tryReadElementFromXml(reader);

        if (!result) {
            this.item = EwsUtilities.createItemFromXmlElementName(this, reader.getLocalName());

            if (this.item != null) {
                try {
                    this.item.loadFromXml(reader, true /* clearPropertyBag */);
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "error reading XML", e);
                }
            }
        }

        return result;
    }

    /**
     * For ItemAttachment, AttachmentId and Item should be patched.
     *
     * @param reader The reader.
     *               <p/>
     *               True if element was read.
     */
    public boolean tryReadElementFromXmlToPatch(EwsServiceXmlReader reader) throws Exception {
        // update the attachment id.
        super.tryReadElementFromXml(reader);

        reader.read();

        String localName = reader.getLocalName();
        Class<?> itemClass = EwsUtilities.getItemTypeFromXmlElementName(localName);

        if (itemClass != null) {
            if (item == null || item.getClass() != itemClass) {
                throw new ServiceLocalException(
                        "Attachment item type mismatch.");
            }

            this.item.loadFromXml(reader, false /* clearPropertyBag */);
            return true;
        }

        return false;
    }


    /**
     * Writes the property of this object as XML elements.
     *
     * @param writer ,The writer to write the elements to.
     * @throws Exception the exception
     */
    @Override
    public void writeElementsToXml(EwsServiceXmlWriter writer)
            throws Exception {
        super.writeElementsToXml(writer);
        try {
            this.item.writeToXml(writer);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error writing XML", e);

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate(int attachmentIndex) throws Exception {
        if (this.getName() == null || this.getName().isEmpty()) {
            throw new ServiceValidationException(String.format(
                    "The name of the item attachment at index %d must be set.", attachmentIndex));
        }

        // Recurse through any item attached to item attachment.
        this.validate();
    }

    /**
     * Loads this attachment.
     *
     * @param additionalProperties the additional property
     * @throws Exception the exception
     */
    public void load(PropertyDefinitionBase... additionalProperties)
            throws Exception {
        internalLoad(null /* bodyType */, Arrays.asList(additionalProperties));
    }

    /**
     * Loads this attachment.
     *
     * @param additionalProperties the additional property
     * @throws Exception the exception
     */
    public void load(Iterable<PropertyDefinitionBase> additionalProperties)
            throws Exception {
        this.internalLoad(null, additionalProperties);
    }

    /**
     * Loads this attachment.
     *
     * @param bodyType             the body type
     * @param additionalProperties the additional property
     * @throws Exception the exception
     */
    public void load(BodyType bodyType,
                     PropertyDefinitionBase... additionalProperties) throws Exception {
        internalLoad(bodyType, Arrays.asList(additionalProperties));
    }

    /**
     * Loads this attachment.
     *
     * @param bodyType             the body type
     * @param additionalProperties the additional property
     * @throws Exception the exception
     */
    public void load(BodyType bodyType,
                     Iterable<PropertyDefinitionBase> additionalProperties)
            throws Exception {
        this.internalLoad(bodyType, additionalProperties);
    }

    /**
     * Service object changed.
     *
     * @param serviceObject accepts ServiceObject
     */
    @Override
    public void serviceObjectChanged(ServiceObject serviceObject) {
        this.itemChanged(serviceObject);
    }

}
