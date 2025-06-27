package com.example.extra.member;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Member {

  @Id
  private Long id;

  private String username;

  private String nickname;

  public Member() {
  }

  public Member(String username, String nickname) {
    this.username = username;
    this.nickname = nickname;
  }
}
