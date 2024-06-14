package cloudclub.blog.posts.repository;

import cloudclub.blog.posts.entity.Post;
import cloudclub.blog.posts.entity.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
    List<PostHashtag> findByPost(Post post);
    List<PostHashtag> findAllByPost(Post post);

}
