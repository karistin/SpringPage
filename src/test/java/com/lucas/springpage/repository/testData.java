package com.lucas.springpage.repository;

import com.lucas.springpage.dto.QuestionDto;
import com.lucas.springpage.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class testData {

    @Autowired
    QuestionService questionService;

    @Test
    void testInsert() {
        for (int i = 1; i <= 300; i++) {
            questionService.create(QuestionDto.builder()
                .subject(String.format("테스트 데이터 입니다. [%03d]", i))
                .content("내용 없음").build());
        }

    }
}
