package cloudclub.blog.posts.service;

import cloudclub.blog.posts.config.CustomException;
import cloudclub.blog.posts.config.StatusEnum;
import cloudclub.blog.posts.dto.PostDto;
import cloudclub.blog.posts.dto.PostRequestsDto;
import cloudclub.blog.posts.config.ResultMessage;
import cloudclub.blog.posts.dto.PostResponseDto;
import cloudclub.blog.posts.entity.Hashtag;
import cloudclub.blog.posts.entity.Post;
import cloudclub.blog.posts.entity.PostHashtag;
import cloudclub.blog.posts.repository.PostHashtagRepository;
import cloudclub.blog.posts.repository.PostRepository;
import cloudclub.blog.posts.repository.HashtagRepository;
import cloudclub.blog.posts.util.SlugUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostHashtagService postHashtagService;
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final HashtagService hashtagService;
    private final PostHashtagRepository postHashtagRepository;

    /*
    게시글 등록
     */
    public ResponseEntity<ResultMessage> create(PostRequestsDto postRequestsDto, Long userId) throws Exception {
        //예외처리

        if (postRequestsDto.title() == null) {
            throw new CustomException("Title cannot be null", "411");
        }
        if (postRequestsDto.contents() == null) {
            throw new CustomException("Content cannot be null", "412");
        }

        //url
        String slug = SlugUtil.toSlug(postRequestsDto.title());

        //게시글 저장
        Post post = postRepository.save(
                Post.builder()
                        .title(postRequestsDto.title())
                        .contents(postRequestsDto.contents())
                        .userId(userId)
                        .url("MyBlog.io/@"+userId+"/"+slug)    //추후에 userId가 아닌 userNickname으로 변경
                        .build()
        );

        PostResponseDto postResponseDto = PostResponseDto.builder()
                .title(postRequestsDto.title())
                .contents(postRequestsDto.contents())
                .tagNames(postRequestsDto.tagNames())
                .userId(userId)
                .url("MyBlog.io/@"+userId+"/"+slug)
                .build();


        //해시태그 저장
        postHashtagService.saveHashtag(post, postRequestsDto.tagNames());

        //response
        ResultMessage message = new ResultMessage();
        HttpHeaders headers = new HttpHeaders();

        message.setStatus(StatusEnum.OK);
        message.setData(postResponseDto);
        message.setMessage("Post created");
        headers.set("userId", String.valueOf(userId));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    /*
    게시글 불러오기
     */
    public ResponseEntity<ResultMessage> getPost(Long postId, Long userId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException("Post Not Found", "413"));
        List<PostHashtag> postHashtags = postHashtagRepository.findByPost(post);
        Stream<String> streamTagName = postHashtags.stream()
                .map(postHashtag -> postHashtag.getHashtag().getTagName());
        List<String> listTagName = streamTagName.collect(Collectors.toList());

        PostDto postGetDto = PostDto.builder()
                .title(post.getTitle())
                .contents(post.getContents())
                .tagNames(listTagName)
                .build();

        //response
        ResultMessage message = new ResultMessage();
        HttpHeaders headers = new HttpHeaders();

        message.setStatus(StatusEnum.OK);
        message.setData(postGetDto);
        message.setMessage("Post Get");
        headers.set("userId", String.valueOf(userId));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    /*
    slug로 불러오기
     */
    public ResponseEntity<ResultMessage> getPostDtoByUrl(Long userId, String slug) {
        String url = "MyBlog.io/@"+userId+"/"+slug;
        Post post = postRepository.findByUrl(url).orElseThrow(() -> new CustomException("Post Not Found", "413"));

        List<PostHashtag> postHashtags = postHashtagRepository.findByPost(post);
        Stream<String> streamTagName = postHashtags.stream()
                .map(postHashtag -> postHashtag.getHashtag().getTagName());
        List<String> listTagName = streamTagName.collect(Collectors.toList());

        PostDto postDto = PostDto.builder()
                .title(post.getTitle())
                .contents(post.getContents())
                .tagNames(listTagName)
                .build();
        //response
        ResultMessage message = new ResultMessage();
        HttpHeaders headers = new HttpHeaders();

        message.setStatus(StatusEnum.OK);
        message.setData(postDto);
        message.setMessage("Post Get");
        headers.set("userId", String.valueOf(userId));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    /*
    게시글 삭제: delYn = True로 변경
     */
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    /*
    게시글 수정
     */
    public ResponseEntity<ResultMessage> update(Long postId, PostRequestsDto postRequestsDto) throws Exception {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException("Post Not Found", "413"));

        if (postRequestsDto.title() != null) {
            post.updatePostTitle(postRequestsDto.title());
        }

        if (postRequestsDto.contents() != null) {
            post.updatePostContents(postRequestsDto.contents());
        }
        postRepository.save(post);

        //해시태그 처리. 기존 해시태그 제거
        if (postRequestsDto.tagNames().size() != 0) {
            List<PostHashtag> postHashtags = postHashtagRepository.findByPost(post);
            postHashtags.stream()
                            .forEach(postHashtag ->
                                    {postHashtagRepository.delete(postHashtag);

                                        Hashtag hashtag = postHashtag.getHashtag();
                                        hashtagService.removeHashtag(hashtag);
                                    });
        }

        //새로운 해시 태그 저장: postHashtag, hashtag
        postHashtagService.saveHashtag(post, postRequestsDto.tagNames());


        //response
        ResultMessage message = new ResultMessage();
        HttpHeaders headers = new HttpHeaders();

        message.setStatus(StatusEnum.OK);
        message.setData(postRequestsDto);
        message.setMessage("Post Patch");

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

}
