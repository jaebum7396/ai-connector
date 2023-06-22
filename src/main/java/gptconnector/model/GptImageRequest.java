package gptconnector.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GptImageRequest extends GptRequest {
    // 참고문서 : https://platform.openai.com/docs/api-reference/images/create
    private String image;
    private String mask;
    @Builder.Default
    private String size = "512x512"; //The size of the generated images. Must be one of 256x256, 512x512, or 1024x1024
    private String response_format;
    private String user;
}
