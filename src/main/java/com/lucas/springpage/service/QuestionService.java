package com.lucas.springpage.service;

import com.lucas.springpage.domain.Question;
import com.lucas.springpage.dto.QuestionDto;
import com.lucas.springpage.exception.DataNotFoundException;
import com.lucas.springpage.exception.ErrorCode;
import com.lucas.springpage.repository.QuestionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<QuestionDto> getList() {
        return questionRepository.findAll().stream().map(
            this::questionToDto
        ).collect(Collectors.toList());
    }

    private QuestionDto questionToDto(Question question) {
        return QuestionDto.builder()
            .id(question.getId())
            .subject(question.getSubject())
            .content(question.getContent())
            .answerList(question.getAnswerList())
            .createDate(question.getCreateDate()).build();
    }
    public Question getQuestion(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(
            () -> new DataNotFoundException(ErrorCode.NOT_FOUND, "해당 question ID가 없습니다.")
        );
        return question;
    }

    public void create(QuestionDto questionDto) {
        Question question = new Question();
        question.setSubject(questionDto.getSubject());
        question.setContent(questionDto.getContent());
        question.setCreateDate(LocalDateTime.now());
        questionRepository.save(question);
    }
}
