package cloudclub.blog.posts.config;

public enum StatusEnum {
    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST");
//    NOT_FOUND(404, "NOT_FOUND"),
//    USER_NOT_FOUND(410, "USER_NOT_FOUND"),
//    TITLE_NOT_FOUND(411, "TITLE_NOT_FOUND"),
//    CONTENTS_NOT_FOUND(412, "CONTENTS_NOT_FOUND");

    int statusCode;
    String message;

    StatusEnum(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

}
