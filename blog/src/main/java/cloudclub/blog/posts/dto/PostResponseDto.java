package cloudclub.blog.posts.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PostResponseDto {
    String title;
    String contents;
    List<String> tagNames;
    Long userId;
    String url;

}
