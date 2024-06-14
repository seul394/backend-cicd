package cloudclub.blog.users.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOauth {
    @Value("${OAuth2.google.url}")
    private String URL;

    @Value("${OAuth2.google.client-id}")
    private String CLIENT_ID;

    @Value("${OAuth2.google.client-secret}")
    private String CLIENT_SECRET;

    @Value("${OAuth2.google.scope}")
    private String SCOPE;

    @Value("${OAuth2.google.redirect-uri}")
    private String REDIRECT_URI;

    private String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

    private String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("scope", SCOPE);
        params.put("response_type", "code");
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", REDIRECT_URI);

        String parameterString = params.entrySet().stream()
                .map(x->x.getKey()+"="+x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = URL+"?"+parameterString;

        return redirectURL;
    }

    public GoogleOAuthToken requestAccessToken(String code) throws JsonProcessingException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                GOOGLE_TOKEN_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return objectMapper.readValue(responseEntity.getBody(), GoogleOAuthToken.class);
        }
        return new GoogleOAuthToken();
    }

    public ResponseEntity<String> getUserInfo(GoogleOAuthToken googleOAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", googleOAuthToken.getToken_type()+" "+googleOAuthToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_URL, HttpMethod.GET, request, String.class);

        return response;
    }
}
