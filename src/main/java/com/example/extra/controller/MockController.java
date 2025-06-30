package com.example.extra.controller;

import com.example.extra.mock.JdbcMcokDataService;
import com.example.extra.mock.MultiThreadMockDataService;
import com.example.extra.mock.SimpleMockDataService;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
@RequiredArgsConstructor
public class MockController {

  private final SimpleMockDataService simpleMockDataService;
  private final JdbcMcokDataService jdbcMcokDataService;
  private final MultiThreadMockDataService multiThreadMockDataService;

  @PostMapping("/simple/save-each")
  public void generateSimpleMockData1(int count) {
    simpleMockDataService.generateMockDataWithForLoopAndSaveEach(count);
  }

  @PostMapping("/simple/save-all")
  public void generateSimpleMockData2(int count) {
    simpleMockDataService.generateMockDataWithForLoopAndSaveAll(count);
  }

  @PostMapping("/jdbc/batch/update")
  public void generateJdbcMockData2(int count) {
    jdbcMcokDataService.generateMockDataWithJdbcBatchUpdate(count);
  }

  @PostMapping("/multi-thread")
  public void generateMultiThreadMockData1(int count) throws ExecutionException, InterruptedException {
    multiThreadMockDataService.generateMockDataInParallel(count);
  }
}
