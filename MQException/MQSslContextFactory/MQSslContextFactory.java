public static SSLContext createSslContext(MQConfigurationProperties mqConfig) throws Exception {
    String keyStorePath = mqConfig.getJks().getKeyStore();
    String keyStorePassword = mqConfig.getJks().getKeyStorePassword();
    String trustStorePath = mqConfig.getJks().getTrustStore();
    String trustStorePassword = mqConfig.getJks().getTrustStorePassword();

    if (keyStorePath == null && trustStorePath == null) {
        return null; // SSL not configured
    }

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
        KeyStore trustStore =
