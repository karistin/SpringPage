package com.lucas.springpage.dto;

import com.lucas.springpage.domain.Question;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class AnswerDto {

    @NotEmpty(message = "내용을 필수 사항입니다.")
    private String content;

}
