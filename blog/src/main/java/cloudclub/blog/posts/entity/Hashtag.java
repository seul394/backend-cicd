package cloudclub.blog.posts.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Hashtag extends BaseEntity {

    private String tagName;
    private int cnt = 0;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PostHashtag> postHashtags;

    @Builder
    public Hashtag(String tagName) {
        this.tagName = tagName;
    }

    public void increaseCnt() {
        this.cnt += 1;
    }

    public void decreaseCnt() {
        this.cnt -= 1;
    }

}
