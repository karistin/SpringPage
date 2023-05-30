package com.lucas.springpage.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.lucas.springpage.domain.Question;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void testJpa() {
        Question q1 = new Question();
        q1.setSubject("What is Spring?");
        q1.setContent("How to make good spring server");
        q1.setCreateDate(LocalDateTime.now());
        questionRepository.save(q1);

        Question q2 = new Question();
        q2.setSubject("What is java?");
        q2.setContent("How to hello world");
        q2.setCreateDate(LocalDateTime.now());
        questionRepository.save(q2);

        List<Question> findall = questionRepository.findAll();

        assertEquals(2, findall.size());
        assertEquals("What is Spring?", findall.get(0).getSubject());
    }
}