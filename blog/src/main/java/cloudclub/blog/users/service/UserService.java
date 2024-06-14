package cloudclub.blog.users.service;

import cloudclub.blog.users.dto.GoogleAccountDto;
import cloudclub.blog.users.entity.User;
import cloudclub.blog.users.jwt.JwtToken;
import cloudclub.blog.users.jwt.JwtTokenProvider;
import cloudclub.blog.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public JwtToken login(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        return jwtTokenProvider.generateToken(authenticationToken);
    }

    public User register(GoogleAccountDto googleAccountDto) {
        try {
            User user = User.builder()
                    .email(googleAccountDto.getEmail())
                    .name(googleAccountDto.getName())
                    .profile(googleAccountDto.getPicture())
                    .role("USER")
                    .build();
            return userRepository.save(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
