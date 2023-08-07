package gptconnector.model;

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
public class KarloImageRequest {
    //참고 문서 : https://developers.kakao.com/docs/latest/ko/karlo/rest-api
    //이미지를 묘사하는 제시어, 영문만 지원(최대: 256자)
    private String prompt;
    //이미지 생성 시 제외할 요소를 묘사하는 부정 제시어, 영문만 지원(최대: 256자)
    private String negative_prompt;
    //원본 이미지 파일을 Base64 인코딩한 값
    private String image;
    //확대할 이미지 파일을 Base64 인코딩한 값, 이미지 인코딩 및 디코딩 참고
    //이미지 크기는 가로세로 최대 1024 픽셀 이하여야 함
    //webp, png, jpeg, heic 형식의 이미지 파일 지원
    //(최대: 8개)
    private String[] images;
    //이미지 가로 크기, 8의 배수여야 함(단위: 픽셀, 기본값: 512, 최소: 384, 최대 640)
    private int width;
    //이미지 세로 크기, 8의 배수여야 함(단위: 픽셀, 기본값: 512, 최소: 384, 최대 640)
    private int height;
    //이미지 크기 확대 여부(true: 확대 false: 확대하지 않음)
    private boolean upscale;
    //확대 배율, 2 또는 4 중 하나(기본값: 2) 비고: scale 값을 4로 지정하더라도 가로세로 최대 2048 픽셀 크기까지만 확대 가능
    private int scale;
    //이미지 파일 형식, 다음 중 하나 webp jpeg png (기본값: webp)이미지 포맷, jpg 또는 png 중 하나(기본값: jpg)
    @Builder.Default
    private String image_format = "png";
    //이미지 저장 품질(기본값: 70, 최소: 1, 최대: 100)
    private int image_quality;
    //생성할 이미지 수(기본값: 1, 최소: 1, 최대 8)
    private int samples;
    //응답의 이미지 파일 반환 형식, 다음 중 하나 base64_string: 이미지 파일을 Base64 인코딩한 값 url: 이미지 파일 URL(기본값: url)
    private String return_type;
    //이미지 생성 과정의 노이즈 제거 단계 수(기본값: 25, 최소: 10, 최대 100)
    private int prior_num_inference_steps;
    //이미지 생성 과정의 노이즈 제거 척도(기본값: 5.0, 최소: 1.0, 최대: 20.0)
    private double prior_guidance_scale;
    //디코더를 통한 노이즈 제거 단계 수(기본값: 50, 최소: 10, 최대: 100)
    private int num_inference_steps;
    //디코더를 통한 노이즈 제거 척도(기본값: 5.0, 최소: 1.0, 최대: 20.0)
    private double guidance_scale;
    //디코더를 통한 노이즈 제거 단계에서 사용할 스케줄러(Scheduler)
    //다음 중 하나 decoder_ddim_v_prediction
    //decoder_ddpm_v_prediction(기본값: decoder_ddim_v_prediction)
    private String scheduler;
    //각 이미지 생성 작업에 사용할 시드(Seed) 값 생성할 이미지 수와 같은 길이의 배열이어야 함
    //0 이상 4,294,967,295 이하 숫자로 구성
    //파라미터 미사용 시 무작위(Random) 시드 값으로 이미지 생성
    //(기본값: null)
    //비고: seed를 포함한 모든 파라미터가 동일할 경우, 항상 같은 이미지 생성
    private int[] seed;
    //생성할 이미지에 대한 NSFW 검사하기 수행 여부
    //true: 수행
    //false 수행하지 않음
    //(기본값: false)
    private boolean nsfw_checker;

    public Map<String, Object> toMap() {
        Map<String, Object> requestMap = new HashMap<>();

        if (prompt != null) requestMap.put("prompt", prompt);
        if (negative_prompt != null) requestMap.put("negative_prompt", negative_prompt);
        if (image != null) requestMap.put("image", image);
        if (images != null) requestMap.put("images", images);
        if (width > 0) requestMap.put("width", width);
        if (height > 0) requestMap.put("height", height);
        requestMap.put("upscale", upscale);
        if (scale > 0) requestMap.put("scale", scale);
        if (image_format != null) requestMap.put("image_format", image_format);
        if (image_quality > 0) requestMap.put("image_quality", image_quality);
        if (samples > 0) requestMap.put("samples", samples);
        if (return_type != null) requestMap.put("return_type", return_type);
        if (prior_num_inference_steps > 0) requestMap.put("prior_num_inference_steps", prior_num_inference_steps);
        if (prior_guidance_scale > 0.0) requestMap.put("prior_guidance_scale", prior_guidance_scale);
        if (num_inference_steps > 0) requestMap.put("num_inference_steps", num_inference_steps);
        if (guidance_scale > 0.0) requestMap.put("guidance_scale", guidance_scale);
        if (scheduler != null) requestMap.put("scheduler", scheduler);
        if (seed != null) requestMap.put("seed", seed);
        requestMap.put("nsfw_checker", nsfw_checker);

        return requestMap;
    }
}

