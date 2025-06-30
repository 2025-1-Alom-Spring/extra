package com.example.extra.post;

import com.example.extra.member.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostQueryDslRepository {

  private final JPAQueryFactory queryFactory;

  public Page<PostInfoResponse> filteredPostByQueryDsl(String title, String content, String author, Pageable pageable) {
    QPost post = QPost.post;
    QMember member = QMember.member;

    // where 절 BooleanExpression
    BooleanExpression titleCond = title != null ? post.title.contains(title) : null;
    BooleanExpression contentCond = content != null ? post.content.contains(content) : null;
    BooleanExpression authorCond = author != null ? member.nickname.contains(author) : null;

    // SELECT ... FROM ... WHERE
    JPAQuery<PostInfoResponse> query = queryFactory
        .select(Projections.constructor(PostInfoResponse.class, post.title, post.content, member.nickname))
        .from(post)
        .join(post.member, member)
        .where(titleCond, contentCond, authorCond);

    // 페이징 & 정렬
    List<PostInfoResponse> results = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // Count Query
    Long total = queryFactory
        .select(post.id.count())
        .from(post)
        .join(post.member, member)
        .where(titleCond, contentCond, authorCond)
        .fetchOne();

    total = total == null ? 0 : total;

    return new PageImpl<>(results, pageable, total);
  }

}
