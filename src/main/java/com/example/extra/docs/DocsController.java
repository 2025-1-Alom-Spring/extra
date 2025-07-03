package com.example.extra.docs;

import com.chuseok22.apichangelog.annotation.ApiChangeLog;
import com.chuseok22.apichangelog.annotation.ApiChangeLogs;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocsController {

  @ApiChangeLogs({
      @ApiChangeLog(
          date = "2025-07-03",
          author = "Chuseok22",
          description = "API 변경 내역",
          issueUrl = "https://github.com/Alom/spring-study/issues/377"
      )
  })
  @Operation(
      summary = "API 요약",
      description = "API 상세 설명"
  )
  @GetMapping("")
  public void app1() {
    return;
  }
}
