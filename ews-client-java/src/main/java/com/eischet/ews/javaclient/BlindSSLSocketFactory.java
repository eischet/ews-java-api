package com.eischet.ews.javaclient;


import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Put together in trial and error (and with a little help from Google), this class
 * turns off SSL certificate verification. It is intended to be used with LDAPS connections
 * that are port-forwarded... there, the certificate presented by the LDAPS server will
 * never match the host name / IP address of the forwarding server, and that (rightfully) lets
 * LDAPS connections fail.
 * <br>
 * <b>Please be aware that using this class severely degrades security!<b>
 * <br>
 * An attacker could set up an
 * LDAPS server and redirect our connections to this server, and we'd never notice. This is
 * exactly what certificate checks are supposed to stop, and by turning them off we do become
 * vulnerable to attacks. However, the current infrastructure forces us to do this.
 *
 *
 * Upgraded via https://stackoverflow.com/questions/52600211/how-to-programmatically-disable-certificate-hostname-verification-in-java-ldap-j
 * for Java 8 - 181+
 */
public class BlindSSLSocketFactory extends SSLSocketFactory {

    private static final Logger log = Logger.getLogger(BlindSSLSocketFactory.class.getCanonicalName());

    private static final SSLSocketFactory defaultFactory = new BlindSSLSocketFactory();

    public static SSLSocketFactory getDefault() {
        return defaultFactory;
    }

    private SSLSocketFactory proxiedFactory = null;

    public static SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        final X509TrustManager [] trumanShow = { getBlindTrustManager() };
        sslContext.init(null, trumanShow, new SecureRandom());
        return sslContext;
    }

    public static X509ExtendedTrustManager getBlindTrustManager() {
        return new X509ExtendedTrustManager() {
            @Override
            public void checkClientTrusted(final X509Certificate[] chain, final String authType, final Socket socket) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain, final String authType, final Socket socket) throws CertificateException {

            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain, final String authType, final SSLEngine engine) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain, final String authType, final SSLEngine engine) throws CertificateException {

            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkServerTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
                // never fails, which is the whole point
            }

            public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
                // never fails, which is the whole point
            }
        };
    }

    public BlindSSLSocketFactory() {
        log.info("BlindSSLSocketFactory()");
        try {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            final X509TrustManager [] trumanShow = { getBlindTrustManager() };
            sslContext.init(null, trumanShow, new SecureRandom());
            proxiedFactory = sslContext.getSocketFactory();
        }
        catch (final NoSuchAlgorithmException e) {
            log.log(Level.SEVERE, "JVM does not speak SSL, we're screwed", e);
        }
        catch (final KeyManagementException e) {
            log.log(Level.SEVERE, "I don't know why, but we're screwed nevertheless", e);
        }
    }


    @Override
    public Socket createSocket(final Socket arg0, final String arg1, final int arg2, final boolean arg3) throws IOException {
        log.finer(() -> String.format("createSocket(%s,%s,%s,%s)", arg0, arg1, arg2, arg3));
        return proxiedFactory.createSocket(arg0, arg1, arg2, arg3);
    }

    @Override
    public String[] getDefaultCipherSuites() {
        log.finer( "getDefaultCipherSuites()");
        return proxiedFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        log.finer( "getSupportedCipherSuites()");
        return proxiedFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(final String arg0, final int arg1) throws IOException {
        //log.fine( "createSocket(%s,%s)", arg0, arg1);
        return proxiedFactory.createSocket(arg0, arg1);
    }

    @Override
    public Socket createSocket(final InetAddress arg0, final int arg1) throws IOException {
        //log.fine( "createSocket(%s,%s)", arg0, arg1);
        return proxiedFactory.createSocket(arg0, arg1);
    }

    @Override
    public Socket createSocket(final String arg0, final int arg1, final InetAddress arg2, final int arg3) throws IOException {
        //log.fine( "createSocket(%s,%s,%s)", arg0, arg1, arg3);
        return proxiedFactory.createSocket(arg0, arg1, arg2, arg3);
    }

    @Override
    public Socket createSocket(final InetAddress arg0, final int arg1, final InetAddress arg2, final int arg3) throws IOException {
        //log.fine( "createSocket(%s,%s,%s)", arg0, arg1, arg3);
        return proxiedFactory.createSocket(arg0, arg1, arg2, arg3);
    }

    public Socket createSocket() throws IOException {
        log.info("funny: someone is calling the unspecified createSocket method");
        return proxiedFactory.createSocket();
    }

}
