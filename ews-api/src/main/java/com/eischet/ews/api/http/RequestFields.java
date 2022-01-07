package com.eischet.ews.api.http;

import com.eischet.ews.api.core.exception.http.EWSHttpException;
import com.eischet.ews.api.http.ExchangeHttpClient;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for implementing an ExchangeHttpClient.Request.
 *
 * This class contains all the fields that the Exchange API expects it to have; it's up to an implementation to actually
 * turn these into HTTP requests using whichever client is used (and to fill back the results).
 */
public abstract class RequestFields implements ExchangeHttpClient.Request {

    // request properties

    private URL url;
    private String requestMethod;
    private int timeout;
    private String contentType;
    private String contentEncoding;
    private String accept;
    private String userAgent;

    private boolean allowAuthentication;
    private boolean allowAutoRedirect;
    private boolean preAuthenticate;
    private boolean acceptGzipEncoding;
    private boolean useDefaultCredentials;

    private final Map<String, String> httpHeaders = new HashMap<>(1);

    // response properties

    private int responseCode;
    private String responseContentType;
    private String responseText;

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public void setUrl(final URL url) {
        this.url = url;
    }

    @Override
    public String getRequestMethod() {
        return requestMethod;
    }

    @Override
    public void setRequestMethod(final String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public boolean isAllowAutoRedirect() {
        return allowAutoRedirect;
    }

    @Override
    public void setAllowAutoRedirect(final boolean allowAutoRedirect) {
        this.allowAutoRedirect = allowAutoRedirect;
    }

    public boolean isPreAuthenticate() {
        return preAuthenticate;
    }

    @Override
    public void setPreAuthenticate(final boolean preAuthenticate) {
        this.preAuthenticate = preAuthenticate;
    }

    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public String getAccept() {
        return accept;
    }

    @Override
    public void setAccept(final String accept) {
        this.accept = accept;
    }

    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public void setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean isAcceptGzipEncoding() {
        return acceptGzipEncoding;
    }

    @Override
    public void setAcceptGzipEncoding(final boolean acceptGzipEncoding) {
        this.acceptGzipEncoding = acceptGzipEncoding;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public boolean isUseDefaultCredentials() {
        return useDefaultCredentials;
    }

    @Override
    public void setUseDefaultCredentials(final boolean useDefaultCredentials) {
        this.useDefaultCredentials = useDefaultCredentials;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(final int responseCode) {
        this.responseCode = responseCode;
    }

    public boolean isAllowAuthentication() {
        return allowAuthentication;
    }

    @Override
    public void setAllowAuthentication(final boolean allowAuthentication) {
        this.allowAuthentication = allowAuthentication;
    }

    @Override
    public void setHeaders(final Map<String, String> httpHeaders) {
        this.httpHeaders.clear();
        this.httpHeaders.putAll(httpHeaders);
    }

    @Override
    public String getResponseHeaderField(final String headerName) throws EWSHttpException {
        return httpHeaders.get(headerName);
    }

    @Override
    public Map<String, String> getResponseHeaders() throws EWSHttpException {
        return new HashMap<>(httpHeaders);
    }

    @Override
    public String getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(final String responseContentType) {
        this.responseContentType = responseContentType;
    }

    @Override
    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(final String responseText) {
        this.responseText = responseText;
    }

    @Override
    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(final String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    @Override
    public Map<String, String> getRequestProperty() throws EWSHttpException {
        // the old Apache HTTP 4 client works differently, adding headers to the Apache POST object in prepareConnection!
        // There, this method returns the actual headers of the POST object, which are different from the plain httpHeaders set
        // by the API. I don't think that this distinction really matters, so they're the same here.
        return getHttpHeaders();
    }

    public void setHeader(final String headerName, final String headerValue) {
        httpHeaders.put(headerName, headerValue);
    }
}
