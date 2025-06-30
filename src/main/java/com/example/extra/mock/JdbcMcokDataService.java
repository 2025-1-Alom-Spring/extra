package com.example.extra.mock;

import com.example.extra.member.Member;
import com.example.extra.post.Post;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JdbcMcokDataService {

  private final MockFactory mockFactory;
  private final JdbcTemplate jdbcTemplate;

  /**
   * JdbcTemplate.batchUpdate()를 이용한 Mock 데이터 일괄 INSERT
   */
  @Transactional
  public void generateMockDataWithJdbcBatchUpdate(int count) {
    log.info("=== Jdbc batch로 Mock 데이터 생성 시작 (count={}) ===", count);
    long start = System.currentTimeMillis();

    // 1) MEMBER 일괄 INSERT 준비
    String memberSql = "INSERT INTO member(username, nickname) VALUES (?, ?)";
    List<Object[]> memberParams = new ArrayList<>(count);
    // 게시글 데이터를 잠시 보관할 리스트
    List<Post> postCache = new ArrayList<>(count);

    for (int i = 0; i < count; i++) {
      // MockFactory로 Member, Post 생성
      Member member = mockFactory.createMockMember();
      Post post = mockFactory.createMockPost(member);

      memberParams.add(new Object[]{ member.getUsername(), member.getNickname() });
      postCache.add(post);
    }

    // 2) INSERT 전, 다음에 들어올 member.id 범위 예측
    Long nextMemberId = jdbcTemplate.queryForObject(
        "SELECT COALESCE(MAX(id), 0) + 1 FROM member", Long.class);

    // 3) MEMBER 일괄 INSERT
    jdbcTemplate.batchUpdate(memberSql, memberParams);

    // 4) POST 일괄 INSERT 준비
    String postSql = "INSERT INTO post(member_id, title, content) VALUES (?, ?, ?)";
    List<Object[]> postParams = new ArrayList<>(count);

    for (int i = 0; i < count; i++) {
      long memberId = nextMemberId + i;
      Post p = postCache.get(i);
      postParams.add(new Object[]{ memberId, p.getTitle(), p.getContent() });
    }

    // 5) POST 일괄 INSERT
    jdbcTemplate.batchUpdate(postSql, postParams);

    long end = System.currentTimeMillis();
    log.info("총 소요시간: {}ms", (end - start));
    log.info("=== Jdbc batch로 {}개의 Post 데이터 생성 완료 ===", count);
  }
}
