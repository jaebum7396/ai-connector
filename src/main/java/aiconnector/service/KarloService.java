package aiconnector.service;

import aiconnector.model.karlo.KarloImageRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Service
public class KarloService {
    @Value("${jwt.secret.key}")
    private String JWT_SECRET_KEY;

    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    @Value("${kakao.api.key}")
    private String KAKAO_API_KEY;

    public static String getImageUrlFromResponse(Map<String, Object> responseMap) {
        if (responseMap.containsKey("images")) {
            Object imagesObj = responseMap.get("images");
            if (imagesObj instanceof Iterable) {
                Iterable<?> imagesList = (Iterable<?>) imagesObj;
                for (Object imageObj : imagesList) {
                    if (imageObj instanceof Map) {
                        Map<?, ?> imageMap = (Map<?, ?>) imageObj;
                        if (imageMap.containsKey("image")) {
                            return (String) imageMap.get("image");
                        }
                    }
                }
            }
        }
        return null;
    }

    // MultipartFile을 Base64로 인코딩하는 메서드
    private String encodeMultipartFile(MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        return Base64Utils.encodeToString(fileBytes);
    }

    public Claims getClaims(HttpServletRequest request) {
        Key secretKey = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        Claims claim = Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(request.getHeader("authorization")).getBody();
        return claim;
    }

    public HashMap<String,Object> generations(HttpServletRequest request, String prompt, String negative_prompt) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();
        String apiUrl = "https://api.kakaobrain.com/v2/inference/karlo/t2i";
        KarloImageRequest karloImageRequest = KarloImageRequest.builder()
                .prompt(prompt)
                .negative_prompt(negative_prompt)
                .build();
        JSONObject resp = requestKarlo(apiUrl, karloImageRequest);
        System.out.println("resp : " + resp);

        String imageUrl = getImageUrlFromResponse(resp.toMap());

        // 이미지 URL이 있다면 결과 맵에 추가
        if (imageUrl != null) {
            resultMap.put("image_url", imageUrl);
        }

        return resultMap;
    }

    public HashMap<String,Object> edits(HttpServletRequest request, String prompt, String negative_prompt, MultipartFile file) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();
        String apiUrl = "https://api.kakaobrain.com/v2/inference/karlo/variations";

        // MultipartFile을 Base64로 인코딩
        String encodedFile = encodeMultipartFile(file);

        KarloImageRequest karloImageRequest = KarloImageRequest.builder()
                .image(encodedFile)
                .prompt(prompt)
                .negative_prompt(negative_prompt)
                .build();

        JSONObject resp = requestKarlo(apiUrl, karloImageRequest);
        System.out.println("resp : " + resp);

        String imageUrl = getImageUrlFromResponse(resp.toMap());

        // 이미지 URL이 있다면 결과 맵에 추가
        if (imageUrl != null) {
            resultMap.put("image_url", imageUrl);
        }

        return resultMap;
    }

    public JSONObject requestKarlo(String apiUrl, KarloImageRequest karloImageRequest) {
        System.out.println("karloImageRequest : " + karloImageRequest);
        Map<String,Object> karloImageRequestMap = karloImageRequest.toMap();

        for (Map.Entry<String, Object> entry : karloImageRequestMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + value);
        }

        WebClient client = WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + KAKAO_API_KEY)
                .build();

        ClientResponse clientResponse = client.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(karloImageRequestMap))
                .exchange()
                .block();

        // Get response body as string
        String responseBody = clientResponse.bodyToMono(String.class).block();

        // Convert response body to JSONObject
        JSONObject jsonResponse = new JSONObject(responseBody);
        return jsonResponse;
    }
}