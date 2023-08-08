package aiconnector.model;

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
public class GptTextRequest extends GptRequest{
    // 참고문서 : https://platform.openai.com/docs/api-reference/completions/create
    private String model;
    private String suffix;
    @Builder.Default
    private int max_tokens = 500;
    private double temperature; //왠만하면 조작 X
    private double top_p; //왠만하면 조작 X
    @Builder.Default
    private boolean stream = false; //
    private String stop;
    private double presence_penalty; //-2.0에서 2.0 사이의 숫자입니다. 양수 값은 지금까지 텍스트에 나타나는지 여부에 따라 새 토큰에 페널티를 주어 모델이 새 주제에 대해 이야기할 가능성을 높입니다.
    private double frequency_penalty; //-2.0에서 2.0 사이의 숫자입니다. 양수 값은 지금까지 텍스트의 기존 빈도를 기반으로 새 토큰에 페널티를 주어 모델이 동일한 줄을 그대로 반복할 가능성을 줄입니다.
    private String logit_bias; //왠만하면 조작 X

    @Builder.Default
    private List<GptMessage> messages = new ArrayList<>();

    public void addMessage(GptMessage gptMessage) {
        this.messages.add(gptMessage);
    }
}
