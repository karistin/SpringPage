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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//https://devlog-wjdrbs96.tistory.com/414
@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    //    전체 리스트 불러오기
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Question> questionDtoPage = questionService.getList(page);
        model.addAttribute("paging", questionDtoPage);
        return "question_list";
    }

    //    해당 id로 이동
    @RequestMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Question question = questionService.getQuestion(id);
        // 조회수 증가
        questionService.updateViewer(question);
        
        model.addAttribute("question", question);
        // 질문 및 답변 표시
        model.addAttribute("answerDto", AnswerDto.builder().build());
        // 답볍 등록을 위한 dto
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

    // question_detail 질문 수정 버튼시
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionDto questionDto, BindingResult bindingResult,
        Principal principal, @PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = questionService.getQuestion(id);

        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 업습니다.");
        }
        questionService.modify(question, questionDto.getSubject(), questionDto.getContent());
        redirectAttributes.addAttribute("id", id);
        return "redirect:/question/detail/{id}";
    }

    // question_detail 질문 수정 버튼시
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionDto questionDto, @PathVariable("id") Long id,
        Principal principal) {
        Question question = questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionDto.setSubject(question.getSubject());
        questionDto.setContent(question.getContent());
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Long id) {
        Question question = questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Long id
    , RedirectAttributes redirectAttributes) {
        Question question = questionService.getQuestion(id);
        SiteUser user = userService.getUser(principal.getName());
        questionService.vote(question, user);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/question/detail/{id}";
    }
}
