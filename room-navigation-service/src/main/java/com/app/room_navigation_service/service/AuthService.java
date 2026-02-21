package com.app.room_navigation_service.service;

import com.app.room_navigation_service.DTO.CmuEntraIDBasicInfoDTO;
import com.app.room_navigation_service.DTO.EntraTokenResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    @Value("${cmu.entraid.get.token.url}")
    private String tokenUrl;

    @Value("${cmu.entraid.redirect.url}")
    private String redirectUrl;

    @Value("${cmu.entraid.client.id}")
    private String clientId;

    @Value("${cmu.entraid.client.secret}")
    private String clientSecret;

    @Value("${scope}")
    private String scope;

    @Value("${cmu.entraid.get.basic.info.url}")
    private String basicInfoUrl;


    public CmuEntraIDBasicInfoDTO getCMUBasicInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<CmuEntraIDBasicInfoDTO> response = restTemplate.exchange(
                    basicInfoUrl,
                    HttpMethod.GET,
                    entity,
                    CmuEntraIDBasicInfoDTO.class
            );

            return response.getBody();

        } catch (Exception e) {
            // Log error: e.getMessage()
            return null;
        }
    }

    public String getEntraIDAccessToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("code", authorizationCode);
            map.add("redirect_uri", redirectUrl);
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("scope", scope);
            map.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<EntraTokenResponseDTO> response = restTemplate.postForEntity(
                    tokenUrl,
                    request,
                    EntraTokenResponseDTO.class
            );

            return response.getBody() != null ? response.getBody().getAccessToken() : null;

        } catch (Exception e) {
            return null;
        }
    }
}
