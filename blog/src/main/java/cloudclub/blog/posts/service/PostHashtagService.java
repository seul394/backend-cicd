package cloudclub.blog.posts.service;

import cloudclub.blog.posts.entity.Hashtag;
import cloudclub.blog.posts.entity.Post;
import cloudclub.blog.posts.entity.PostHashtag;
import cloudclub.blog.posts.repository.HashtagRepository;
import cloudclub.blog.posts.repository.PostHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostHashtagService {
    private final HashtagService hashtagService;
    private final PostHashtagRepository postHashtagRepository;
    private final HashtagRepository hashtagRepository;

    public void saveHashtag(Post post, List<String> tagNames) {
        //tag가 없는 경우
        if (tagNames.size() == 0) return;

        //tag 있는 경우
        tagNames.stream()
                .map(hashtag ->
                        hashtagService.findByTagName(hashtag)
                                .orElseGet(() -> hashtagService.save(hashtag)))
                .forEach(hashtag -> mapHashtagToPost(post, hashtag));
    }

    //hashtag와 post를 연결하는 DB table에 저장
    private Long mapHashtagToPost(Post post, Hashtag hashtag) {
        hashtag.increaseCnt();
        hashtagRepository.save(hashtag);
        return postHashtagRepository.save(new PostHashtag(post, hashtag)).getId();
    }

    public List<PostHashtag> findHashtagListByPost(Post post) {
        return postHashtagRepository.findAllByPost(post);
    }
}
