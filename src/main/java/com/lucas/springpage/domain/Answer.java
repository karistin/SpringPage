package com.lucas.springpage.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //    질문 내용
//    글자수 제한이 없음
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private SiteUser author;

    //    질문을 작성한 날짜
//    create_date
    private LocalDateTime createDate;

//    N : 1 관계 (하나의 질문에 존재하는 여러가지 질문들)
//    부모 : 자식 관계
    @ManyToOne
    private Question question;

    private LocalDateTime modifyDate;

    @ManyToMany
    private Set<SiteUser> voter;
}
