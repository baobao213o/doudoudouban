package com.xxx.library.network.https;


import com.xxx.library.BaseApplication;
import com.xxx.library.R;
import com.xxx.library.utils.CommonLogger;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLSocketFactoryUtils {

    private static final int keyServerStroreID = R.raw.douban;

    public static MyTrustManager createTrustManager() {
        return new MyTrustManager();
    }

    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory mSSLSocketFactory;

        InputStream trustStream = BaseApplication.getInstance().getResources().openRawResource(keyServerStroreID);
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        //获得服务器端证书
        TrustManager[] turstManager = getTurstManager(trustStream);

        //初始化ssl证书库
        try {
            sslContext.init(null, turstManager, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        //获得sslSocketFactory
        mSSLSocketFactory = sslContext.getSocketFactory();
        return mSSLSocketFactory;
    }


    /**
     * 获得指定流中的服务器端证书库
     */

    private static TrustManager[] getTurstManager(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            //自签名证书
            int index = 0;
            for (InputStream certificate : certificates) {
                if (certificate == null) {
                    continue;
                }
                Certificate certificate1;
                try {
                    certificate1 = certificateFactory.generateCertificate(certificate);
                } finally {
                    certificate.close();
                }

                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificate1);
            }
            //ca证书
            KeyStore mCAStore = KeyStore.getInstance("AndroidCAStore");
            mCAStore.load(null, null);
            Enumeration<String> als = mCAStore.aliases();
            while (als.hasMoreElements()) {
                String alias = als.nextElement();
                //系统默认CA开头为system  用户的为user
                if (alias.startsWith("system")){
                    X509Certificate CAcert = (X509Certificate) mCAStore.getCertificate(alias);
                    keyStore.setCertificateEntry(alias, CAcert);
                }
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);

            return trustManagerFactory.getTrustManagers();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new X509TrustManager[]{createTrustManager()};
    }


    /***
     * 读取*.cer公钥证书文件
     */
    static X509Certificate getX509CerFile() {
        try {
            // 读取证书文件
            InputStream inStream = BaseApplication.getInstance().getResources().openRawResource(keyServerStroreID);
            // 创建X509工厂类
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // 创建证书对象
            X509Certificate oCert = (X509Certificate) cf.generateCertificate(inStream);
            inStream.close();
            return oCert;
        } catch (Exception e) {
            CommonLogger.e("解析证书出错！");
            e.printStackTrace();
            return null;
        }
    }


    public static X509TrustManager systemDefaultTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }
    }

    public static SSLSocketFactory systemDefaultSslSocketFactory(X509TrustManager trustManager) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            return sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }
    }
}
