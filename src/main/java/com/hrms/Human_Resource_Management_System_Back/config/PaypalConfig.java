package com.hrms.Human_Resource_Management_System_Back.config;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for setting up PayPal SDK integration.
 * <p>
 * This class defines the required beans and configuration values needed to initialize
 * the PayPal API context used for processing payments through the PayPal REST API.
 * </p>
 */
@Configuration
public class PaypalConfig {

    /**
     * The PayPal client ID, injected from application properties.
     */
    @Value("${paypal.client-id}")
    private String clientId;

    /**
     * The PayPal client secret, injected from application properties.
     */
    @Value("${paypal.client-secret}")
    private String clientSecret;

    /**
     * The mode of operation for the PayPal API (e.g., "sandbox" or "live").
     */
    @Value("${paypal.mode}")
    private String mode;

    /**
     * Creates a configuration map for the PayPal SDK.
     *
     * @return a map containing PayPal SDK configuration parameters
     */
    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);
        return configMap;
    }


    /**
     * Creates an OAuthTokenCredential used to authenticate with the PayPal API.
     *
     * @return a new {@link OAuthTokenCredential} with the configured client credentials
     */
    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    /**
     * Creates and configures an {@link APIContext} used to interact with the PayPal API.
     *
     * @return a configured {@link APIContext}
     * @throws PayPalRESTException if the token retrieval fails
     */
    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalSdkConfig());
        return context;
    }
}
