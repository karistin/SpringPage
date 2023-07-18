package com.lucas.springpage.repository;

import com.lucas.springpage.domain.Question;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Question findBySubject (String subject);

    Question findBySubjectAndContent(String subject, String content);

    List<Question> findBySubjectLike(String subject);

    Page<Question> findAll(Pageable pageable);

//    Specification를 사용하여 Query 작성 방식
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);

//    직접 쿼리를 짜는 방식
    @Query("select "
        + "distinct q "
        + "from Question q "
        + "left outer join SiteUser u1 on q.author=u1 "
        + "left outer join Answer a on a.question=q "
        + "left outer join SiteUser u2 on a.author=u2 "
        + "where "
        + "   q.subject like %:kw% "
        + "   or q.content like %:kw% "
        + "   or u1.username like %:kw% "
        + "   or a.content like %:kw% "
        + "   or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
