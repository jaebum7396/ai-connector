package aiconnector.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SdApiRequest {
    private String key;
    private String prompt;
    private String negative_prompt;
    @Builder.Default
    private String model_id = "majicmix-realistic-9000";
    @Builder.Default
    private String width = "512";
    @Builder.Default
    private String height = "512";
    @Builder.Default
    private String samples = "1";
    @Builder.Default
    private String num_inference_steps="30";
    private YesOrNo safety_checker;
    private YesOrNo enhance_prompt;
    private String seed;
    @Builder.Default
    private double guidance_scale = 7.5;
    private YesOrNo multi_lingual;
    private YesOrNo panorama;
    private YesOrNo self_attention;
    private YesOrNo upscale;
    private String embeddings_model;
    private String lora_model;
    private YesOrNo tomesd;
    private String clip_skip;
    private YesOrNo use_karras_sigmas;
    private String vae;
    private String lora_strength;
    private Scheduler scheduler;
    private String webhook;
    private String track_id;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (key != null) map.put("key", key);
        if (model_id != null) map.put("model_id", model_id);
        if (prompt != null) map.put("prompt", prompt);
        if (negative_prompt != null) map.put("negative_prompt", negative_prompt);
        if (width != null) map.put("width", width);
        if (height != null) map.put("height", height);
        if (samples != null) map.put("samples", samples);
        if (num_inference_steps != null) map.put("num_inference_steps", num_inference_steps);
        if (safety_checker != null) map.put("safety_checker", safety_checker.name());
        if (enhance_prompt != null) map.put("enhance_prompt", enhance_prompt.name());
        if (seed != null) map.put("seed", seed);
        if (guidance_scale != 0) map.put("guidance_scale", guidance_scale);
        if (multi_lingual != null) map.put("multi_lingual", multi_lingual.name());
        if (panorama != null) map.put("panorama", panorama.name());
        if (self_attention != null) map.put("self_attention", self_attention.name());
        if (upscale != null) map.put("upscale", upscale.name());
        if (embeddings_model != null) map.put("embeddings_model", embeddings_model);
        if (lora_model != null) map.put("lora_model", lora_model);
        if (tomesd != null) map.put("tomesd", tomesd.name());
        if (clip_skip != null) map.put("clip_skip", clip_skip);
        if (use_karras_sigmas != null) map.put("use_karras_sigmas", use_karras_sigmas.name());
        if (vae != null) map.put("vae", vae);
        if (lora_strength != null) map.put("lora_strength", lora_strength);
        if (scheduler != null) map.put("scheduler", scheduler.name());
        if (webhook != null) map.put("webhook", webhook);
        if (track_id != null) map.put("track_id", track_id);

        return map;
    }
}

