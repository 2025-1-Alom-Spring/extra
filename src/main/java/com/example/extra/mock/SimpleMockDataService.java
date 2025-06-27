package com.example.extra.mock;

import com.example.extra.member.Member;
import com.example.extra.member.MemberRepository;
import com.example.extra.post.Post;
import com.example.extra.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleMockDataService {

  private final MockFactory mockFactory;
  private final PostRepository postRepository;
  private final MemberRepository memberRepository;

  /**
   * for문을 사용한 단순한 Mock 데이터 생성
   * for문 + 개별 저장
   */
  public void generateMockDataWithForLoopAndSaveEach(int count) {
    log.info("=== for문을 사용한 Mock 데이터 생성 시작 ===");
    long startTime = System.currentTimeMillis();

    for (int i = 0; i < count; i++) {
      Member member = mockFactory.createMockMember(); // Mock 회원 생성
      memberRepository.save(member); // Mock 회원 저장
      Post post = mockFactory.createMockPost(member); // Mock 게시글 생성
      postRepository.save(post); // Mock 게시글 저장
    }

    long endTime = System.currentTimeMillis();
    log.info("총 소요시간: {}ms", endTime - startTime);
    log.info("=== {}개의 Post 데이터 생성 완료 ===", count);
  }
}
