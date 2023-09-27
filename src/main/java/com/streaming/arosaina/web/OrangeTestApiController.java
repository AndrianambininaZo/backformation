package com.streaming.arosaina.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api")
public class OrangeTestApiController {

    private final String orangeApiUrl = "https://api.orange.com/oauth/v3/token";
    private final String authorizationHeader = "Basic bjhWaUdqTFhtcTI2cHEzeURBOEZ6VU15QUM4MnNiSlY6U25RSUMwd1htQW1JSnlnTg==";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/token")
    @ResponseBody
    public ResponseEntity<?> getToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationHeader);
            headers.set("Content-Type", "application/x-www-form-urlencoded");
            headers.set("Accept", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>("grant_type=client_credentials", headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    orangeApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            String responseBody = response.getBody();
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de la demande à l'API Orange.");
        }
    }

    @PostMapping("/webpayment")
    @ResponseBody
    public ResponseEntity<?> initiateWebPayment(@RequestBody String requestBody) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationHeader);
            headers.set("Accept", "application/json");
            headers.set("Content-Type", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    orangeApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            String responseBody = response.getBody();
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de la demande à l'API Orange.");
        }
    }

}

