package aiconnector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class KarloImage {
    //이미지 ID
    String id;
    //이미지 생성 시 사용된 시드 값
    //재요청을 통해 같은 콘텐츠의 이미지를 다시 생성하는 데 사용
    int seed;
    //이미지 파일
    //return_type 파라미터 값에 따라 이미지 파일을 Base64 인코딩한 값, 또는 이미지 파일 URL 제공
    //이미지 파일 URL은 응답 시각으로부터 10분간 유효
    String image;
    //이미지의 NSFW 콘텐츠 포함 여부
    //true: 포함
    //false: 포함하지 않음
    //비고: 미사용 시 null 반환
    boolean nsfw_content_detected;
    //이미지의 NSFW 콘텐츠 포함 확률
    //비고: 미사용 시 null 반환
    double nsfw_score;
}
