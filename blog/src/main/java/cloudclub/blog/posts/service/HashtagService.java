package cloudclub.blog.posts.service;

import cloudclub.blog.posts.config.CustomException;
import cloudclub.blog.posts.entity.Hashtag;
import cloudclub.blog.posts.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public Optional<Hashtag> findByTagName(String tagName) {
        return hashtagRepository.findByTagName(tagName);
    }

    public Hashtag save(String tagName) {

        return hashtagRepository.save(
                Hashtag.builder()
                        .tagName(tagName)
                        .build());
    }

    public void checkHashtag(Hashtag hashtag) {
        int cnt = hashtag.getCnt();
        if (cnt == 0) {
            hashtagRepository.delete(hashtag);
        }
    }

    public void removeHashtag(Hashtag hashtag) {
        hashtag.decreaseCnt();
        hashtagRepository.save(hashtag);
        checkHashtag(hashtag);
    }

}
