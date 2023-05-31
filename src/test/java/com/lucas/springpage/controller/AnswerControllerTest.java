package com.lucas.springpage.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.lucas.springpage.domain.Question;
import com.lucas.springpage.dto.AnswerDto;
import com.lucas.springpage.dto.QuestionDto;
import com.lucas.springpage.service.AnswerService;
import com.lucas.springpage.service.QuestionService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AnswerController.class)
class AnswerControllerTest {

    @MockBean
    QuestionService questionService;
    @MockBean
    AnswerService answerService;

    @Autowired
    MockMvc mockMvc;

    private static final String BASE_URL = "/answer";
    private static final Long id = 1L;

    void setQuestion() {
        Question question = new Question();
        question.setId(id);
        question.setSubject("Spring DI");
        question.setContent("what is Autowired?");
        question.setCreateDate(LocalDateTime.now());
        question.setAnswerList(new ArrayList<>());
        given(questionService.getQuestion(id)).willReturn(question);
    }
    @Test
    @DisplayName("답변 등록")
    void answerCreate() throws Exception {
        setQuestion();
        mockMvc.perform(post(BASE_URL + "/create/" + id)
                .param("content", "EASY"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/question/detail/" + id));
    }

    @Test
    @DisplayName("답변 등록 - 내용 없음")
    void answerCreateNoContent() throws Exception {
        setQuestion();
        mockMvc.perform(post(BASE_URL + "/create/" +id))
            .andExpect(status().isOk())
            .andExpect(view().name("question_detail"))
            .andExpect(model().attributeExists("question"))
            .andExpect(model().hasErrors());
    }
}