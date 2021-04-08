package com.tpay.domains.mypage.presentation;

import com.tpay.domains.mypage.application.MyPageService;
import com.tpay.domains.mypage.application.dto.MyPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyPageController {

  private final MyPageService myPageService;

  @GetMapping("/mypage/{franchiseeId}")
  public ResponseEntity<MyPageResponse> mypage(@PathVariable Long franchiseeId) {
    return myPageService.mypage(franchiseeId);
  }
}
