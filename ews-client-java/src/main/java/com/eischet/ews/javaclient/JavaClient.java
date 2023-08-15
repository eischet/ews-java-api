/*
 * The MIT License
 * Copyright (c) 2022 Eischet Software e.K.
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

package com.eischet.ews.javaclient;

import com.eischet.ews.api.core.exception.http.EWSHttpException;
import com.eischet.ews.api.http.ExchangeHttpClient;
import com.eischet.ews.api.http.RequestFields;
import jcifs.http.NtlmSsp;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Exchange Client using Java's built-in HTTP client.
 *
 * TODO: this class is not yet complete.
 */
public class JavaClient implements ExchangeHttpClient {

    private static final Logger log = Logger.getLogger(JavaClient.class.getCanonicalName());

    private boolean insecure;
    private boolean debugLogging;
    private String proxyHost;
    private int proxyPort = 8080;

    private CopyOnWriteArrayList<String> cookies = null;

    public JavaClient allowCookies() {
        cookies = new CopyOnWriteArrayList<>();
        return this;
    }

    public JavaClient ignoreSecurityErrors() {
        setInsecure(true);
        return this;
    }

    public JavaClient enableDebugLogging() {
        setDebugLogging(true);
        return this;
    }

    public JavaClient proxy(final String proxyHost, final int proxyPort) {
        setProxyHost(proxyHost);
        setProxyPort(proxyPort);
        return this;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(final String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(final int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public boolean isDebugLogging() {
        return debugLogging;
    }

    public void setDebugLogging(final boolean debugLogging) {
        this.debugLogging = debugLogging;
    }

    public void setInsecure(final boolean insecure) {
        this.insecure = insecure;
    }

    @Override
    public Request createRequest() {
        return new JavaRequest();
    }

    @Override
    public Request createPoolingRequest() {
        return new JavaRequest();
    }

    @Override
    public void close() throws IOException {
    }

    // TODO: this could maybe help --> https://github.com/codelibs/jcifs/blob/master/src/main/java/jcifs/http/NtlmHttpURLConnection.java
    // yes, it's "broken by design", as they say, but it's our reality, too ;-)

    protected class JavaRequest extends RequestFields {

        private final ByteArrayOutputStream post = new ByteArrayOutputStream();
        private HttpResponse<String> response;
        private static final String authHeaderName = "Authorization";
        private String authHeaderContents = null;
        private String username;
        private String domain;
        private String password;

        private boolean useNtlm = true;

        public boolean isUseNtlm() {
            return useNtlm;
        }

        public void setUseNtlm(final boolean useNtlm) {
            this.useNtlm = useNtlm;
        }

        @Override
        public void prepareConnection() {
            // Populate headers. (Copied from ApacheHttpClient::prepareConnection)

            setHeader("Content-type", getContentType());
            setHeader("User-Agent", getUserAgent());
            setHeader("Accept", getAccept());
            // these are not supported in the Java client and cannot be used like in the Apache client:
            // setHeader("Keep-Alive", "300");
            // setHeader("Connection", "Keep-Alive");
            // See https://stackoverflow.com/questions/53617574/how-to-keep-connection-alive-in-java-11-http-client for workarounds when needed


            if (isAcceptGzipEncoding()) {
                // TODO: this will need more work, according to https://stackoverflow.com/questions/53502626/does-java-http-client-handle-compression
                // I should maybe evaluate Methanol: https://mizosoft.github.io/methanol/enhanced_httpclient/

                setHeader("Accept-Encoding", "gzip,deflate");
            }

            if (authHeaderContents != null && !useNtlm) {
                setHeader(authHeaderName, authHeaderContents);
            }

        }

        @Override
        public void setCredentials(final String domain, final String username, final String password) {
            this.username = username;
            this.domain = domain;
            this.password = password;
            // TODO: other modes of Authentication, actually use Cookies, etc.
            if (username == null || username.isEmpty()) {
                authHeaderContents = null;
            } else {
                authHeaderContents = "Basic " + Base64.getEncoder()
                        .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            }
        }


        @Override
        public void close() throws IOException {
        }

        @Override
        public OutputStream getOutputStream() throws EWSHttpException {
            return post;
        }

        @Override
        public int executeRequest() throws IOException, EWSHttpException {
            if (debugLogging) {
                log.info("----- POSTing to " + getUrl() + " -----");
            }
            try {
                final HttpRequest.Builder builder = HttpRequest
                        .newBuilder(getUrl().toURI())
                        .timeout(Duration.ofMillis(getTimeout()))
                        .POST(HttpRequest.BodyPublishers.ofByteArray(post.toByteArray()));
                for (Map.Entry<String, String> entry : getHttpHeaders().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (debugLogging) {
                        log.info("HTTP header: " + key + ": " + value);
                    }
                    builder.header(key, value);
                }
                if (cookies != null) {
                    for (final String cookie : cookies) {
                        if (debugLogging) {
                            log.info("Cookie: " + cookie);
                        }
                        builder.header("Cookie", cookie);
                    }
                }
                final HttpRequest request = builder.build();

                if (debugLogging) {
                    log.info("POST BODY: " + post);
                }
                response = buildClient().send(request, HttpResponse.BodyHandlers.ofString());
                setResponseCode(response.statusCode());
                setResponseContentType(response.headers().firstValue("Content-Type").orElse(null));
                setResponseText(response.body());
                setContentEncoding(response.headers().firstValue("Content-Encoding").orElse(null));
                if (cookies != null) {
                    cookies.addAll(response.headers().allValues("Set-Cookie"));
                }
                log.info("Response: " + response.statusCode() + " " + response.body());
                return response.statusCode();
            } catch (URISyntaxException e) {
                throw new EWSHttpException("invalid request URI: " + getUrl(), e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new EWSHttpException("interrupted during request", e);
            }
        }

        private HttpClient buildClient() {
            // TODO: this should be moved to JavaClient level and reused, but the timeouts are currently stored at Request level...
            try {
                if (insecure) {
                    // They'll eventually go around to fixing this in the JDK, I hope...
                    // https://stackoverflow.com/questions/52988677/allow-insecure-https-connection-for-java-jdk-11-httpclient
                    System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
                    return configure(HttpClient.newBuilder().sslContext(BlindSSLSocketFactory.getSSLContext())).build();
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "FAILED to create an 'insecure' HTTP client!", e);
            }
            return configure(HttpClient.newBuilder()).build();
        }


        @Override
        public InputStream getInputStream() throws EWSHttpException, IOException {
            return new ByteArrayInputStream(getResponseText().getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public InputStream getErrorStream() throws EWSHttpException {
            return new ByteArrayInputStream(getResponseText().getBytes(StandardCharsets.UTF_8));
        }

        private HttpClient.Builder configure(final HttpClient.Builder builder) {
            if (proxyHost != null && !proxyHost.isBlank()) {
                builder.proxy(ProxySelector.of(new InetSocketAddress(getProxyHost(), getProxyPort())));
            }


            builder.authenticator(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password.toCharArray());
                }
            });
            return builder
                    .followRedirects(isAllowAutoRedirect() ? HttpClient.Redirect.ALWAYS : HttpClient.Redirect.NEVER)
                    .connectTimeout(Duration.of(getTimeout(), ChronoUnit.MILLIS));
        }

    }

}
