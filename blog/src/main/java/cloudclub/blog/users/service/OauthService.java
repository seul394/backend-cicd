package cloudclub.blog.users.service;

import cloudclub.blog.users.dto.GoogleAccountDto;
import cloudclub.blog.users.dto.SignInResponseDto;
import cloudclub.blog.users.entity.User;
import cloudclub.blog.users.oauth.GoogleOAuthToken;
import cloudclub.blog.users.oauth.GoogleOauth;
import cloudclub.blog.users.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {
    private final GoogleOauth googleOauth;
    private final UserRepository userRepository;
    private final UserService userService;

    public void request(HttpServletResponse response) throws IOException {
        String redirectURL = googleOauth.getOauthRedirectURL();
        response.sendRedirect(redirectURL);
    }

    public GoogleOAuthToken requestAccessToken(String code) {
        try {
            return googleOauth.requestAccessToken(code);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new GoogleOAuthToken();
        }
    }

    public GoogleAccountDto requestUserInfo(GoogleOAuthToken token) {
        ResponseEntity<String> userInfo = googleOauth.getUserInfo(token);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(userInfo.getBody(), GoogleAccountDto.class);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }
        return null;
    }


    public SignInResponseDto googleLogin(GoogleOAuthToken token) {
        GoogleAccountDto googleAccountInfo = requestUserInfo(token);
        if(userRepository.existsByEmail(googleAccountInfo.getEmail())) {
            Optional<User> user = userRepository.findByEmail(googleAccountInfo.getEmail());
            if (user.isPresent()) {
                return new SignInResponseDto(true, user.get().getEmail(), userService.login(user.get().getEmail(), null));
            }
        }

        userService.register(googleAccountInfo);
        return new SignInResponseDto(true, googleAccountInfo.getEmail(), userService.login(googleAccountInfo.getEmail(), null));
    }
}
