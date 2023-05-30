package com.lucas.springpage.service;

import com.lucas.springpage.domain.Answer;
import com.lucas.springpage.domain.Question;
import com.lucas.springpage.dto.QuestionDto;
import com.lucas.springpage.repository.AnswerRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void create(Question question, String content) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);

        answerRepository.save(answer);
    }
}
