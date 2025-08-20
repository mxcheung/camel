import com.ibm.mq.spring.boot.MQConfigurationProperties;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.KeyManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class MQSslContextFactory {

    public static SSLContext createSslContext(MQConfigurationProperties mqConfig) throws Exception {
        // Only build SSLContext if keystore/truststore is configured
        if (mqConfig.getSslKeyStore() == null && mqConfig.getSslTrustStore() == null) {
            return null;
        }

        String keyStorePath = mqConfig.getSslKeyStore();
        String keyStorePassword = mqConfig.getSslKeyStorePassword();
        String trustStorePath = mqConfig.getSslTrustStore();
        String trustStorePassword = mqConfig.getSslTrustStorePassword();
        String sslProtocol = mqConfig.getSslCipherSuite() != null ? "TLSv1.2" : "TLS";

        // Load KeyStore
        KeyManagerFactory kmf = null;
        if (keyStorePath != null) {
            KeyStore keyStore = KeyStore.getInstance("JKS"); // or PKCS12 if needed
            try (FileInputStream ksStream = new FileInputStream(keyStorePath)) {
                keyStore.load(ksStream, keyStorePassword != null ? keyStorePassword.toCharArray() : null);
            }
            kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keyStorePassword != null ? keyStorePassword.toCharArray() : null);
        }

        // Load TrustStore
        TrustManagerFactory tmf = null;
        if (trustStorePath != null) {
            KeyStore trustStore = KeyStore.getInstance("JKS");
            try (FileInputStream tsStream = new FileInputStream(trustStorePath)) {
                trustStore.load(tsStream, trustStorePassword != null ? trustStorePassword.toCharArray() : null);
            }
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
        }

        // Create SSLContext
        SSLContext sslContext = SSLContext.getInstance(sslProtocol);
        sslContext.init(
                kmf != null ? kmf.getKeyManagers() : null,
                tmf != null ? tmf.getTrustManagers() : null,
                new SecureRandom()
        );

        return sslContext;
    }
}
