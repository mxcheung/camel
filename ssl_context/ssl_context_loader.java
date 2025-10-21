import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.StringReader;
import java.security.*;
import java.security.cert.*;
import java.util.*;

public class SSLContextLoader {
    public static SSLContext fromEnv() throws Exception {
        String clientKeyPem = System.getenv("CLIENT_KEY");
        String clientCertPem = System.getenv("CLIENT_CERT");
        String caChainPem = System.getenv("CA_CHAIN");

        // Parse private key
        PEMParser pemParser = new PEMParser(new StringReader(clientKeyPem));
        Object keyObject = pemParser.readObject();
        PrivateKey privateKey = new JcaPEMKeyConverter().getPrivateKey((org.bouncycastle.asn1.pkcs.PrivateKeyInfo) keyObject);
        pemParser.close();

        // Parse client certificate
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate clientCert = (X509Certificate)
                cf.generateCertificate(new java.io.ByteArrayInputStream(clientCertPem.getBytes()));

        // Parse CA chain (can contain multiple certs)
        Collection<? extends Certificate> caCerts =
                cf.generateCertificates(new java.io.ByteArrayInputStream(caChainPem.getBytes()));

        // Build key store (for client identity)
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setKeyEntry("client", privateKey, "".toCharArray(),
                new Certificate[]{clientCert});

        // Build trust store (for server validation)
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        int i = 0;
        for (Certificate cert : caCerts) {
            trustStore.setCertificateEntry("ca-" + i++, cert);
        }

        // Initialize SSLContext
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, "".toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        return sslContext;
    }
}