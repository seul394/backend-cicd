package cloudclub.blog.users.controller;

import cloudclub.blog.users.dto.SignInResponseDto;
import cloudclub.blog.users.oauth.GoogleOAuthToken;
import cloudclub.blog.users.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping
public class AuthController {
    private final OauthService oauthService;

    @GetMapping("/login")
    public void googleLogin(HttpServletResponse response) throws Exception {
        oauthService.request(response);
        log.info("google login");
    }

    @GetMapping("/login/oauth2/code/google")
    @ResponseBody
    public SignInResponseDto callback(@RequestParam(name = "code") String code) {
        GoogleOAuthToken token = oauthService.requestAccessToken(code);
        SignInResponseDto signInResponseDto = oauthService.googleLogin(token);
        return signInResponseDto;
    }
}
