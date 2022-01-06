package microsoft.exchange.webservices.data.http;

import microsoft.exchange.webservices.data.EWSConstants;
import microsoft.exchange.webservices.data.core.WebProxy;
import microsoft.exchange.webservices.data.core.exception.http.EWSHttpException;
import microsoft.exchange.webservices.data.util.IOUtils;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Map;

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
        AuthenticationStrategy authStrategy = new CookieProcessingTargetAuthenticationStrategy();

        httpClient = HttpClients.custom()
                .setConnectionManager(httpConnectionManager)
                .setTargetAuthenticationStrategy(authStrategy)
                .build();
    }

    private void initializeHttpPoolingClient() {
        Registry<ConnectionSocketFactory> registry = createConnectionSocketFactoryRegistry();
        PoolingHttpClientConnectionManager httpConnectionManager = new PoolingHttpClientConnectionManager(registry);
        httpConnectionManager.setMaxTotal(maximumPoolingConnections);
        httpConnectionManager.setDefaultMaxPerRoute(maximumPoolingConnections);
        AuthenticationStrategy authStrategy = new CookieProcessingTargetAuthenticationStrategy();

        httpPoolingClient = HttpClients.custom()
                .setConnectionManager(httpConnectionManager)
                .setTargetAuthenticationStrategy(authStrategy)
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
                    .register(EWSConstants.HTTPS_SCHEME, EwsSSLProtocolSocketFactory.build(null))
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
        return new ApacheRequest(request);
    }

    @Override
    public Request createPoolingRequest() {
        if (httpPoolingClient == null) {
            initializeHttpPoolingClient();
        }

        HttpClientWebRequest request = new HttpClientWebRequest(httpPoolingClient, httpContext);
        request.setProxy(getWebProxy());
        return new ApacheRequest(request);
    }

    private static class ApacheRequest implements Request {

        private final HttpClientWebRequest request;

        public ApacheRequest(final HttpClientWebRequest request) {
            this.request = request;
        }

        @Override
        public void setUrl(final URL toURL) {
            request.setUrl(toURL);
        }

        @Override
        public void setRequestMethod(final String method) {
            request.setRequestMethod(method);
        }

        @Override
        public void setAllowAutoRedirect(final boolean b) {
            request.setAllowAutoRedirect(b);
        }

        @Override
        public void setPreAuthenticate(final boolean preAuthenticate) {
            request.setPreAuthenticate(preAuthenticate);
        }

        @Override
        public void setTimeout(final int timeout) {
            request.setTimeout(timeout);
        }

        @Override
        public void setContentType(final String s) {
            request.setContentType(s);
        }

        @Override
        public void setAccept(final String s) {
            request.setAccept(s);
        }

        @Override
        public void setUserAgent(final String userAgent) {
            request.setUserAgent(userAgent);
        }

        @Override
        public void setAcceptGzipEncoding(final boolean acceptGzipEncoding) {
            request.setAcceptGzipEncoding(acceptGzipEncoding);
        }

        @Override
        public void setHeaders(final Map<String, String> httpHeaders) {
            request.setHeaders(httpHeaders);
        }

        @Override
        public void setUseDefaultCredentials(final boolean useDefaultCredentials) {
            request.setUseDefaultCredentials(useDefaultCredentials);
        }

        @Override
        public void prepareConnection() {
            request.prepareConnection();
        }

        @Override
        public void close() {
            try {
                request.close();
            } catch (IOException ignored) {
            }
        }

        @Override
        public OutputStream getOutputStream() throws EWSHttpException {
            return request.getOutputStream();
        }

        // IntelliJ thinks that EWSHttpException will be thrown because it looks at the CALLERS.
        // I don't think this is right, but will have to look deeper later.

        @Override
        public void executeRequest() throws IOException, EWSHttpException {
            request.executeRequest();
        }

        @Override
        public int getResponseCode() throws EWSHttpException {
            return request.getResponseCode();
        }

        @Override
        public InputStream getInputStream() throws EWSHttpException, IOException {
            return request.getInputStream();
        }

        @Override
        public void setAllowAuthentication(final boolean b) {
            request.setAllowAuthentication(b);
        }

        @Override
        public String getResponseHeaderField(final String headerName) throws EWSHttpException {
            return request.getResponseHeaderField(headerName);
        }

        @Override
        public Map<String, String> getResponseHeaders() throws EWSHttpException {
            return request.getResponseHeaders();
        }

        @Override
        public String getResponseContentType() throws EWSHttpException {
            return request.getResponseContentType();
        }

        @Override
        public void setCredentials(final String domain, final String user, final String pwd) {
            request.setCredentials(domain, user, pwd);
        }

        @Override
        public URL getUrl() {
            return request.getUrl();
        }

        @Override
        public String getContentEncoding() throws EWSHttpException {
            return request.getContentEncoding();
        }

        @Override
        public String getRequestMethod() {
            return request.getRequestMethod();
        }

        @Override
        public Map<String, String> getRequestProperty() throws EWSHttpException {
            return request.getRequestProperty();
        }

        @Override
        public InputStream getErrorStream() throws EWSHttpException {
            return request.getErrorStream();
        }

        @Override
        public String getResponseText() throws EWSHttpException {
            return request.getResponseText();
        }
    }
}
