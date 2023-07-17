package com.lucas.springpage.controller;

import com.lucas.springpage.domain.Answer;
import com.lucas.springpage.domain.Question;
import com.lucas.springpage.domain.SiteUser;
import com.lucas.springpage.dto.AnswerDto;
import com.lucas.springpage.dto.QuestionDto;
import com.lucas.springpage.service.AnswerService;
import com.lucas.springpage.service.QuestionService;
import com.lucas.springpage.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    private final UserService userService;

    // 답변 등록
    // questionID
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{questionID}")
    public String createAnswer(Model model, @PathVariable("questionID") Long questionID,
        @Valid @ModelAttribute AnswerDto answerDto, BindingResult bindingResult
        , Principal principal, RedirectAttributes redirectAttributes) {
        Question question = questionService.getQuestion(questionID);
        SiteUser user = userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        Answer answer = answerService.create(question, answerDto.getContent(), user);
        redirectAttributes.addAttribute("questionId", question.getId());
        redirectAttributes.addAttribute("answerId", answer.getId());
        return "redirect:/question/detail/{questionId}  #answer_{answerId}";
    }

    // 답변 가져와서 수정 페이지로
    // answerId
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{answerId}")
    public String answerModify(AnswerDto answerDto, @PathVariable("answerId") Long answerId,
        Principal principal
        , Model model) {
        Answer answer = answerService.getAnswer(answerId);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerDto.setContent(answer.getContent());
        model.addAttribute("answerDto", answerDto);
        return "answer_form";
    }

    // 답변 수정하기
    // answer ID
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{answerId}")
    public String answerModify(@Valid AnswerDto answerDto, Principal principal,
        BindingResult bindingResult, @PathVariable("answerId") Long answerId
        , RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }
        Answer answer = answerService.getAnswer(answerId);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerService.modify(answer, answerDto.getContent());
        redirectAttributes.addAttribute("questionId", answer.getQuestion().getId());
        redirectAttributes.addAttribute("answerId", answer.getId());
        return "redirect:/question/detail/{questionId}#answer_{answerId}";
    }

    // 삭제하기
    // answerId
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{answerId}")
    public String answerModify(@PathVariable("answerId") Long answerId,
        Principal principal, RedirectAttributes redirectAttributes) {
        Answer answer = this.answerService.getAnswer(answerId);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(answer);
        redirectAttributes.addAttribute("questionId", answer.getQuestion().getId());
        return "redirect:/question/detail/{questionId}";
    }

    // 추천하기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{answerId}")
    public String answerVote(Principal principal, @PathVariable("answerId") Long answerId,
        RedirectAttributes redirectAttributes) {
        Answer answer = answerService.getAnswer(answerId);
        SiteUser user = userService.getUser(principal.getName());
        answerService.vote(answer, user);
        redirectAttributes.addAttribute("questionId", answer.getQuestion().getId());
        redirectAttributes.addAttribute("answerId", answer.getId());
        return "redirect:/question/detail/{questionId}#answer_{answerId}";
    }
}
