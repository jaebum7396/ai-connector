package aiconnector.model.sdapi;

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
    private String key; //요청 승인을 위해 사용되는 API 키
    private String prompt;
    private String negative_prompt;
    @Builder.Default
    private String model_id = "majicmix-realistic-9000"; //사용할 모델의 ID. 공개 모델 또는 귀하의 훈련된 모델일 수 있습니다.
    @Builder.Default
    private String width = "512";
    @Builder.Default
    private String height = "512";
    @Builder.Default
    private String samples = "1"; //응답에 반환될 이미지 수. 최대값은 4입니다.
    @Builder.Default
    private String num_inference_steps="30"; //노이즈 제거 단계 수 (최소: 1; 최대: 50)
    @Builder.Default
    private String steps="30"; //노이즈 제거 단계 수 (최소: 1; 최대: 50)
    @Builder.Default
    private YesOrNo safety_checker = YesOrNo.yes; //18금 이미지를 확인하는 체커. 이러한 이미지가 감지되면 빈 이미지로 대체됩니다.
    @Builder.Default
    private YesOrNo enhance_prompt = YesOrNo.yes; //더 나은 결과를 위한 프롬프트 강화; 기본값: yes, 옵션: yes/no
    @Builder.Default
    private double guidance_scale = 7.5; //프롬프트 강화 가중치
    @Builder.Default
    private Scheduler scheduler = Scheduler.UniPCMultistepScheduler; //스케줄러를 설정하는 데 사용합니다.
    @Builder.Default
    private YesOrNo tomesd = YesOrNo.yes; //매우 빠른 결과를 제공합니다, 기본값: yes, 옵션: yes/no
    @Builder.Default
    private YesOrNo use_karras_sigmas = YesOrNo.yes; //이미지 생성에 keras 시그마 사용. 멋진 결과 제공, 기본값: yes, 옵션: yes/no
    private double strength; //init 이미지 사용 시 프롬프트 강도. 1.0은 init 이미지의 정보를 완전히 소실시킵니다.
    private String init_image; //초기 이미지 링크
    private YesOrNo multi_lingual;
    private YesOrNo panorama;
    private YesOrNo self_attention;
    private YesOrNo upscale;
    private String embeddings_model;
    private String clip_skip;
    private String vae; //사용자 정의 VAE를 이미지 생성에 사용, 기본값: null
    private String lora_model; //다중 lora를 지원하며, 쉼표로 구분된 값 전달 가능. 예: contrast-fix,yae-miko-genshin
    private String lora_strength; //사용 중인 lora 모델의 강도. 다중 lora를 사용하는 경우 쉼표로 구분하여 각 값을 전달합니다.
    private String seed; //결과를 재현하는 데 사용되는 시드. 동일한 시드는 동일한 이미지를 다시 반환합니다. 무작위 숫자를 얻으려면 null을 전달하세요.
    private String webhook; //이미지 생성이 완료되면 POST API 호출을 받기 위한 URL을 설정합니다.
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
        if (steps != null) map.put("steps", steps);
        if (init_image != null) map.put("init_image", init_image);
        if (safety_checker != null) map.put("safety_checker", safety_checker.name());
        if (enhance_prompt != null) map.put("enhance_prompt", enhance_prompt.name());
        if (guidance_scale != 0) map.put("guidance_scale", guidance_scale);
        if (multi_lingual != null) map.put("multi_lingual", multi_lingual.name());
        if (panorama != null) map.put("panorama", panorama.name());
        if (self_attention != null) map.put("self_attention", self_attention.name());
        if (upscale != null) map.put("upscale", upscale.name());
        if (embeddings_model != null) map.put("embeddings_model", embeddings_model);
        if (tomesd != null) map.put("tomesd", tomesd.name());
        if (clip_skip != null) map.put("clip_skip", clip_skip);
        if (use_karras_sigmas != null) map.put("use_karras_sigmas", use_karras_sigmas.name());
        if (vae != null) map.put("vae", vae);
        if (lora_strength != null) map.put("lora_strength", lora_strength);
        if (lora_model != null) map.put("lora_model", lora_model);
        if (strength != 0) map.put("strength", strength);
        if (seed != null) map.put("seed", seed);
        if (scheduler != null) map.put("scheduler", scheduler.name());
        if (webhook != null) map.put("webhook", webhook);
        if (track_id != null) map.put("track_id", track_id);

        return map;
    }
}

