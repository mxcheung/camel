import com.ibm.mq.spring.boot.MQConfigurationProperties;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.KeyManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class MQSslContextFactory {

    public static SSLContext createSslContext(MQConfigurationProperties.Jks jksConfig) throws Exception {
        if (jksConfig == null || 
            (jksConfig.getKeyStore() == null && jksConfig.getTrustStore() == null)) {
            return null; // SSL not configured
        }

        String keyStorePath = jksConfig.getKeyStore();
        String keyStorePassword = jksConfig.getKeyStorePassword();
        String trustStorePath = jksConfig.getTrustStore();
        String trustStorePassword = jksConfig.getTrustStorePassword();

        // Load KeyStore
        KeyManagerFactory kmf = null;
        if (keyStorePath != null) {
            KeyStore keyStore = createKeyStore(keyStorePath, keyStorePassword);
            kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keyStorePassword != null ? keyStorePassword.toCharArray() : null);
        }

        // Load TrustStore
        TrustManagerFactory tmf = null;
        if (trustStorePath != null) {
            KeyStore trustStore = createKeyStore(trustStorePath, trustStorePassword);
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
        }

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(
            kmf != null ? kmf.getKeyManagers() : null,
            tmf != null ? tmf.getTrustManagers() : null,
            new SecureRandom()
        );

        return sslContext;
    }

    private static KeyStore createKeyStore(String path, String password) throws Exception {
        // detect type by extension
        String type = path.endsWith(".p12") || path.endsWith(".pfx") ? "PKCS12" : "JKS";
        KeyStore ks = KeyStore.getInstance(type);
        try (FileInputStream is = new FileInputStream(path)) {
            ks.load(is, password != null ? password.toCharArray() : null);
        }
        return ks;
    }
}
