package com.example.extra.mock;

import com.example.extra.member.Member;
import com.example.extra.post.Post;
import java.util.ArrayList;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MockFactory {

  private final Faker faker = new Faker(new Locale("ko", "KR"));

  /**
   * 회원 Mock 데이터 생성
   */
  public Member createMockMember() {
    // 1. username 랜덤값 설정
    String username = faker.internet().emailAddress();

    // 2. nickname 랜덤값 설정
    String nickname = faker.name().name();

    // 3. Member 생성
    return Member.builder()
        .username(username)
        .nickname(nickname)
        .posts(new ArrayList<>())
        .build();
  }

  /**
   * 게시글 Mock 데이터 생성
   */
  public Post createMockPost(Member member) {
    // 1. title 랜덤값 설정
    String title = faker.lorem().sentence();

    // 2. content 랜덤값 설정
    String content = faker.lorem().paragraph();

    // 3. Post 생성
    return Post.builder()
        .member(member)
        .title(title)
        .content(content)
        .build();
  }

}
