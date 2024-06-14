package cloudclub.blog.posts.config;

import cloudclub.blog.posts.config.StatusEnum;
import lombok.Data;

@Data
public class ResultMessage {

    private StatusEnum status;
    private String message;
    private Object data;

    public ResultMessage() {
        this.status = StatusEnum.BAD_REQUEST;
        this.data = null;
        this.message = null;
    }
}
