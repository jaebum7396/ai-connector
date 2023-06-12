package gptconnector.service;

import gptconnector.model.GptMessage;
import gptconnector.model.GptRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;

@Service
public class GptQueryService {
    @Value("${jwt.secret.key}")
    private String JWT_SECRET_KEY;

    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    private final OkHttpClient client = new OkHttpClient();

    public Claims getClaims(HttpServletRequest request) {
        Key secretKey = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        Claims claim = Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(request.getHeader("authorization")).getBody();
        return claim;
    }

    public String query(HttpServletRequest request, String prompt){
        String apiUrl = "https://api.openai.com/v1/chat/completions";
        GptRequest gptRequest = GptRequest.builder()
                .model("gpt-3.5-turbo")
                .temperature(0.7)
                .build();
        gptRequest.addMessage(GptMessage.builder()
                .content(prompt)
                .role("user")
                .build());
        return requestGpt(apiUrl, gptRequest);
    }
    
    public String requestGpt(String apiUrl, GptRequest gptRequest) {
        WebClient client = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + OPENAI_API_KEY)
                .build();

        return client.post()
                .uri(apiUrl)
                .body(BodyInserters.fromValue(gptRequest))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}