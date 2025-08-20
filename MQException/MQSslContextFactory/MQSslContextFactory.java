import com.ibm.mq.spring.boot.MQConfigurationProperties;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.KeyManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class MQSslContextFactory {

    public static SSLContext createSslContext(MQConfigurationProperties mqConfig) throws Exception {
        // If SSL not enabled, return null
        if (!mqConfig.getSsl().isEnabled()) {
            return null;
        }

        // Pull values from MQConfigurationProperties
        String keyStorePath = mqConfig.getSsl().getKeyStore();
        String keyStorePassword = mqConfig.getSsl().getKeyStorePassword();
        String trustStorePath = mqConfig.getSsl().getTrustStore();
        String trustStorePassword = mqConfig.getSsl().getTrustStorePassword();
        String sslProtocol = mqConfig.getSsl().getCipherSuite() != null 
                ? mqConfig.getSsl().getCipherSuite()   // e.g. "TLSv1.2"
                : "TLSv1.2";

        // Load KeyStore
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream ksStream = new FileInputStream(keyStorePath)) {
            keyStore.load(ksStream, keyStorePassword != null ? keyStorePassword.toCharArray() : null);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keyStorePassword != null ? keyStorePassword.toCharArray() : null);

        // Load TrustStore
        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (FileInputStream tsStream = new FileInputStream(trustStorePath)) {
            trustStore.load(tsStream, trustStorePassword != null ? trustStorePassword.toCharArray() : null);
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        // Create SSLContext
        SSLContext sslContext = SSLContext.getInstance(sslProtocol);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        return sslContext;
    }
}
