package cloudclub.blog.posts.controller;

import cloudclub.blog.posts.dto.PostDto;
import cloudclub.blog.posts.dto.PostRequestsDto;
import cloudclub.blog.posts.config.ResultMessage;
import cloudclub.blog.posts.entity.Post;
import cloudclub.blog.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/v1/post")
    public ResponseEntity<ResultMessage> createPost(@RequestBody PostRequestsDto requestsDto, @RequestHeader Long userId) throws Exception {
        return postService.create(requestsDto, userId);
    }

    @GetMapping("/v1/post")
    public ResponseEntity<ResultMessage> getPost(@RequestParam Long postId, @RequestHeader Long userId) {
        return postService.getPost(postId, userId);
    }

    @GetMapping("/MyBlog.io/@{userId}/{slug}")
    public ResponseEntity<ResultMessage> getPost(@PathVariable Long userId, @PathVariable String slug) {
        return postService.getPostDtoByUrl(userId, slug);
    }

    @DeleteMapping("/v1/post")
    public void deletePost(@RequestParam Long postId) {
        postService.deletePost(postId);
    }

    @PatchMapping("/v1/post")
    public ResponseEntity<ResultMessage> updatePost(@RequestParam Long postId, @RequestBody PostRequestsDto requestsDto) throws Exception {
        return postService.update(postId, requestsDto);
    }
}
