package gptconnector.service;

import gptconnector.model.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class KarloService {
    @Value("${jwt.secret.key}")
    private String JWT_SECRET_KEY;

    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    @Value("${kakao.api.key}")
    private String KAKAO_API_KEY;

    private final OkHttpClient client = new OkHttpClient();

    public Claims getClaims(HttpServletRequest request) {
        Key secretKey = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        Claims claim = Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(request.getHeader("authorization")).getBody();
        return claim;
    }

    public HashMap<String,Object> generations(HttpServletRequest request, String prompt, String negative_prompt) throws Exception {
        String apiUrl = "https://api.kakaobrain.com/v2/inference/karlo/t2i";
        KarloImageRequest karloImageRequest = KarloImageRequest.builder()
                .prompt(prompt)
                .negative_prompt(negative_prompt)
                .build();
        JSONObject resp = requestKarlo(apiUrl, karloImageRequest);
        System.out.println("resp : " + resp);

        HashMap<String, Object> resultMap = new HashMap<>();
        return resultMap;
    }

    public JSONObject requestKarlo(String apiUrl, KarloImageRequest karloImageRequest) {
        WebClient client = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + KAKAO_API_KEY)
                .build();

        ClientResponse clientResponse = client.post()
                .uri(apiUrl)
                .body(BodyInserters.fromValue(karloImageRequest))
                .exchange()
                .block();

        // Get response body as string
        String responseBody = clientResponse.bodyToMono(String.class).block();

        // Convert response body to JSONObject
        JSONObject jsonResponse = new JSONObject(responseBody);
        return jsonResponse;
    }
}