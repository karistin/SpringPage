package com.lucas.springpage.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.springpage.domain.Question;
import com.lucas.springpage.dto.QuestionDto;
import com.lucas.springpage.service.QuestionService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//https://rieckpil.de/test-thymeleaf-controller-endpoints-with-spring-boot-and-mockmvc/


//Import(SecurityConfig.class)
@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QuestionService questionService;

    private static final String BASE_URL = "/question";

    @Test
    @DisplayName("질문 목록 API")
    void questionList() throws Exception {
        List<QuestionDto> questionDtoList = new ArrayList<>();
        questionDtoList.add(QuestionDto.builder()
            .subject("제목 입니다.")
            .content("내용 입니다.").build());

        given(questionService.getList()).willReturn(questionDtoList);

        mockMvc.perform(get(BASE_URL + "/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("question_list"))
            .andExpect(model().attributeExists("questionList"));
    }
    @Test
    @DisplayName("질문 Detail API")
    void questionListDetail() throws Exception {
        Long path = 1L;
        QuestionDto question = QuestionDto.builder()
            .id(path)
            .subject("제목 입니다.")
            .content("내용 입니다.")
            .createDate(LocalDateTime.now())
            .answerList(new ArrayList<>())
            .build();

        given(questionService.getQuestionDto(path)).willReturn(question);

        mockMvc.perform(get(BASE_URL + "/detail/"+path))
            .andExpect(status().isOk())
            .andExpect(view().name("question_detail"))
            .andExpect(model().attributeExists("question"))
            .andExpect(model().attributeExists("answerDto"));
    }

    @Test
    @DisplayName("질문 생성 API")
    void questionCreate() throws Exception {

        mockMvc.perform(get(BASE_URL + "/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("question_form"))
            .andExpect(model().attributeExists("questionDto"));
    }

    @Test
    @DisplayName("질문 생성 확인")
    void questionCreatePost() throws Exception {
        mockMvc.perform(post(BASE_URL + "/create")
                .param("subject", "주제입니다.")
                .param("content", "내용입니다."))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/question/list"));
    }

    @Test
    @DisplayName("질문 생성 - 주제 & 내용 없음")
    void questionCreatePostNoSubjectContent() throws Exception {
        mockMvc.perform(post(BASE_URL + "/create")
                .param("subject", "주제입니다."))
            .andExpect(status().isOk())
            .andExpect(model().hasErrors());
        mockMvc.perform(post(BASE_URL + "/create")
                .param("content", "내용입니다."))
            .andExpect(status().isOk())
            .andExpect(model().hasErrors());
    }


}