package aiconnector.model.gpt;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GptMessage {
    private String role;
    private String content;
    private String name;
}
