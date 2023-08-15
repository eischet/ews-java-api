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

// TODO: apply https://hc.apache.org/httpcomponents-client-5.2.x/migration-guide/preparation.html

package com.eischet.ews.apache5;

import com.eischet.ews.api.EWSConstants;
import com.eischet.ews.api.core.WebProxy;
import com.eischet.ews.api.core.exception.http.EWSHttpException;
import com.eischet.ews.api.core.request.HttpWebRequest;
import com.eischet.ews.api.http.ExchangeHttpClient;
import com.eischet.ews.api.util.IOUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.NTCredentials;
import org.apache.hc.client5.http.auth.StandardAuthScheme;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ApacheHttpClient implements ExchangeHttpClient {

    private CloseableHttpClient httpClient;
    protected HttpClientContext httpContext;

    protected CloseableHttpClient httpPoolingClient;

    private int maximumPoolingConnections = 10;

    public int getMaximumPoolingConnections() {
        return maximumPoolingConnections;
    }

    private WebProxy webProxy;



    public ApacheHttpClient() {
        initializeHttpClient();
        initializeHttpContext();
    }

    private void initializeHttpClient() {
        Registry<ConnectionSocketFactory> registry = createConnectionSocketFactoryRegistry();
        HttpClientConnectionManager httpConnectionManager = new BasicHttpClientConnectionManager(registry);



        // what do we do with this?
        // AuthenticationStrategy authStrategy = new CookieProcessingTargetAuthenticationStrategy();

        httpClient = HttpClients.custom()

                .setConnectionManager(httpConnectionManager)


                //TODO: seems to be missing .setTargetAuthenticationStrategy(authStrategy)
                .setDefaultCookieStore(new BasicCookieStore())
                .build();
    }

    private void initializeHttpPoolingClient() {
        Registry<ConnectionSocketFactory> registry = createConnectionSocketFactoryRegistry();
        PoolingHttpClientConnectionManager httpConnectionManager = new PoolingHttpClientConnectionManager(registry);
        httpConnectionManager.setMaxTotal(maximumPoolingConnections);
        httpConnectionManager.setDefaultMaxPerRoute(maximumPoolingConnections);
        // AuthenticationStrategy authStrategy = new CookieProcessingTargetAuthenticationStrategy();
        CookieStore cookieStore = new BasicCookieStore();

        httpPoolingClient = HttpClients.custom()
                .setConnectionManager(httpConnectionManager)
                // .setTargetAuthenticationStrategy(authStrategy)
                .setDefaultCookieStore(cookieStore)

                /* maybe adjust timeouts here, too:
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD)
                        .build())

                 */
                .build();
    }

    /**
     * Sets the maximum number of connections for the pooling connection manager which is used for
     * subscriptions.
     * <p>
     * Default is 10.
     * </p>
     *
     * @param maximumPoolingConnections Maximum number of pooling connections
     */
    public void setMaximumPoolingConnections(int maximumPoolingConnections) {
        if (maximumPoolingConnections < 1)
            throw new IllegalArgumentException("maximumPoolingConnections must be 1 or greater");
        this.maximumPoolingConnections = maximumPoolingConnections;
    }

    /**
     * Create registry with configured {@link ConnectionSocketFactory} instances.
     * Override this method to change how to work with different schemas.
     *
     * @return registry object
     */
    protected Registry<ConnectionSocketFactory> createConnectionSocketFactoryRegistry() {
        try {
            return RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(EWSConstants.HTTP_SCHEME, new PlainConnectionSocketFactory())
                    // TODO: make this configurable
                    .register(EWSConstants.HTTPS_SCHEME, EwsSSLProtocolSocketFactory.build(null, new NoopHostnameVerifier()))
                    .build();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(
                    "Could not initialize ConnectionSocketFactory instances for HttpClientConnectionManager", e
            );
        }
    }

    /**
     * (Re)initializes the HttpContext object. This removes any existing state (mainly cookies). Use an own
     * cookie store, instead of the httpClient's global store, so cookies get reset on reinitialization
     */
    private void initializeHttpContext() {
        CookieStore cookieStore = new BasicCookieStore();
        httpContext = HttpClientContext.create();
        httpContext.setCookieStore(cookieStore);
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(httpClient);
        IOUtils.closeQuietly(httpPoolingClient);
    }

    /**
     * Gets the web proxy that should be used when sending request to EWS.
     *
     * @return Proxy
     * the Proxy Information
     */
    public WebProxy getWebProxy() {
        return this.webProxy;
    }

    /**
     * Sets the web proxy that should be used when sending request to EWS.
     * Set this property to null to use the default web proxy.
     *
     * @param value the Proxy Information
     */
    public void setWebProxy(WebProxy value) {
        this.webProxy = value;
    }

    @Override
    public Request createRequest() {
        HttpClientWebRequest request = new HttpClientWebRequest(httpClient, httpContext);
        request.setProxy(getWebProxy());
        return request;
    }

    @Override
    public Request createPoolingRequest() {
        if (httpPoolingClient == null) {
            initializeHttpPoolingClient();
        }

        HttpClientWebRequest request = new HttpClientWebRequest(httpPoolingClient, httpContext);
        request.setProxy(getWebProxy());
        return request;
    }

    /**
     * HttpClientWebRequest is used for making request to the server through NTLM Authentication by using Apache
     * HttpClient 3.1 and JCIFS Library.
     */
    public static class HttpClientWebRequest extends HttpWebRequest implements Request {

        /**
         * The Http Method.
         */
        private HttpPost httpPost = null;
        private CloseableHttpResponse response = null;

        private final CloseableHttpClient httpClient;
        private final HttpClientContext httpContext;
        private WrappingOuputStream currentOutputStream;


        /**
         * Instantiates a new http native web request.
         */
        public HttpClientWebRequest(CloseableHttpClient httpClient, HttpClientContext httpContext) {
            this.httpClient = httpClient;
            this.httpContext = httpContext;
        }

        /**
         * Releases the connection by Closing.
         */
        @Override
        public void close() throws IOException {
            // First check if we can close the response, by consuming the complete response
            // This releases the connection but keeps it alive for future request
            // If that is not possible, we simply cleanup the whole connection
            if (response != null && response.getEntity() != null) {
                EntityUtils.consume(response.getEntity());
            }
            // There's no releaseConnection method anymore. Doing nothing here.
            // else if (httpPost != null) {
                // should this be response.close(); now?
                // httpPost.releaseConnection();
            // }

            // We set httpPost to null to prevent the connection from being closed again by an accidental
            // second call to close()
            // The response is kept, in case something in the library still wants to read something from it,
            // like response code or headers
            httpPost = null;
        }

        /**
         * Prepares the request by setting appropriate headers, authentication, timeouts, etc.
         */
        @Override
        public void prepareConnection() {
            httpPost = new HttpPost(getUrl().toString());

            // Populate headers.
            httpPost.addHeader("Content-type", getContentType());
            httpPost.addHeader("User-Agent", getUserAgent());
            httpPost.addHeader("Accept", getAccept());
            httpPost.addHeader("Keep-Alive", "300");
            httpPost.addHeader("Connection", "Keep-Alive");

            if (isAcceptGzipEncoding()) {
                httpPost.addHeader("Accept-Encoding", "gzip,deflate");
            }

            if (getHeaders() != null) {
                for (Map.Entry<String, String> httpHeader : getHeaders().entrySet()) {
                    httpPost.addHeader(httpHeader.getKey(), httpHeader.getValue());
                }
            }

            // Build request configuration.
            // Disable Kerberos in the preferred auth schemes - EWS should usually allow NTLM or Basic auth
            RequestConfig.Builder
                    requestConfigBuilder =
                    RequestConfig.custom()
                            .setAuthenticationEnabled(true)
                                // MS is an assumption - they didn't bother to document it in the Apache HTTP client 4
                            .setConnectTimeout(Timeout.ofMilliseconds(getTimeout()))
                            .setConnectionRequestTimeout(getTimeout(), TimeUnit.MILLISECONDS)
                            .setRedirectsEnabled(isAllowAutoRedirect())
                            .setResponseTimeout(getTimeout(), TimeUnit.MILLISECONDS)
                            .setTargetPreferredAuthSchemes(Arrays.asList(StandardAuthScheme.NTLM, StandardAuthScheme.BASIC))
                            .setProxyPreferredAuthSchemes(Arrays.asList(StandardAuthScheme.NTLM, StandardAuthScheme.BASIC));


            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();

            // Add proxy credential if necessary.
            WebProxy proxy = getProxy();
            if (proxy != null) {
                HttpHost proxyHost = new HttpHost(proxy.getHost(), proxy.getPort());
                requestConfigBuilder.setProxy(proxyHost);

                if (proxy.hasCredentials()) {
                    NTCredentials
                            proxyCredentials =
                            new NTCredentials(proxy.getCredentials().getUsername(), proxy.getCredentials().getPassword().toCharArray(), "",
                                    proxy.getCredentials().getDomain());



                    credentialsProvider.setCredentials(new AuthScope(proxyHost), proxyCredentials);
                }
            }

            // Add web service credential if necessary.
            if (isAllowAuthentication() && getUsername() != null) {
                NTCredentials webServiceCredentials = new NTCredentials(getUsername(), getPassword().toCharArray(), "", getDomain());
                final AuthScope any = new AuthScope(null, null, -1, null, null);
                credentialsProvider.setCredentials(any, webServiceCredentials);

                //try {
                    // final HttpHost host = new HttpHost(httpPost.getUri().getHost());
                    // old: credentialsProvider.setCredentials(new AuthScope(AuthScope.ANY), webServiceCredentials);

                //} catch (URISyntaxException e) {
                //    throw new RuntimeException("error configuring credentials for this POST request", e);
                //}
            }

            httpContext.setCredentialsProvider(credentialsProvider);

            httpPost.setConfig(requestConfigBuilder.build());
        }

        /**
         * Gets the input stream.
         *
         * @return the input stream
         * @throws EWSHttpException the EWS http exception
         */
        @Override
        public InputStream getInputStream() throws EWSHttpException, IOException {
            throwIfResponseIsNull();
            BufferedInputStream bufferedInputStream = null;
            try {
                bufferedInputStream = new BufferedInputStream(response.getEntity().getContent());
            } catch (IOException e) {
                throw new EWSHttpException("Connection Error " + e);
            }
            return bufferedInputStream;
        }

        /**
         * Gets the error stream.
         *
         * @return the error stream
         * @throws EWSHttpException the EWS http exception
         */
        @Override
        public InputStream getErrorStream() throws EWSHttpException {
            throwIfResponseIsNull();
            BufferedInputStream bufferedInputStream = null;
            try {
                bufferedInputStream = new BufferedInputStream(response.getEntity().getContent());
            } catch (Exception e) {
                throw new EWSHttpException("Connection Error " + e);
            }
            return bufferedInputStream;
        }

        /**
         * Gets the output stream.
         *
         * @return the output stream
         * @throws EWSHttpException the EWS http exception
         */
        @Override
        public OutputStream getOutputStream() throws EWSHttpException {
            throwIfRequestIsNull();
            currentOutputStream = new WrappingOuputStream(httpPost);
            return currentOutputStream;
        }

        /**
         * Gets the response headers.
         *
         * @return the response headers
         * @throws EWSHttpException the EWS http exception
         */
        @Override
        public Map<String, String> getResponseHeaders() throws EWSHttpException {
            throwIfResponseIsNull();
            Map<String, String> map = new HashMap<String, String>();

            Header[] hM = response.getHeaders();
            for (Header header : hM) {
                // RFC2109: Servers may return multiple Set-Cookie headers
                // Need to append the cookies before they are added to the map
                if (header.getName().equals("Set-Cookie")) {
                    String cookieValue = "";
                    if (map.containsKey("Set-Cookie")) {
                        cookieValue += map.get("Set-Cookie");
                        cookieValue += ",";
                    }
                    cookieValue += header.getValue();
                    map.put("Set-Cookie", cookieValue);
                } else {
                    map.put(header.getName(), header.getValue());
                }
            }

            return map;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * microsoft.exchange.webservices.HttpWebRequest#getResponseHeaderField(
         * java.lang.String)
         */
        @Override
        public String getResponseHeaderField(String headerName) throws EWSHttpException {
            throwIfResponseIsNull();
            Header hM = response.getFirstHeader(headerName);
            return hM != null ? hM.getValue() : null;
        }

        /**
         * Gets the content encoding.
         *
         * @return the content encoding
         * @throws EWSHttpException the EWS http exception
         */
        @Override
        public String getContentEncoding() throws EWSHttpException {
            throwIfResponseIsNull();
            return response.getFirstHeader("content-encoding") != null ? response.getFirstHeader("content-encoding")
                    .getValue() : null;
        }

        /**
         * Gets the response content type.
         *
         * @return the response content type
         * @throws EWSHttpException the EWS http exception
         */
        @Override
        public String getResponseContentType() throws EWSHttpException {
            throwIfResponseIsNull();
            return response.getFirstHeader("Content-type") != null ? response.getFirstHeader("Content-type")
                    .getValue() : null;
        }

        /**
         * Executes Request by sending request xml data to server.
         *
         * @throws EWSHttpException    the EWS http exception
         * @throws IOException the IO Exception
         * @return
         */
        @Override
        public int executeRequest() throws EWSHttpException, IOException {
            throwIfRequestIsNull();

            if (currentOutputStream == null) {
                throw new EWSHttpException("the output stream is null, there's no data to send!?");
            }
            currentOutputStream.close(); // closing it triggers setting the entity

            response = httpClient.execute(httpPost, httpContext);
            return response.getCode(); // ?? don't know what is wanted in return
        }

        /**
         * Gets the response code.
         *
         * @return the response code
         * @throws EWSHttpException the EWS http exception
         */
        @Override
        public int getResponseCode() throws EWSHttpException {
            throwIfResponseIsNull();
            return response.getCode();
        }

        /**
         * Gets the response message.
         *
         * @return the response message
         * @throws EWSHttpException the EWS http exception
         */
        public String getResponseText() throws EWSHttpException {
            throwIfResponseIsNull();
            return response.getReasonPhrase();
        }

        /**
         * Throw if conn is null.
         *
         * @throws EWSHttpException the EWS http exception
         */
        private void throwIfRequestIsNull() throws EWSHttpException {
            if (null == httpPost) {
                throw new EWSHttpException("Connection not established");
            }
        }

        private void throwIfResponseIsNull() throws EWSHttpException {
            if (null == response) {
                throw new EWSHttpException("Connection not established");
            }
        }

        /**
         * Gets the request property.
         *
         * @return the request property
         * @throws EWSHttpException the EWS http exception
         */
        public Map<String, String> getRequestProperty() throws EWSHttpException {
            throwIfRequestIsNull();
            Map<String, String> map = new HashMap<String, String>();

            Header[] hM = httpPost.getHeaders();
            for (Header header : hM) {
                map.put(header.getName(), header.getValue());
            }
            return map;
        }
    }
}
