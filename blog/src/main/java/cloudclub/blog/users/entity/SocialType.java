package cloudclub.blog.users.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SocialType {
    NONE("자체 로그인");
    private final String type;
}
