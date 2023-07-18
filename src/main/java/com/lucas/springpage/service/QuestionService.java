package com.lucas.springpage.service;

import com.lucas.springpage.domain.Answer;
import com.lucas.springpage.domain.Question;
import com.lucas.springpage.domain.SiteUser;
import com.lucas.springpage.dto.QuestionDto;
import com.lucas.springpage.exception.DataNotFoundException;
import com.lucas.springpage.exception.ErrorCode;
import com.lucas.springpage.repository.QuestionRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

//    will be not use
//    public List<QuestionDto> getList() {
//        return questionRepository.findAll().stream().map(
//            this::questionToDto
//        ).collect(Collectors.toList());
//    }

    private QuestionDto questionToDto(Question question) {
        return QuestionDto.builder()
            .id(question.getId())
            .subject(question.getSubject())
            .content(question.getContent())
            .answerList(question.getAnswerList())
            .createDate(question.getCreateDate()).build();
    }
    public QuestionDto getQuestionDto(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(
            () -> new DataNotFoundException(ErrorCode.NOT_FOUND, "해당 question ID가 없습니다.")
        );
        return questionToDto(question);
    }
    public Question getQuestion(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(
            () -> new DataNotFoundException(ErrorCode.NOT_FOUND, "해당 question ID가 없습니다.")
        );
        return question;
    }
    public void create(QuestionDto questionDto, SiteUser siteUser) {
        Question question = new Question();
        question.setSubject(questionDto.getSubject());
        question.setContent(questionDto.getContent());
        question.setCreateDate(LocalDateTime.now());
        question.setAuthor(siteUser);
        questionRepository.save(question);
    }

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> search = this.search(kw);
        return questionRepository.findAll(search, pageable);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        questionRepository.save(question);
    }

    public void updateViewer(Question question) {
        // 조회수 업데이트
        Long viewer = question.getViewer();
        if (viewer != null) {
            question.setViewer(++viewer);
        } else {
            //default value
            question.setViewer(1L);
        }
        questionRepository.save(question);
    }
    public void delete(Question question) {
        questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        questionRepository.save(question);
    }
/*
    select
    distinct q.id,
    (...)
    from question q
    // 합집합(outer)으로
    left outer join site_user u1 on **q.author_id=u1.id**
    u1 : <질문 유저> left outer Join
    left outer join answer a on q.id=a.question_id
    a : <질문, 답글>
    left outer join site_user u2 on a.author_id=u2.id
    u2 : <답글, 유저>
        where
        // 스프링이 제목, 내용, 유저 이름 댓글, 댓글 유저
    q.subject like '%스프링%'
    or q.content like '%스프링%'
    or u1.username like '%스프링%'
    or a.content like '%스프링%'
    or u2.username like '%스프링%'
    */
    private Specification<Question> search(String kw) {
        return new Specification<Question>() {
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query,
                CriteriaBuilder cb) {
                query.distinct(true); // sql distinct
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2  = q.join("author", JoinType.LEFT);

                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                    cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                    cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                    cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                    cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

}
