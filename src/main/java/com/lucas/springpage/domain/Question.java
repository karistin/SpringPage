package com.lucas.springpage.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
public class Question {

//    질문 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    질문 제목
    @Column(length = 200)
    private String subject;

//    질문 내용
//    글자수 제한이 없음
    @Column(columnDefinition = "TEXT")
    private String content;

    // 여러개의 질문이 1명의 사용자에게 사용됨
    @ManyToOne
    private SiteUser author;

//    질문을 작성한 날짜
//    create_date
    private LocalDateTime createDate;

//    mapping Answer.question , 동시에 같이 삭제
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    private LocalDateTime modifyDate;
}
