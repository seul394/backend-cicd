package cloudclub.blog.posts.config;

public record CustomExceptionModel(
        String message,
        String errorCode
) {
}
