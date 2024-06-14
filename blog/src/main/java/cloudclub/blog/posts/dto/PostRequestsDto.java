package cloudclub.blog.posts.dto;

import cloudclub.blog.posts.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public record PostRequestsDto(
        String title,
        String contents,
        List<String> tagNames
) {

}
