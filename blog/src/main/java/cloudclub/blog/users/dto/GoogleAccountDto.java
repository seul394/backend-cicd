package cloudclub.blog.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleAccountDto {
    private String id;
    private String email;
    private String name;
    private String picture;
}