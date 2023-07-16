package com.lucas.springpage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.springpage.domain.Question;
import com.lucas.springpage.domain.SiteUser;
import com.lucas.springpage.dto.AnswerDto;
import com.lucas.springpage.dto.QuestionDto;
import com.lucas.springpage.service.QuestionService;
import com.lucas.springpage.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//https://devlog-wjdrbs96.tistory.com/414
@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
//    전체 리스트 불러오기
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0")int page) {
        Page<Question> questionDtoPage = questionService.getList(page);
        model.addAttribute("paging", questionDtoPage);
        return "question_list";
    }

//    해당 id로 이동
    @RequestMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        QuestionDto question = questionService.getQuestionDto(id);
        model.addAttribute("question", question);
        model.addAttribute("answerDto", AnswerDto.builder().build());
        return "question_detail";
    }

//    질문 탬플릿
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(Model model) {
        QuestionDto questionDto = QuestionDto.builder().build();
        model.addAttribute("questionDto", questionDto);
        return "question_form";
    }

//    질문 생성
    // 로그인이 되어야 접근가능
    // 로그아웃의 경우 자동으로 로그인 페이지 접근
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid @ModelAttribute QuestionDto QuestionDto, BindingResult
        bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser user = userService.getUser(principal.getName());
        questionService.create(QuestionDto, user);
        return "redirect:/question/list";
    }
}
