package aiconnector.model.sdapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SdApiResponse {
    private String status;
    private double generationTime;
    private int id;
    private List<String> output;
    private Meta meta;

    @Data
    public static class Meta {
        private String prompt;
        @JsonProperty("model_id")
        private String modelId;
        private String scheduler;
        private String safetychecker;
        @JsonProperty("negative_prompt")
        private String negativePrompt;
        private int W;
        private int H;
        @JsonProperty("guidance_scale")
        private double guidanceScale;
        @JsonProperty("init_image")
        private String initImage;
        private int steps;
        @JsonProperty("n_samples")
        private int nSamples;
        private double strength;
        @JsonProperty("multi_lingual")
        private String multiLingual;
        @JsonProperty("full_url")
        private String fullUrl;
        private String upscale;
        private long seed;
        private String outdir;
        @JsonProperty("file_prefix")
        private String filePrefix;
    }
}

