package com.xxx.library.network.https;


import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class MyTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (chain == null) {
            throw new IllegalArgumentException("checkServerTrusted:x509Certificate array isnull");
        }

        if (!(chain.length > 0)) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
        }

        if (!(null != authType && authType.contains("RSA"))) {
            throw new CertificateException("checkServerTrusted: AuthType is not RSA");
        }

        if(!trustCACertificate(chain, authType)&&!trustMyCertificate(chain)){
            throw new CertificateException();
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }


    private boolean trustMyCertificate(X509Certificate[] chain) throws CertificateException {

        RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();
        String encoded = new BigInteger(1 /* positive */, pubkey.getEncoded()).toString(16);
        X509Certificate certificate = SSLSocketFactoryUtils.getX509CerFile();

        if (certificate != null) {
            PublicKey local_pubkey = certificate.getPublicKey();
            String local_pubkey_str = new BigInteger(1 /* positive */, local_pubkey.getEncoded()).toString(16);
            if (!local_pubkey_str.equalsIgnoreCase(encoded)) {
                throw new CertificateException("checkServerTrusted: Expected public key: " + local_pubkey_str + ", got public key:" + encoded);
            }
            return true;
        } else {
            throw new CertificateException("certificate is null");
        }
    }

    private boolean trustCACertificate(X509Certificate[] chain, String authType){
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore) null);
            for (TrustManager trustManager : tmf.getTrustManagers()) {
                ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
