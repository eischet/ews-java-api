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
import com.eischet.ews.api.core.ExchangeService;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.ExchangeVersion;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.enumeration.service.error.ServiceErrorHandling;
import com.eischet.ews.api.core.exception.service.local.ServiceLocalException;
import com.eischet.ews.api.core.exception.service.local.ServiceVersionException;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.response.ServiceResponse;
import com.eischet.ews.api.misc.FolderIdWrapperList;
import com.eischet.ews.api.search.Grouping;
import com.eischet.ews.api.search.ViewBase;
import com.eischet.ews.api.search.filter.SearchFilter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents an abstract Find request.
 *
 * @param <TResponse> The type of the response.
 */
abstract class FindRequest<TResponse extends ServiceResponse> extends MultiResponseServiceRequest<TResponse> {

    private static final Logger LOG = Logger.getLogger(FindRequest.class.getCanonicalName());

    /**
     * The parent folder ids.
     */
    private final FolderIdWrapperList parentFolderIds = new FolderIdWrapperList();

    /**
     * The search filter.
     */
    private SearchFilter searchFilter;

    /**
     * The query string.
     */
    private String queryString;

    /**
     * The view.
     */
    private ViewBase view;

    /**
     * Initializes a new instance of the FindRequest class.
     *
     * @param service           The service.
     * @param errorHandlingMode Indicates how errors should be handled.
     * @throws Exception
     */
    protected FindRequest(ExchangeService service, ServiceErrorHandling errorHandlingMode) throws ServiceVersionException {
        super(service, errorHandlingMode);
    }

    /**
     * Validate request.
     *
     * @throws ServiceLocalException the service local exception
     * @throws Exception             the exception
     */
    @Override
    protected void validate() throws ServiceLocalException, Exception {
        super.validate();

        this.getView().internalValidate(this);

        // query string parameter is only valid for Exchange2010 or higher
        //
        if (!(this.queryString == null || this.queryString.isEmpty())
                && this.getService().getRequestedServerVersion().ordinal() <
                ExchangeVersion.Exchange2010.ordinal()) {
            throw new ServiceVersionException(String.format(
                    "The parameter %s is only valid for Exchange Server version %s or a later version.",
                    "queryString", ExchangeVersion.Exchange2010));
        }

        if ((!(this.queryString == null || this.queryString.isEmpty()))
                && this.searchFilter != null) {
            throw new ServiceLocalException(
                    "Both search filter and query string can't be specified. One of them must be null.");
        }
    }

    /**
     * Gets the expected response message count.
     *
     * @return XML element name.
     */
    @Override
    protected int getExpectedResponseMessageCount() {
        return this.getParentFolderIds().getCount();
    }

    /**
     * Gets the group by clause.
     *
     * @return The group by clause, null if the request does not have or support
     * grouping.
     */
    protected Grouping getGroupBy() {
        return null;
    }

    /**
     * Writes XML attribute.
     *
     * @param writer The Writer
     */
    @Override
    protected void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        super.writeAttributesToXml(writer);
        this.getView().writeAttributesToXml(writer);
    }

    /**
     * Writes XML elements.
     *
     * @param writer The Writer
     * @throws Exception the exception
     */
    @Override
    protected void writeElementsToXml(EwsServiceXmlWriter writer)
            throws Exception {
        this.getView().writeToXml(writer, this.getGroupBy());

        if (this.getSearchFilter() != null) {
            writer.writeStartElement(XmlNamespace.Messages,
                    XmlElementNames.Restriction);
            this.getSearchFilter().writeToXml(writer);
            writer.writeEndElement(); // Restriction
        }

        this.getView().writeOrderByToXml(writer);

        try {
            this.getParentFolderIds().writeToXml(writer, XmlNamespace.Messages,
                    XmlElementNames.ParentFolderIds);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error writing XML", e);
        }

        if (!(this.queryString == null || this.queryString.isEmpty())) {
            writer.writeElementValue(XmlNamespace.Messages,
                    XmlElementNames.QueryString, this.queryString);
        }
    }

    /**
     * Gets the parent folder ids.
     *
     * @return the parent folder ids
     */
    public FolderIdWrapperList getParentFolderIds() {
        return this.parentFolderIds;
    }

    /**
     * Gets the search filter. Available search filter classes include
     * SearchFilter.IsEqualTo, SearchFilter.ContainsSubstring and
     * SearchFilter.SearchFilterCollection. If SearchFilter is null, no search
     * filter are applied.
     *
     * @return the search filter
     */
    public SearchFilter getSearchFilter() {
        return searchFilter;
    }

    /**
     * Sets the search filter. Available search filter classes include
     * SearchFilter.IsEqualTo, SearchFilter.ContainsSubstring and
     * SearchFilter.SearchFilterCollection. If SearchFilter is null, no search
     * filter are applied.
     *
     * @param searchFilter the new search filter
     */
    public void setSearchFilter(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
    }

    /**
     * Gets the query string for indexed search.
     *
     * @return the query string
     */
    public String getQueryString() {
        return queryString;
    }

    /**
     * Sets the query string for indexed search.
     *
     * @param queryString the new query string
     */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    /**
     * Gets the view controlling the number of item or folder returned.
     *
     * @return the view
     */
    public ViewBase getView() {
        return view;
    }

    /**
     * Sets the view controlling the number of item or folder returned.
     *
     * @param view the new view
     */
    public void setView(ViewBase view) {
        this.view = view;
    }
}
