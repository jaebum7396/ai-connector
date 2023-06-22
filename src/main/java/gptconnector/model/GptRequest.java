package gptconnector.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GptRequest {
    // 참고문서 : https://platform.openai.com/docs/api-reference/completions/create
    private String prompt;
    @Builder.Default
    private int n = 1; //각 프롬프트에 대해 생성할 완료 수입니다.
}
