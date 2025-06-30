package com.example.extra.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

  @Query("""
      SELECT p
      FROM Post p
      JOIN p.member m
      WHERE (:title IS NULL OR p.title LIKE CONCAT('%', :title, '%'))
      AND (:content IS NULL OR p.content LIKE CONCAT('%', :content, '%'))
      AND (:author IS NULL OR m.nickname LIKE CONCAT('%', :author, '%'))
      """)
  Page<Post> filteredPostByJpql(
      @Param("title") String title,
      @Param("content") String content,
      @Param("author") String author,
      Pageable pageable
  );


  @Query(value = """
      SELECT p.*
      FROM post p
      JOIN member m ON p.member_id = m.id
      WHERE (:title   IS NULL OR p.title   LIKE CONCAT('%', :title,   '%'))
        AND (:content IS NULL OR p.content LIKE CONCAT('%', :content, '%'))
        AND (:author  IS NULL OR m.nickname LIKE CONCAT('%', :author,  '%'))
      """,
      countQuery = """
          SELECT COUNT(*)
          FROM post p
          JOIN member m ON p.member_id = m.id
          WHERE (:title   IS NULL OR p.title   LIKE CONCAT('%', :title,   '%'))
            AND (:content IS NULL OR p.content LIKE CONCAT('%', :content, '%'))
            AND (:author  IS NULL OR m.nickname LIKE CONCAT('%', :author,  '%'))
          """,
      nativeQuery = true)
  Page<Post> filteredPostByNativeQuery(
      @Param("title") String title,
      @Param("content") String content,
      @Param("author") String author,
      Pageable pageable
  );
}
