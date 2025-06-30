package com.example.extra.filter;

import com.example.extra.post.Post;
import com.example.extra.post.PostInfoResponse;
import com.example.extra.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostFilterService {

  private final PostRepository postRepository;

  /**
   * 게시글 필터링 조회 (JPQL)
   * 1. 제목 검색
   * 2. 본문 검색
   * 3. 작성자 닉네임 검색
   */
  public Page<PostInfoResponse> filteredPostByJpql(String title, String content, String author) {
    // Pageable 설정
    Pageable pageable = PageRequest.of(0, 30);

    // DB 조회
    Page<Post> posts = postRepository.filteredPostByJpql(
        title,
        content,
        author,
        pageable
    );

    return posts.map(this::toDTO);
  }

  public Page<PostInfoResponse> filteredPostByNativeQuery(String title, String content, String author) {
    // Pageable 설정
    Pageable pageable = PageRequest.of(0, 30);

    // DB 조회
    Page<Post> posts = postRepository.filteredPostByNativeQuery(
        title,
        content,
        author,
        pageable
    );

    return posts.map(this::toDTO);
  }

  private PostInfoResponse toDTO(Post post) {
    return PostInfoResponse.builder()
        .title(post.getTitle())
        .content(post.getContent())
        .author(post.getMember().getNickname())
        .build();
  }
}
