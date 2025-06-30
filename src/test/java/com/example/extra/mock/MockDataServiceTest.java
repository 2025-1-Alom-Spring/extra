package com.example.extra.mock;

import com.example.extra.member.MemberRepository;
import com.example.extra.post.PostJpaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class MockDataServiceTest {

  @Autowired
  MockFactory mockFactory;

  @Autowired
  SimpleMockDataService simpleMockDataService;

  @Autowired
  JdbcMockDataService jdbcMockDataService;

  @Autowired
  MultiThreadMockDataService multiThreadMockDataService;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  PostJpaRepository postJpaRepository;

  List<ResultEntity> resultEntities = new ArrayList<>();

  @BeforeEach
  void setUp() throws ExecutionException, InterruptedException {
    postJpaRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
    simpleMockDataService.generateMockDataWithForLoopAndSaveEach(10); // 웜업 코드로 10개 미리 생성
    simpleMockDataService.generateMockDataWithForLoopAndSaveAll(10);
    jdbcMockDataService.generateMockDataWithJdbcBatchUpdate(10);
    multiThreadMockDataService.generateMockDataInParallel(10);
  }

  @RepeatedTest(10)
  void for문_단일_저장(RepetitionInfo info) {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    simpleMockDataService.generateMockDataWithForLoopAndSaveEach(10);

    stopWatch.stop();
    resultEntities.add(ResultEntity.builder()
        .taskTotalTImeMillis(stopWatch.getTotalTimeMillis())
        .currentRepetition(info.getCurrentRepetition())
        .totalRepetition(info.getTotalRepetitions())
        .build());
  }

  @RepeatedTest(10)
  void for문_saveAll_저장(RepetitionInfo info) {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    simpleMockDataService.generateMockDataWithForLoopAndSaveAll(10);

    stopWatch.stop();
    resultEntities.add(ResultEntity.builder()
        .taskTotalTImeMillis(stopWatch.getTotalTimeMillis())
        .currentRepetition(info.getCurrentRepetition())
        .totalRepetition(info.getTotalRepetitions())
        .build());
  }

  @RepeatedTest(10)
  void jdbc_batch_update_저장(RepetitionInfo info) {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    jdbcMockDataService.generateMockDataWithJdbcBatchUpdate(10);

    stopWatch.stop();
    resultEntities.add(ResultEntity.builder()
        .taskTotalTImeMillis(stopWatch.getTotalTimeMillis())
        .currentRepetition(info.getCurrentRepetition())
        .totalRepetition(info.getTotalRepetitions())
        .build());
  }

  @RepeatedTest(10)
  void 멀티스레드_저장(RepetitionInfo info) throws ExecutionException, InterruptedException {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    multiThreadMockDataService.generateMockDataInParallel(10);

    stopWatch.stop();
    resultEntities.add(ResultEntity.builder()
        .taskTotalTImeMillis(stopWatch.getTotalTimeMillis())
        .currentRepetition(info.getCurrentRepetition())
        .totalRepetition(info.getTotalRepetitions())
        .build());
  }


  @AfterAll
  void afterAll() {
    // 각 테스트 별 로깅
    resultEntities.forEach(r -> {
      log.info("[반복 {}/{}] 테스트 시간: {}ms",
          r.getCurrentRepetition(),
          r.getTotalRepetition(),
          r.getTaskTotalTImeMillis());
    });

    // 평균 계산
    double averageTime = resultEntities.stream()
        .mapToLong(ResultEntity::getTaskTotalTImeMillis)
        .average()
        .orElse(0.0);

    // 평균 로깅
    log.info(">>> 전체 {}회 반복 평균 수행 시간: {}ms",
        resultEntities.size(),
        String.format("%.3f", averageTime));
  }

  @Builder
  @Getter
  private static class ResultEntity {

    private Long taskTotalTImeMillis; // 측정 시간
    private Integer currentRepetition; // 현재 반복 횟수
    private Integer totalRepetition; // 총 반복 횟수
  }
}