package com.example.extra.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  public void register(String username, String nickname) {
    memberRepository.save(new Member(username, nickname));
  }
}
