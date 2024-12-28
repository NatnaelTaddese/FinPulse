package com.finpulse.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataBillAccountlogQueryRequest;
import com.alipay.api.request.AlipayFundAccountQueryRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipayDataBillAccountlogQueryResponse;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.finpulse.model.Transaction;

import java.util.ArrayList;
import java.util.List;

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

    public String getAccountBalance(String authToken) throws Exception {
        AlipayFundAccountQueryRequest request = new AlipayFundAccountQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("alipay_user_id", authToken);
        request.setBizContent(bizContent.toString());

        AlipayFundAccountQueryResponse response = alipayClient.execute(request, authToken);
        if (response.isSuccess()) {
            return response.getAvailableAmount();
        } else if ("isv.not-online-app".equals(response.getSubCode())) {
            throw new Exception("Application is not online. Balance checking will be available after production approval.");
        } else {
            throw new Exception("Failed to get account balance: " + response.getSubMsg());
        }
    }

    public List<Transaction> getRecentTransactions(String authToken) throws Exception {
        AlipayDataBillAccountlogQueryRequest request = getAlipayDataBillAccountlogQueryRequest();

        AlipayDataBillAccountlogQueryResponse response = alipayClient.execute(request, authToken);
        List<Transaction> transactions = new ArrayList<>();

        if (response.isSuccess()) {
            JSONObject responseJson = JSONObject.parseObject(response.getBody());
            JSONObject alipayResponse = responseJson.getJSONObject("alipay_data_bill_accountlog_query_response");
            JSONArray transactionLogs = alipayResponse.getJSONArray("account_log_list");

            if (transactionLogs != null) {
                for (int i = 0; i < transactionLogs.size(); i++) {
                    JSONObject log = transactionLogs.getJSONObject(i);

                    Transaction transaction = new Transaction();
                    transaction.setAmount(log.getString("amount"));
                    transaction.setDate(log.getString("create_time"));
                    transaction.setDescription(log.getString("memo"));
                    transactions.add(transaction);
                }
            }
            return transactions;
        } else if ("isv.not-online-app".equals(response.getSubCode())) {
            throw new Exception("Application is not online. Transaction fetching will be available after production approval.");
        } else {
            throw new Exception("Failed to get recent transactions: " + response.getSubMsg());
        }
    }

    @NotNull
    private static AlipayDataBillAccountlogQueryRequest getAlipayDataBillAccountlogQueryRequest() {
        AlipayDataBillAccountlogQueryRequest request = new AlipayDataBillAccountlogQueryRequest();

        JSONObject bizContent = new JSONObject();
        bizContent.put("account_type", "USER_BALANCE");
        bizContent.put("start_time", "2024-12-15 00:00:00");
        bizContent.put("end_time", "2024-12-20 23:59:59");
        bizContent.put("page_size", 5);
        bizContent.put("page_no", 1);

        request.setBizContent(bizContent.toString());
        return request;
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