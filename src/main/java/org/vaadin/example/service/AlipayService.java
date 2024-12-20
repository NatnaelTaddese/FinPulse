package org.vaadin.example.service;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AlipayService {

    private static final Logger logger = LoggerFactory.getLogger(AlipayService.class);
    private final AlipayClient alipayClient;

    public AlipayService(
            @Value("${alipay.appId}") String appId,
            @Value("${alipay.privateKey}") String privateKey,
            @Value("${alipay.publicKey}") String publicKey,
            @Value("${alipay.serverUrl}") String serverUrl) {
        try {
            this.alipayClient = new DefaultAlipayClient(serverUrl, appId, privateKey, "json", "UTF-8", publicKey, "RSA2");
        } catch (Exception e) {
            logger.error("Error creating AlipayClient", e);
            throw e;
        }
    }

    public AlipayUserInfoShareResponse getUserInfo(String authToken) throws Exception {
        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        return alipayClient.execute(request, authToken);
    }

    public String getAuthToken(String authCode) throws Exception {
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(authCode);
        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return response.getAccessToken();
        } else {
            throw new Exception("Failed to get auth token: " + response.getSubMsg());
        }
    }
}