package com.example.extra.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostInfoResponse {
  private String title; // 글 제목
  private String content; // 글 본문
  private String author; // 작성자 닉네임
}
