package com.lucas.springpage.dto;

import com.lucas.springpage.domain.Answer;
import com.lucas.springpage.domain.SiteUser;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
public class QuestionDto {

    private Long id;
    @NotEmpty(message = "제목은 필수사항 입니다.")
    @Size(max = 200)
    private String subject;


    @NotEmpty(message = "내용을 필수 사항입니다.")
    private String content;

    private LocalDateTime createDate;
    private List<Answer> answerList;

    private SiteUser author;
}
