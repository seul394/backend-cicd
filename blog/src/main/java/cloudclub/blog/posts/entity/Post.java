package cloudclub.blog.posts.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.web.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE post SET del_yn=true WHERE id = ?")
@SQLRestriction("del_yn = false")
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(name = "url")
    private String url;

    @Column(name = "thumbnail_image")
    private String thumbImg;

    @Column(name = "ogTitle")
    private String ogTitle;

    @Column(name = "ogDescription")
    private String ogDescription;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "del_yn")
    private Boolean delYn;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PostHashtag> postHashtags;

    @Builder
    public Post(String title, String contents, Long userId, String url) {
        this.title = title;
        this.contents = contents;
        this.userId = userId;
        this.url = url;
        this.viewCount = 0L;
        this.delYn = false;
    }

    public void updatePostTitle(String title) {
        this.title = title;
    }

    public void updatePostContents(String contents) {
        this.contents = contents;
    }

}
