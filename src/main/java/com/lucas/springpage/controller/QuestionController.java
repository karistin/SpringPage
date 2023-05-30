package com.lucas.springpage.controller;

import com.lucas.springpage.domain.Question;
import com.lucas.springpage.dto.AnswerDto;
import com.lucas.springpage.dto.QuestionDto;
import com.lucas.springpage.service.QuestionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

//    전체 리스트 불러오기
    @RequestMapping("/list")
    public String list(Model model) {
        List<QuestionDto> questionList = questionService.getList();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

//    해당 id로 이동
    @RequestMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        model.addAttribute("answerDto", AnswerDto.builder().build());
        return "question_detail";
    }

//    질문 탬플릿
    @GetMapping("/create")
    public String questionCreate(Model model) {
        QuestionDto questionDto = QuestionDto.builder().build();
        model.addAttribute("questionDto", questionDto);
        return "question_form";
    }

//    질문 생성
    @PostMapping("/create")
    public String questionCreate(@Valid @ModelAttribute QuestionDto QuestionDto, BindingResult
        bindingResult) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        questionService.create(QuestionDto);
        return "redirect:/question/list";
    }
}
