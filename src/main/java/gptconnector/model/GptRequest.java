package gptconnector.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class GptRequest {
    // 참고문서 : https://platform.openai.com/docs/api-reference/completions/create
    private String model;
    private String prompt;
    private String suffix;
    private int max_tokens;
    private double temperature; //왠만하면 조작 X
    private double top_p; //왠만하면 조작 X
    private int n; //각 프롬프트에 대해 생성할 완료 수입니다.
    private int logprobs; //가장 가능성이 높은 토큰과 선택한 토큰에 대한 로그 확률을 포함합니다. 최대값은 5
    @Builder.Default
    private boolean stream = false; //
    @Builder.Default
    private boolean echo = false;
    private String stop;
    private double presence_penalty; //-2.0에서 2.0 사이의 숫자입니다. 양수 값은 지금까지 텍스트에 나타나는지 여부에 따라 새 토큰에 페널티를 주어 모델이 새 주제에 대해 이야기할 가능성을 높입니다.
    private double frequency_penalty; //-2.0에서 2.0 사이의 숫자입니다. 양수 값은 지금까지 텍스트의 기존 빈도를 기반으로 새 토큰에 페널티를 주어 모델이 동일한 줄을 그대로 반복할 가능성을 줄입니다.
    private int best_of; //
    private String logit_bias; //왠만하면 조작 X

    @Builder.Default
    private List<GptMessage> messages = new ArrayList<>();

    public void addMessage(GptMessage gptMessage) {
        this.messages.add(gptMessage);
    }
}
