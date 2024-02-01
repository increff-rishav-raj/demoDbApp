//package com.example.local.demoDbApp;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.core.env.*;
//import org.springframework.core.io.support.EncodedResource;
//import org.springframework.core.io.support.PropertySourceFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.vault.authentication.ClientAuthentication;
//import org.springframework.vault.authentication.TokenAuthentication;
//import org.springframework.vault.client.VaultEndpoint;
//import org.springframework.vault.core.VaultTemplate;
//import org.springframework.vault.support.VaultResponse;
//
//import java.io.FileOutputStream;
//import java.util.*;
//
//import static com.example.local.demoDbApp.VaultConstants.*;
//
//@Component
//@Log4j2
//public class CustomPropertyLocator implements PropertySourceFactory {
//    private String url;
//    private int port;
//    private String token;
//    private String path;
//
//
//    private void readPropertiesFromCustomServer() {
//        VaultResponse response = getPropertiesFromVault();
//        if(Objects.nonNull(response)) {
//            MapPropertySource appPropertiesFromVault = convertToProperties(response);
//            syncLocalPropertiesWithVault(appPropertiesFromVault);
//        }
//    }
//
//    private void syncLocalPropertiesWithVault(MapPropertySource appPropertiesFromVault) {
//        String propertiesFile = "application.properties";
//        Boolean isError = false;
//        try (FileOutputStream output = new FileOutputStream(propertiesFile, false)) {
//            Properties properties = new Properties();
//            properties.putAll(appPropertiesFromVault.getSource());
//            if (!properties.isEmpty()) {
//                properties.store(output, null);
//            }
//        } catch (Throwable e) {
//            log.error("Could not sync local properties with vault, received error: " + e.getMessage().substring(0,2500));
//            isError = true;
//        }
//
//        if(!isError)
//            log.info("Successfully synced properties with Vault");
//
//    }
//
//    public static MapPropertySource convertToProperties(VaultResponse response) {
//        LinkedHashMap<String, String> vaultResponse = (LinkedHashMap<String, String>) response.getData().get("data");
//        Map<String, Object> properties = new HashMap<>();
//        vaultResponse.forEach((key, value) -> properties.put(key, value));
//        return new MapPropertySource("vault-properties", properties);
//    }
//
//    private VaultResponse getPropertiesFromVault() {
//        VaultTemplate vaultTemplate = new VaultTemplate(vaultEndpoint(), clientAuthentication());
//        VaultResponse response = null;
//        try {
//            response = vaultTemplate.read("kv/data/internal/demoDbApp/dev");
//            if(Objects.isNull(response)) {
//                log.error("Could not fetch response from vault: Invalid vault file path: " + new Exception().getMessage().substring(0,100));
//            }
//        } catch (Exception e) {
//            log.error("Error connecting to vault: " + e.getMessage().substring(0,100));
//        }
//        return response;
//    }
//
//    private void setELKConfigFromFile(){
//        url = System.getenv(VAULT_URI);
//        port = Integer.parseInt(System.getenv(VAULT_PORT));
//        token = System.getenv(VAULT_TOKEN);
//        path = System.getenv(VAULT_PATH);
//    }
//
//    @Override
//    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
//        setELKConfigFromFile();
//        // Call service to get properties
//        readPropertiesFromCustomServer();
//        return new MapPropertySource(name, Collections.emptyMap());
//    }
//
//
//    public VaultEndpoint vaultEndpoint() {
//        return VaultEndpoint.create(url, port);
//    }
//
//    public ClientAuthentication clientAuthentication() {
//        return new TokenAuthentication(token);
//    }
//
//}