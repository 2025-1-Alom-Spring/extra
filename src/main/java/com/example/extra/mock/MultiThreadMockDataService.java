package com.example.extra.mock;

import com.example.extra.member.Member;
import com.example.extra.member.MemberRepository;
import com.example.extra.post.Post;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MultiThreadMockDataService {

  private final MockFactory mockFactory;
  private final MemberRepository memberRepository;
  private final ApplicationContext ctx;

  // 사용할 쓰레드 수 (CPU 코어 수나, 최대 커넥션 풀 크기에 맞춰 조정)
  private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

  /**
   * 멀티스레드로 전체 count를 분할 처리
   */
  public void generateMockDataInParallel(int totalCount) throws InterruptedException, ExecutionException {
    log.info("=== 멀티스레드 Mock 데이터 생성 시작 (totalCount={}) ===", totalCount);
    log.info("스레드 개수: {}", THREAD_COUNT);
    long startTime = System.currentTimeMillis();

    // 1) 카운트를 스레드 수만큼 분배
    int base = totalCount / THREAD_COUNT;
    int rem = totalCount % THREAD_COUNT;

    ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
    List<Future<?>> futures = new ArrayList<>();

    for (int i = 0; i < THREAD_COUNT; i++) {
      // 나머지(rem)는 앞쪽 스레드에 1씩 추가 분배
      final int chunk = base + (i < rem ? 1 : 0);

      futures.add(executor.submit(() -> {
        // AOP 프록시를 통해 트랜잭션이 적용된 메서드를 호출
        ctx.getBean(MultiThreadMockDataService.class).saveChunk(chunk);
      }));
    }

    // 모든 스레드 완료 대기
    for (Future<?> f : futures) {
      f.get();
    }
    executor.shutdown();

    long endTime = System.currentTimeMillis();
    log.info("=== 멀티스레드 Mock 데이터 생성 완료. 총 소요시간: {}ms ===", endTime - startTime);
  }

  /**
   * 각 스레드가 독립 트랜잭션으로 실행할 메서드
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveChunk(int count) {
    long startTime = System.currentTimeMillis();

    List<Member> members = new ArrayList<>(count);

    // 2) Mock 데이터 생성 & 연관관계 세팅
    for (int i = 0; i < count; i++) {
      Member m = mockFactory.createMockMember();
      Post p = mockFactory.createMockPost(m);
      m.addPost(p);
      members.add(m);
    }

    // 3) 각 스레드별로 saveAll() 호출
    memberRepository.saveAll(members);

    long endTime = System.currentTimeMillis();
    log.info("Thread[{}] - {}건 저장 완료 ({}ms)", Thread.currentThread().getName(), count, endTime - startTime);
  }
}
