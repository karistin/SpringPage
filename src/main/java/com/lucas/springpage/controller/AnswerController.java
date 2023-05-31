package com.lucas.springpage.controller;

import com.lucas.springpage.domain.Question;
import com.lucas.springpage.dto.AnswerDto;
import com.lucas.springpage.dto.QuestionDto;
import com.lucas.springpage.service.AnswerService;
import com.lucas.springpage.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Long id,
        @Valid @ModelAttribute AnswerDto answerDto, BindingResult bindingResult) {
        Question question = questionService.getQuestion(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        answerService.create(question, answerDto.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }
}
