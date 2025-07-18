package com.example.extra.mock;

import com.example.extra.member.Member;
import com.example.extra.member.MemberRepository;
import com.example.extra.post.Post;
import com.example.extra.filter.PostJpaRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleMockDataService {

  private final MockFactory mockFactory;
  private final PostJpaRepository postJpaRepository;
  private final MemberRepository memberRepository;

  /**
   * for문을 사용한 단순한 Mock 데이터 생성
   * for문 + 개별 저장
   */
  public void generateMockDataWithForLoopAndSaveEach(int count) {
    log.info("=== for문과 개별 저장을 사용한 Mock 데이터 생성 시작 ===");
    long startTime = System.currentTimeMillis();

    for (int i = 0; i < count; i++) {
      Member member = mockFactory.createMockMember(); // Mock 회원 생성
      memberRepository.save(member); // Mock 회원 저장
      Post post = mockFactory.createMockPost(member); // Mock 게시글 생성
      postJpaRepository.save(post); // Mock 게시글 저장
    }

    long endTime = System.currentTimeMillis();
    log.info("총 소요시간: {}ms", endTime - startTime);
    log.info("=== {}개의 Post 데이터 생성 완료 ===", count);
  }

  /**
   * for문을 사용한 단순한 Mock 데이터 저장
   * for문 + saveAll()
   */
  public void generateMockDataWithForLoopAndSaveAll(int count) {
    log.info("=== for문과 saveAll()을 사용한 Mock 데이터 생성 시작 ===");
    long startTime = System.currentTimeMillis();

    List<Member> members = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      Member member = mockFactory.createMockMember(); // Mock 회원 생성
      Post post = mockFactory.createMockPost(member); // Mock 게시글 생성
      member.addPost(post); // 양방향 연관관계 설정
      members.add(member);
    }

    // CASCADE로 회원과 게시글 동시 저장
    memberRepository.saveAll(members);

    long endTime = System.currentTimeMillis();
    log.info("총 소요시간: {}ms", endTime - startTime);
    log.info("=== {}개의 Post 데이터 생성 완료 ===", count);
  }
}
