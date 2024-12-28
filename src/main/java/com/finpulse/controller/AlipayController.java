package com.finpulse.controller;

import com.alipay.api.response.AlipayUserInfoShareResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.finpulse.model.User;
import com.finpulse.service.AlipayService;
import com.finpulse.service.AuthenticationService;
import com.finpulse.service.UserService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@RestController
@RequestMapping("/alipay")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @GetMapping("/connect")
    public String connectAlipay(
            @RequestParam(required = false) String authCode,
            @RequestParam(required = false) String error,
            HttpServletRequest request) {

        final Logger logger = LoggerFactory.getLogger(AlipayService.class);
        // Log all request parameters for debugging
        logger.info("Received callback from Alipay");
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            logger.info("{}: {}", paramName, request.getParameter(paramName));
        }

        if (authCode == null || authCode.isEmpty()) {
            // Check if auth_code is present instead
            String alternativeAuthCode = request.getParameter("auth_code");
            if (alternativeAuthCode != null && !alternativeAuthCode.isEmpty()) {
                authCode = alternativeAuthCode;
            } else {
                return "No authorization code received from Alipay";
            }
        }

        try {
            User currentUser = authenticationService.getCurrentUser();
            String authToken = alipayService.getAuthToken(authCode);
            currentUser.setAlipayToken(authToken);
            userService.saveUser(currentUser);
            return "redirect:/dashboard?success=true";
        } catch (Exception e) {
            logger.error("Error connecting Alipay account", e);
            return "redirect:/dashboard?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        }
    }

    @GetMapping("/sync")
    public String syncAlipayData() {
        try {
            User currentUser = authenticationService.getCurrentUser();
            String authToken = currentUser.getAlipayToken();
            if (authToken == null) {
                return "Alipay account not connected.";
            }
            AlipayUserInfoShareResponse userInfo = alipayService.getUserInfo(authToken);
            // Process userInfo and update expenses...
            return "Alipay data synchronized successfully!";
        } catch (Exception e) {
            return "Failed to synchronize Alipay data: " + e.getMessage();
        }
    }

    @GetMapping("/disconnect")
    public String disconnectAlipay() {
        try {
            User currentUser = authenticationService.getCurrentUser();
            currentUser.setAlipayToken(null);
            userService.saveUser(currentUser);
            return "Alipay account disconnected successfully!";
        } catch (Exception e) {
            return "Failed to disconnect Alipay account: " + e.getMessage();
        }
    }

    @GetMapping("/alipay/test")
    public String testAlipayRedirect() {
        return "Alipay redirect endpoint is accessible";
    }
}

