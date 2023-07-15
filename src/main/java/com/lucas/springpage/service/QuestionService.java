package com.lucas.springpage.service;

import com.lucas.springpage.domain.Question;
import com.lucas.springpage.dto.QuestionDto;
import com.lucas.springpage.exception.DataNotFoundException;
import com.lucas.springpage.exception.ErrorCode;
import com.lucas.springpage.repository.QuestionRepository;
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
    public void create(QuestionDto questionDto) {
        Question question = new Question();
        question.setSubject(questionDto.getSubject());
        question.setContent(questionDto.getContent());
        question.setCreateDate(LocalDateTime.now());
        questionRepository.save(question);
    }

    public Page<Question> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAll(pageable);
    }

}
