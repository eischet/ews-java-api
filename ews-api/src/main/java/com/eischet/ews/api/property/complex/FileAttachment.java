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
import com.eischet.ews.api.core.enumeration.misc.ExchangeVersion;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.exception.service.local.ExchangeValidationException;
import com.eischet.ews.api.core.exception.service.local.ServiceVersionException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.service.item.Item;
import com.eischet.ews.api.util.IOUtils;

import java.io.*;

/**
 * Represents a file attachment.
 */
public final class FileAttachment extends Attachment {

    /**
     * The file name.
     */
    private String fileName;

    /**
     * The content stream.
     */
    private InputStream contentStream;

    /**
     * The content.
     */
    private byte[] content;

    /**
     * The load to stream.
     */
    private OutputStream loadToStream;

    /**
     * The is contact photo.
     */
    private boolean isContactPhoto;

    /**
     * Initializes a new instance.
     *
     * @param owner the owner
     */
    protected FileAttachment(Item owner) {
        super(owner);
    }

    /**
     * Gets the name of the XML element.
     *
     * @return XML element name
     */
    public String getXmlElementName() {
        return XmlElementNames.FileAttachment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate(int attachmentIndex) throws ExchangeValidationException {
        if ((this.fileName == null || this.fileName.isEmpty())
                && this.content == null && this.contentStream == null) {
            throw new ExchangeValidationException(String.format(
                    "The content of the file attachment at index %d must be set.",
                    attachmentIndex));
        }
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
            if (reader.getLocalName().equals(XmlElementNames.IsContactPhoto)) {
                this.isContactPhoto = reader.readElementValue(Boolean.class);
            } else if (reader.getLocalName().equals(XmlElementNames.Content)) {
                if (this.loadToStream != null) {
                    reader.writeBase64ElementValue(this.loadToStream);
                } else {
                    // If there's a file attachment content handler, use it.
                    // Otherwise
                    // load the content into a byte array.
                    // TODO: Should we mark the attachment to indicate that
                    // content is stored elsewhere?
                    if (reader.getService().getFileAttachmentContentHandler() != null) {
                        OutputStream outputStream = reader.getService()
                                .getFileAttachmentContentHandler()
                                .getOutputStream(getId());
                        if (outputStream != null) {
                            reader.writeBase64ElementValue(outputStream);
                        } else {
                            this.content = reader.writeBase64ElementValue();
                        }
                    } else {
                        this.content = reader.writeBase64ElementValue();
                    }
                }

                result = true;
            }
        }

        return result;
    }


    /**
     * For FileAttachment, the only thing need to patch is the AttachmentId.
     *
     * @param reader The reader.
     * @return true if element was read
     */
    @Override
    public boolean tryReadElementFromXmlToPatch(EwsServiceXmlReader reader) throws ExchangeXmlException {
        return super.tryReadElementFromXml(reader);
    }


    /**
     * Writes elements and content to XML.
     *
     * @param writer the writer
     */
    @Override
    public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        super.writeElementsToXml(writer);
        // ExchangeVersion ev=writer.getService().getRequestedServerVersion();
        if (writer.getService().getRequestedServerVersion().ordinal() >
                ExchangeVersion.Exchange2007_SP1
                        .ordinal()) {
            writer.writeElementValue(XmlNamespace.Types,
                    XmlElementNames.IsContactPhoto, this.isContactPhoto);
        }

        writer.writeStartElement(XmlNamespace.Types, XmlElementNames.Content);

        if (!(this.fileName == null || this.fileName.isEmpty())) {
            try {
                File fileStream = new File(this.fileName);
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(fileStream);
                    writer.writeBase64ElementValue(fis);
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
            } catch (IOException e) {
                throw new ExchangeXmlException("error reading file " + this.fileName + " as a file attachment", e);
            }

        } else if (this.contentStream != null) {
            writer.writeBase64ElementValue(this.contentStream);
        } else if (this.content != null) {
            writer.writeBase64ElementValue(this.content);
        } else {
            EwsUtilities
                    .ewsAssert(false, "FileAttachment.WriteElementsToXml", "The attachment's content is not set.");
        }

        writer.writeEndElement();
    }

    /**
     * Loads the content of the file attachment into the specified stream.
     * Calling this method results in a call to EWS.
     *
     * @param stream the stream
     * @throws Exception the exception
     */
    public void load(OutputStream stream) throws Exception {
        this.loadToStream = stream;

        try {
            this.load();
        } finally {
            this.loadToStream = null;
        }
    }

    /**
     * Loads the content of the file attachment into the specified file.
     * Calling this method results in a call to EWS.
     *
     * @param fileName the file name
     * @throws Exception the exception
     */
    public void load(String fileName) throws Exception {
        File fileStream = new File(fileName);

        try {
            this.loadToStream = new FileOutputStream(fileStream);
            this.load();
            this.loadToStream.flush();
        } finally {
            IOUtils.closeQuietly(this.loadToStream);
            this.loadToStream = null;
        }

        this.fileName = fileName;
        this.content = null;
        this.contentStream = null;
    }

    /**
     * Gets the name of the file the attachment is linked to.
     *
     * @return the file name
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Sets the file name.
     *
     * @param fileName the new file name
     */
    protected void setFileName(String fileName) {
        this.throwIfThisIsNotNew();

        this.fileName = fileName;
        this.content = null;
        this.contentStream = null;
    }

    /**
     * Gets  the content stream.Gets the name of the file the attachment
     * is linked to.
     *
     * @return The content stream
     */
    protected InputStream getContentStream() {
        return this.contentStream;
    }

    /**
     * Sets the content stream.
     *
     * @param contentStream the new content stream
     */
    protected void setContentStream(InputStream contentStream) {
        this.throwIfThisIsNotNew();

        this.contentStream = contentStream;
        this.content = null;
        this.fileName = null;
    }

    /**
     * Gets the content of the attachment into memory. Content is set only
     * when Load() is called.
     *
     * @return the content
     */
    public byte[] getContent() {
        return this.content;
    }

    /**
     * Sets the content.
     *
     * @param content the new content
     */
    protected void setContent(byte[] content) {
        this.throwIfThisIsNotNew();

        this.content = content;
        this.fileName = null;
        this.contentStream = null;
    }

    /**
     * Gets  a value indicating whether this attachment is a contact
     * photo.
     *
     * @return true, if is contact photo
     * @throws ServiceVersionException the service version exception
     */
    public boolean isContactPhoto() throws ExchangeXmlException {
        EwsUtilities.validatePropertyVersion(this.getOwner().getService(), ExchangeVersion.Exchange2010, "IsContactPhoto");
        return this.isContactPhoto;
    }

    /**
     * Sets the checks if is contact photo.
     *
     * @param isContactPhoto the new checks if is contact photo
     * @throws ServiceVersionException the service version exception
     */
    public void setIsContactPhoto(boolean isContactPhoto)
            throws ExchangeXmlException {
        EwsUtilities.validatePropertyVersion(this.getOwner().getService(),
                ExchangeVersion.Exchange2010, "IsContactPhoto");
        this.throwIfThisIsNotNew();
        this.isContactPhoto = isContactPhoto;
    }

}
