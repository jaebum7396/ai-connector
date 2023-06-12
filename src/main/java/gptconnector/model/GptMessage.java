package gptconnector.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GptMessage {
    private String role;
    private String content;
    private String name;
}
