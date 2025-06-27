package com.example.extra.controller;

import com.example.extra.mock.SimpleMockDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
@RequiredArgsConstructor
public class MockController {

  private final SimpleMockDataService simpleMockDataService;

  @PostMapping("/simple/save-each")
  public void generateSimpleMockData1(int count) {
    simpleMockDataService.generateMockDataWithForLoopAndSaveEach(count);
  }

  @PostMapping("/simple/save-all")
  public void generateSimpleMockData2(int count) {
    simpleMockDataService.generateMockDataWithForLoopAndSaveAll(count);
  }
}
