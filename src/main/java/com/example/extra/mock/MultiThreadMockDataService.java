package com.example.extra.mock;

import com.example.extra.member.MemberRepository;
import com.example.extra.post.PostRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MultiThreadMockDataService {

  private final MemberRepository memberRepository;
  private final PostRepository postRepository;
  private final Faker faker = new Faker(new Locale("ko", "KR"));


}
