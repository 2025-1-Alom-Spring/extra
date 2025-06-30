package com.example.extra.controller;

import com.example.extra.filter.PostFilterService;
import com.example.extra.post.PostInfoResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
@Slf4j
public class FilterController {

  private final PostFilterService postFilterService;

  @GetMapping("/posts/jpql")
  public Page<PostInfoResponse> filteredPostByJpql(@Nullable String title, @Nullable String content, @Nullable String author) {
    return postFilterService.filteredPostByJpql(title, content, author);
  }

  @GetMapping("/posts/native-query")
  public Page<PostInfoResponse> filteredPostByNativeQuery(@Nullable String title, @Nullable String content, @Nullable String author) {
    return postFilterService.filteredPostByNativeQuery(title, content, author);
  }

  @GetMapping("/posts/query-dsl")
  public Page<PostInfoResponse> filteredPostByQueryDsl(@Nullable String title, @Nullable String content, @Nullable String author) {
    return postFilterService.filteredPostByQueryDsl(title, content, author);
  }
}
