package cloudclub.blog.users.dto;

import cloudclub.blog.users.jwt.JwtToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInResponseDto {
    private boolean loginSuccess;
    private String email;
    private JwtToken googleAccessToken;
}