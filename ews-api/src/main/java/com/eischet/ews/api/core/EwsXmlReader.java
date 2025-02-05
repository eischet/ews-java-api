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
import com.eischet.ews.api.core.exception.service.local.ServiceXmlDeserializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.misc.OutParam;
import com.eischet.ews.api.security.XmlNodeType;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Defines the EwsXmlReader class.
 */
public class EwsXmlReader {

    private static final Logger LOG = Logger.getLogger(EwsXmlReader.class.getCanonicalName());

    /**
     * The xml reader.
     */
    private XMLEventReader xmlReader;

    /**
     * The present event.
     */
    private XMLEvent presentEvent;

    /**
     * The prev event.
     */
    private XMLEvent prevEvent;

    /**
     * Initializes a new instance of the EwsXmlReader class.
     *
     * @param stream the stream
     */
    public EwsXmlReader(InputStream stream) throws ExchangeXmlException {
        this.xmlReader = initializeXmlReader(stream);
    }

    /**
     * Initializes the XML reader.
     *
     * @param stream the stream
     * @return An XML reader to use.
     */
    protected XMLEventReader initializeXmlReader(InputStream stream) throws ExchangeXmlException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        try {
            return inputFactory.createXMLEventReader(stream);
        } catch (XMLStreamException e) {
            throw new ExchangeXmlException("error initializing XMLInputFactory", e);
        }
    }


    /**
     * Formats the name of the element.
     *
     * @param namespacePrefix  The namespace prefix
     * @param localElementName Element name
     * @return the string
     */
    private static String formatElementName(String namespacePrefix,
                                            String localElementName) {

        return isNullOrEmpty(namespacePrefix) ? localElementName :
                namespacePrefix + ":" + localElementName;
    }

    /**
     * Read XML element.
     *
     * @param xmlNamespace The XML namespace
     * @param localName    Name of the local
     * @param nodeType     Type of the node
     */
    private void internalReadElement(XmlNamespace xmlNamespace, String localName, XmlNodeType nodeType) throws ExchangeXmlException {

        if (xmlNamespace == XmlNamespace.NotSpecified) {
            this.internalReadElement("", localName, nodeType);
        } else {
            this.read(nodeType);

            if ((!this.getLocalName().equals(localName)) ||
                    (!this.getNamespaceUri().equals(EwsUtilities
                            .getNamespaceUri(xmlNamespace)))) {
                throw new ExchangeXmlException(
                        String
                                .format(
                                        "An element node '%s:%s' of the type %s was expected, but node '%s' of type %s was found.",
                                        EwsUtilities
                                                .getNamespacePrefix(
                                                        xmlNamespace),
                                        localName, nodeType.toString(), this
                                                .getName(), this.getNodeType()
                                                .toString()));
            }
        }
    }

    /**
     * Read XML element.
     *
     * @param namespacePrefix The namespace prefix
     * @param localName       Name of the local
     * @param nodeType        Type of the node
     */
    private void internalReadElement(String namespacePrefix, String localName, XmlNodeType nodeType) throws ExchangeXmlException {
        read(nodeType);

        if ((!this.getLocalName().equals(localName)) ||
                (!this.getNamespacePrefix().equals(namespacePrefix))) {
            throw new ExchangeXmlException(String.format(
                    "An element node '%s:%s' of the type %s was expected, but node '%s' of type %s was found.", namespacePrefix, localName,
                    nodeType.toString(), this.getName(), this.getNodeType()
                            .toString()));
        }
    }

    /**
     * Reads the specified node type.
     */
    public void read() throws ExchangeXmlException {
        read(false);
    }

    /**
     * Reads the specified node type.
     *
     * @param keepWhiteSpace Do not remove whitespace characters if true
     */
    private void read(boolean keepWhiteSpace) throws ExchangeXmlException {
        // The caller to EwsXmlReader.Read expects
        // that there's another node to
        // read. Throw an exception if not true.
        while (true) {
            if (!xmlReader.hasNext()) {
                throw new ExchangeXmlException("Unexpected end of XML document.");
            } else {
                try {
                    XMLEvent event = xmlReader.nextEvent();
                    if (event.getEventType() == XMLStreamConstants.CHARACTERS) {
                        Characters characters = (Characters) event;
                        if (!keepWhiteSpace)
                            if (characters.isIgnorableWhiteSpace()
                                    || characters.isWhiteSpace()) {
                                continue;
                            }
                    }
                    this.prevEvent = this.presentEvent;
                    this.presentEvent = event;
                    break;
                } catch (XMLStreamException e) {
                    throw new ExchangeXmlException("error reading next XML event", e);
                }
            }
        }
    }

    /**
     * Reads the specified node type.
     *
     * @param nodeType Type of the node.
     */
    public void read(XmlNodeType nodeType) throws ExchangeXmlException {
        this.read();
        if (!Objects.equals(this.getNodeType(), nodeType)) {
            throw new ExchangeXmlException(String.format("The expected XML node type was %s, but the actual type is %s.", nodeType, this.getNodeType()));
        }
    }

    /**
     * Read attribute value from QName.
     *
     * @param qName QName of the attribute
     * @return Attribute Value
     * @throws com.eischet.ews.api.core.exception.ExchangeException thrown if attribute value can not be read
     */
    private String readAttributeValue(QName qName) throws ExchangeXmlException {
        if (this.presentEvent.isStartElement()) {
            StartElement startElement = this.presentEvent.asStartElement();
            Attribute attr = startElement.getAttributeByName(qName);
            if (null != attr) {
                return attr.getValue();
            } else {
                return null;
            }
        } else {
            String errMsg = String.format("Could not fetch attribute %s", qName.toString());
            throw new ExchangeXmlException(errMsg);
        }
    }

    /**
     * Reads the attribute value.
     *
     * @param xmlNamespace  The XML namespace.
     * @param attributeName Name of the attribute
     * @return Attribute Value
     */
    public String readAttributeValue(XmlNamespace xmlNamespace, String attributeName) throws ExchangeXmlException {
        if (xmlNamespace == XmlNamespace.NotSpecified) {
            return this.readAttributeValue(attributeName);
        } else {
            QName qName = new QName(EwsUtilities.getNamespaceUri(xmlNamespace),
                    attributeName);
            return readAttributeValue(qName);
        }
    }

    /**
     * Reads the attribute value.
     *
     * @param attributeName Name of the attribute
     * @return Attribute value.
     */
    public String readAttributeValue(String attributeName) throws ExchangeXmlException {
        QName qName = new QName(attributeName);
        return readAttributeValue(qName);
    }

    /**
     * Reads the attribute value.
     *
     * @param <T>           the generic type
     * @param cls           the cls
     * @param attributeName the attribute name
     * @return T
     */
    public <T> T readAttributeValue(Class<T> cls, String attributeName) throws ExchangeXmlException {
        return EwsUtilities.parse(cls, this.readAttributeValue(attributeName));
    }

    /**
     * Reads a nullable attribute value.
     *
     * @param <T>           the generic type
     * @param cls           the cls
     * @param attributeName the attribute name
     * @return T
     */
    public <T> T readNullableAttributeValue(Class<T> cls, String attributeName) throws ExchangeXmlException {
        String attributeValue = this.readAttributeValue(attributeName);
        if (attributeValue == null) {
            return null;
        } else {
            return EwsUtilities.parse(cls, attributeValue);
        }
    }

    /**
     * Reads the element value.
     *
     * @param namespacePrefix the namespace prefix
     * @param localName       the local name
     * @return String
     */
    public String readElementValue(String namespacePrefix, String localName) throws ExchangeXmlException {
        if (!this.isStartElement(namespacePrefix, localName)) {
            this.readStartElement(namespacePrefix, localName);
        }

        String value = null;

        if (!this.isEmptyElement()) {
            value = this.readValue();
        }
        return value;
    }

    /**
     * Reads the element value.
     *
     * @param xmlNamespace the xml namespace
     * @param localName    the local name
     * @return String
     */
    public String readElementValue(XmlNamespace xmlNamespace, String localName) throws ExchangeXmlException {

        if (!this.isStartElement(xmlNamespace, localName)) {
            this.readStartElement(xmlNamespace, localName);
        }

        String value = null;

        if (!this.isEmptyElement()) {
            value = this.readValue();
        } else {
            this.read();
        }

        return value;
    }

    /**
     * Read element value.
     *
     * @return String
     */
    public String readElementValue() throws ExchangeXmlException {
        this.ensureCurrentNodeIsStartElement();

        return this.readElementValue(this.getNamespacePrefix(), this
                .getLocalName());
    }

    /**
     * Reads the element value.
     *
     * @param <T>          the generic type
     * @param cls          the cls
     * @param xmlNamespace the xml namespace
     * @param localName    the local name
     * @return T
     */
    public <T> T readElementValue(Class<T> cls, XmlNamespace xmlNamespace, String localName) throws ExchangeXmlException {
        if (!this.isStartElement(xmlNamespace, localName)) {
            this.readStartElement(xmlNamespace, localName);
        }

        T value = null;

        if (!this.isEmptyElement()) {
            value = this.readValue(cls);
        }

        return value;
    }

    /**
     * Read element value.
     *
     * @param <T> the generic type
     * @param cls the cls
     * @return T
     */
    public <T> T readElementValue(Class<T> cls) throws ExchangeXmlException {
        this.ensureCurrentNodeIsStartElement();

        T value = null;

        if (!this.isEmptyElement()) {
            value = this.readValue(cls);
        }

        return value;
    }

    /**
     * Reads the value. Should return content element or text node as string
     * Present event must be START ELEMENT. After executing this function
     * Present event will be set on END ELEMENT
     *
     * @return String
     */
    public String readValue() throws ExchangeXmlException {
        return readValue(false);
    }

    /**
     * Reads the value. Should return content element or text node as string
     * Present event must be START ELEMENT. After executing this function
     * Present event will be set on END ELEMENT
     *
     * @param keepWhiteSpace Do not remove whitespace characters if true
     * @return String
     */
    public String readValue(boolean keepWhiteSpace) throws ExchangeXmlException {
        if (this.presentEvent.isStartElement()) {
            // Go to next event and check for Characters event
            this.read(keepWhiteSpace);
            if (this.presentEvent.isCharacters()) {
                final StringBuilder elementValue = new StringBuilder();
                do {
                    if (this.getNodeType().nodeType == XmlNodeType.CHARACTERS) {
                        Characters characters = (Characters) this.presentEvent;
                        if (keepWhiteSpace || (!characters.isIgnorableWhiteSpace()
                                && !characters.isWhiteSpace())) {
                            final String charactersData = characters.getData();
                            if (charactersData != null && !charactersData.isEmpty()) {
                                elementValue.append(charactersData);
                            }
                        }
                    }
                    this.read();
                } while (!this.presentEvent.isEndElement());
                // Characters chars = this.presentEvent.asCharacters();
                // String elementValue = chars.getData();
                // Advance to next event post Characters (ideally it will be End
                // Element)
                // this.read();
                return elementValue.toString();
            } else if (this.presentEvent.isEndElement()) {
                return "";
            } else {
                throw new ExchangeXmlException(getReadValueErrMsg("Could not find " + XmlNodeType.getString(XmlNodeType.CHARACTERS)));
            }
        } else if (this.presentEvent.getEventType() == XmlNodeType.CHARACTERS
                && this.presentEvent.isCharacters()) {
            /*
             * if(this.presentEvent.asCharacters().getData().equals("<")) {
             */
            final String charData = this.presentEvent.asCharacters().getData();
            final StringBuilder data = new StringBuilder(charData == null ? "" : charData);
            do {
                this.read(keepWhiteSpace);
                if (this.getNodeType().nodeType == XmlNodeType.CHARACTERS) {
                    Characters characters = (Characters) this.presentEvent;
                    if (keepWhiteSpace || (!characters.isIgnorableWhiteSpace()
                            && !characters.isWhiteSpace())) {
                        final String charactersData = characters.getData();
                        if (charactersData != null && !charactersData.isEmpty()) {
                            data.append(charactersData);
                        }
                    }
                }
            } while (!this.presentEvent.isEndElement());
            return data.toString();// this.presentEvent. = new XMLEvent();
            /*
             * } else { Characters chars = this.presentEvent.asCharacters();
             * String elementValue = chars.getData(); // Advance to next event
             * post Characters (ideally it will be End // Element) this.read();
             * return elementValue; }
             */
        } else {
            throw new ExchangeXmlException(getReadValueErrMsg("Expected is " + XmlNodeType.getString(XmlNodeType.START_ELEMENT))
            );
        }

    }

    /**
     * Tries to read value.
     *
     * @param value the value
     * @return boolean
     */
    public boolean tryReadValue(OutParam<String> value) throws ExchangeXmlException {
        if (!this.isEmptyElement()) {
            this.read();

            if (this.presentEvent.isCharacters()) {
                value.setParam(this.readValue());
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Reads the value.
     *
     * @param <T> the generic type
     * @param cls the cls
     * @return T
     */
    public <T> T readValue(Class<T> cls) throws ExchangeXmlException {
        return EwsUtilities.parse(cls, this.readValue());
    }

    public byte[] writeBase64ElementValue() throws ExchangeXmlException {
        this.ensureCurrentNodeIsStartElement();
        try {
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            byte[] buffer = Base64.getMimeDecoder().decode(this.xmlReader.getElementText());
            byteArrayStream.write(buffer);
            return byteArrayStream.toByteArray();
        } catch (XMLStreamException | IOException e) {
            throw new ExchangeXmlException("error reading base64 element value", e);
        }

    }

    public void writeBase64ElementValue(OutputStream outputStream) throws ExchangeXmlException {
        this.ensureCurrentNodeIsStartElement();
        try {
            byte[] buffer = Base64.getMimeDecoder().decode(this.xmlReader.getElementText());
            outputStream.write(buffer);
            outputStream.flush();
        } catch (XMLStreamException | IOException e) {
            throw new ExchangeXmlException("error reading base64 element value", e);
        }
    }

    /**
     * Reads the start element.
     *
     * @param namespacePrefix the namespace prefix
     * @param localName       the local name
     */
    public void readStartElement(String namespacePrefix, String localName) throws ExchangeXmlException {
        this.internalReadElement(namespacePrefix, localName, new XmlNodeType(XmlNodeType.START_ELEMENT));
    }

    /**
     * Reads the start element.
     *
     * @param xmlNamespace the xml namespace
     * @param localName    the local name
     */
    public void readStartElement(XmlNamespace xmlNamespace, String localName) throws ExchangeXmlException {
        this.internalReadElement(xmlNamespace, localName, new XmlNodeType(
                XmlNodeType.START_ELEMENT));
    }

    /**
     * Reads the end element.
     *
     * @param namespacePrefix the namespace prefix
     * @param elementName     the element name
     */
    public void readEndElement(String namespacePrefix, String elementName) throws ExchangeXmlException {
        this.internalReadElement(namespacePrefix, elementName, new XmlNodeType(
                XmlNodeType.END_ELEMENT));
    }

    /**
     * Reads the end element.
     *
     * @param xmlNamespace the xml namespace
     * @param localName    the local name
     */
    public void readEndElement(XmlNamespace xmlNamespace, String localName) throws ExchangeXmlException {
        this.internalReadElement(xmlNamespace, localName, new XmlNodeType(XmlNodeType.END_ELEMENT));
    }

    /**
     * Reads the end element if necessary.
     *
     * @param xmlNamespace the xml namespace
     * @param localName    the local name
     */
    public void readEndElementIfNecessary(XmlNamespace xmlNamespace, String localName) throws ExchangeXmlException {

        if (!(this.isStartElement(xmlNamespace, localName) && this
                .isEmptyElement())) {
            if (!this.isEndElement(xmlNamespace, localName)) {
                this.readEndElement(xmlNamespace, localName);
            }
        }
    }

    /**
     * Determines whether current element is a start element.
     *
     * @return boolean
     */
    public boolean isStartElement() {
        return this.presentEvent.isStartElement();
    }

    /**
     * Determines whether current element is a start element.
     *
     * @param namespacePrefix the namespace prefix
     * @param localName       the local name
     * @return boolean
     */
    public boolean isStartElement(String namespacePrefix, String localName) {
        boolean isStart = false;
        if (this.presentEvent.isStartElement()) {
            StartElement startElement = this.presentEvent.asStartElement();
            QName qName = startElement.getName();
            isStart = qName.getLocalPart().equals(localName)
                    && qName.getPrefix().equals(namespacePrefix);
        }
        return isStart;
    }

    /**
     * Determines whether current element is a start element.
     *
     * @param xmlNamespace the xml namespace
     * @param localName    the local name
     * @return true for matching start element; false otherwise.
     */
    public boolean isStartElement(XmlNamespace xmlNamespace, String localName) {
        return this.isStartElement()
                && Objects.equals(getLocalName(), localName)
                && (
                Objects.equals(getNamespacePrefix(), EwsUtilities.getNamespacePrefix(xmlNamespace)) ||
                        Objects.equals(getNamespaceUri(), EwsUtilities.getNamespaceUri(xmlNamespace)));
    }

    /**
     * Determines whether current element is a end element.
     *
     * @param namespacePrefix the namespace prefix
     * @param localName       the local name
     * @return boolean
     */
    public boolean isEndElement(String namespacePrefix, String localName) {
        boolean isEndElement = false;
        if (this.presentEvent.isEndElement()) {
            EndElement endElement = this.presentEvent.asEndElement();
            QName qName = endElement.getName();
            isEndElement = qName.getLocalPart().equals(localName)
                    && qName.getPrefix().equals(namespacePrefix);

        }
        return isEndElement;
    }

    /**
     * Determines whether current element is a end element.
     *
     * @param xmlNamespace the xml namespace
     * @param localName    the local name
     * @return boolean
     */
    public boolean isEndElement(XmlNamespace xmlNamespace, String localName) {

        boolean isEndElement = false;
        /*
         * if(localName.equals("Body")) { return true; } else
         */
        if (this.presentEvent.isEndElement()) {
            EndElement endElement = this.presentEvent.asEndElement();
            QName qName = endElement.getName();
            isEndElement = qName.getLocalPart().equals(localName)
                    && (qName.getPrefix().equals(
                    EwsUtilities.getNamespacePrefix(xmlNamespace)) ||
                    qName.getNamespaceURI().equals(
                            EwsUtilities.getNamespaceUri(
                                    xmlNamespace)));

        }
        return isEndElement;
    }

    /**
     * Skips the element.
     *
     * @param namespacePrefix the namespace prefix
     * @param localName       the local name
     */
    public void skipElement(String namespacePrefix, String localName) throws ExchangeXmlException {
        if (!this.isEndElement(namespacePrefix, localName)) {
            if (!this.isStartElement(namespacePrefix, localName)) {
                this.readStartElement(namespacePrefix, localName);
            }

            if (!this.isEmptyElement()) {
                do {
                    this.read();
                } while (!this.isEndElement(namespacePrefix, localName));
            }
        }
    }

    /**
     * Skips the element.
     *
     * @param xmlNamespace the xml namespace
     * @param localName    the local name
     */
    public void skipElement(XmlNamespace xmlNamespace, String localName) throws ExchangeXmlException {
        if (!this.isEndElement(xmlNamespace, localName)) {
            if (!this.isStartElement(xmlNamespace, localName)) {
                this.readStartElement(xmlNamespace, localName);
            }

            if (!this.isEmptyElement()) {
                do {
                    this.read();
                } while (!this.isEndElement(xmlNamespace, localName));
            }
        }
    }

    /**
     * Skips the current element.
     */
    public void skipCurrentElement() throws ExchangeXmlException {
        this.skipElement(this.getNamespacePrefix(), this.getLocalName());
    }

    /**
     * Ensures the current node is start element.
     *
     * @param xmlNamespace the xml namespace
     * @param localName    the local name
     */
    public void ensureCurrentNodeIsStartElement(XmlNamespace xmlNamespace, String localName) throws ExchangeXmlException {
        if (!this.isStartElement(xmlNamespace, localName)) {
            throw new ExchangeXmlException(String.format("The element '%s' in namespace '%s' wasn't found at the current position.", localName, xmlNamespace));
        }
    }

    /**
     * Ensures the current node is start element.
     */
    public void ensureCurrentNodeIsStartElement() throws ExchangeXmlException {
        XmlNodeType presentNodeType = new XmlNodeType(this.presentEvent.getEventType());
        if (!this.presentEvent.isStartElement()) {
            throw new ExchangeXmlException(String.format("The start element was expected, but node '%s' of type %s was found.", this.presentEvent.toString(), presentNodeType));
        }
    }

    /**
     * Ensures the current node is start element.
     *
     * @param xmlNamespace the xml namespace
     * @param localName    the local name
     */
    public void ensureCurrentNodeIsEndElement(XmlNamespace xmlNamespace,
                                              String localName) throws ExchangeXmlException {
        if (!this.isEndElement(xmlNamespace, localName)) {
            if (!(this.isStartElement(xmlNamespace, localName) && this.isEmptyElement())) {
                throw new ExchangeXmlException(String.format("The element '%s' in namespace '%s' wasn't found at the current position.", xmlNamespace, localName));
            }
        }
    }

    /**
     * Outer XML as string.
     *
     * @return String
     * @throws ServiceXmlDeserializationException the service xml deserialization exception
     * @throws XMLStreamException                 the XML stream exception
     */
    public String readOuterXml() throws ServiceXmlDeserializationException, XMLStreamException {
        if (!this.isStartElement()) {
            throw new ServiceXmlDeserializationException("The current position is not the start of an element.");
        }
        XMLEvent startEvent = this.presentEvent;
        XMLEvent event;
        StringBuilder str = new StringBuilder();
        str.append(startEvent);
        do {
            event = this.xmlReader.nextEvent();
            str.append(event);
        } while (!checkEndElement(startEvent, event));

        return str.toString();
    }

    /**
     * Reads the Inner XML at the given location.
     *
     * @return String
     * @throws ServiceXmlDeserializationException the service xml deserialization exception
     * @throws XMLStreamException                 the XML stream exception
     */
    public String readInnerXml() throws ServiceXmlDeserializationException,
            XMLStreamException {
        if (!this.isStartElement()) {
            throw new ServiceXmlDeserializationException("The current position is not the start of an element.");
        }

        XMLEvent startEvent = this.presentEvent;
        StringBuilder str = new StringBuilder();
        do {
            XMLEvent event = this.xmlReader.nextEvent();
            if (checkEndElement(startEvent, event)) {
                break;
            }
            str.append(event);
        } while (true);

        return str.toString();
    }

    /**
     * Check end element.
     *
     * @param startEvent the start event
     * @param endEvent   the end event
     * @return true, if successful
     */
    public static boolean checkEndElement(XMLEvent startEvent, XMLEvent endEvent) {
        boolean isEndElement = false;
        if (endEvent.isEndElement()) {
            QName qEName = endEvent.asEndElement().getName();
            QName qSName = startEvent.asStartElement().getName();
            isEndElement = qEName.getLocalPart().equals(qSName.getLocalPart())
                    && (qEName.getPrefix().equals(qSName.getPrefix()) || qEName
                    .getNamespaceURI().equals(qSName.
                            getNamespaceURI()));

        }
        return isEndElement;
    }

    /**
     * Gets the XML reader for node.
     *
     * @return null
     * @throws XMLStreamException                 the XML stream exception
     * @throws ServiceXmlDeserializationException the service xml deserialization exception
     * @throws FileNotFoundException              the file not found exception
     */
    public XMLEventReader getXmlReaderForNode()
            throws FileNotFoundException, ServiceXmlDeserializationException, XMLStreamException {
        return readSubtree();
    }

    public XMLEventReader readSubtree() throws XMLStreamException, ServiceXmlDeserializationException {

        if (!this.isStartElement()) {
            throw new ServiceXmlDeserializationException("The current position is not the start of an element.");
        }

        XMLEventReader eventReader = null;
        InputStream in = null;
        XMLEvent startEvent = this.presentEvent;
        XMLEvent event = startEvent;
        StringBuilder str = new StringBuilder();
        str.append(startEvent);
        do {
            event = this.xmlReader.nextEvent();
            str.append(event);
        } while (!checkEndElement(startEvent, event));

        try {

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();

            in = new ByteArrayInputStream(str.toString().getBytes(StandardCharsets.UTF_8));
            eventReader = inputFactory.createXMLEventReader(in);

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error reading subtree", e);
        }
        return eventReader;
    }

    /**
     * Reads to the next descendant element with the specified local name and
     * namespace.
     *
     * @param xmlNamespace The namespace of the element you with to move to.
     * @param localName    The local name of the element you wish to move to.
     * @throws XMLStreamException the XML stream exception
     */
    public void readToDescendant(XmlNamespace xmlNamespace, String localName) throws XMLStreamException {
        readToDescendant(localName, EwsUtilities.getNamespaceUri(xmlNamespace));
    }

    public boolean readToDescendant(String localName, String namespaceURI) throws XMLStreamException {

        if (!this.isStartElement()) {
            return false;
        }
        XMLEvent startEvent = this.presentEvent;
        XMLEvent event = this.presentEvent;
        do {
            if (event.isStartElement()) {
                QName qEName = event.asStartElement().getName();
                if (qEName.getLocalPart().equals(localName) &&
                        qEName.getNamespaceURI().equals(namespaceURI)) {
                    return true;
                }
            }
            event = this.xmlReader.nextEvent();
        } while (!checkEndElement(startEvent, event));

        return false;
    }


    /**
     * Gets a value indicating whether this instance has attribute.
     *
     * @return boolean
     */
    public boolean hasAttributes() {

        if (this.presentEvent.isStartElement()) {
            StartElement startElement = this.presentEvent.asStartElement();
            return startElement.getAttributes().hasNext();
        } else {
            return false;
        }
    }

    /**
     * Gets a value indicating whether current element is empty.
     *
     * @return boolean
     * @throws XMLStreamException the XML stream exception
     */
    public boolean isEmptyElement() throws ExchangeXmlException {
        try {
            boolean isPresentStartElement = this.presentEvent.isStartElement();
            boolean isNextEndElement = this.xmlReader.peek().isEndElement();
            return isPresentStartElement && isNextEndElement;
        } catch (XMLStreamException e) {
            throw new ExchangeXmlException("error peeking next XML event", e);
        }
    }

    /**
     * Gets the local name of the current element.
     *
     * @return String
     */
    public String getLocalName() {

        String localName = null;

        if (this.presentEvent.isStartElement()) {
            localName = this.presentEvent.asStartElement().getName()
                    .getLocalPart();
        } else {

            localName = this.presentEvent.asEndElement().getName()
                    .getLocalPart();
        }
        return localName;
    }

    /**
     * Gets the namespace prefix.
     *
     * @return String
     */
    protected String getNamespacePrefix() {
        if (this.presentEvent.isStartElement()) {
            return this.presentEvent.asStartElement().getName().getPrefix();
        }
        if (this.presentEvent.isEndElement()) {
            return this.presentEvent.asEndElement().getName().getPrefix();
        }
        return null;
    }

    /**
     * Gets the namespace URI.
     *
     * @return String
     */
    public String getNamespaceUri() {

        String nameSpaceUri;
        if (this.presentEvent.isStartElement()) {
            nameSpaceUri = this.presentEvent.asStartElement().getName()
                    .getNamespaceURI();
        } else {

            nameSpaceUri = this.presentEvent.asEndElement().getName()
                    .getNamespaceURI();
        }
        return nameSpaceUri;
    }

    /**
     * Gets the type of the node.
     * @return XmlNodeType
     */
    public XmlNodeType getNodeType() {
        XMLEvent event = this.presentEvent;
        return new XmlNodeType(event.getEventType());
    }

    /**
     * Gets the name of the current element.
     *
     * @return Object
     */
    protected Object getName() {
        String name;
        if (this.presentEvent.isStartElement()) {
            name = this.presentEvent.asStartElement().getName().toString();
        } else {

            name = this.presentEvent.asEndElement().getName().toString();
        }
        return name;
    }

    /**
     * Checks is the string is null or empty.
     *
     * @param namespacePrefix the namespace prefix
     * @return true, if is null or empty
     */
    private static boolean isNullOrEmpty(String namespacePrefix) {
        return (namespacePrefix == null || namespacePrefix.isEmpty());

    }

    /**
     * Gets the error message which happened during {@link #readValue()}.
     *
     * @param details details message
     * @return error message with details
     */
    private String getReadValueErrMsg(final String details) {
        final int eventType = this.presentEvent.getEventType();
        return "Could not read value from " + XmlNodeType.getString(eventType) + "." + details;
    }

}
