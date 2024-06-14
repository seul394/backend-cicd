package cloudclub.blog.posts.config;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private String message;
    private String errorCode;

    protected CustomException() {}

    public CustomException(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
