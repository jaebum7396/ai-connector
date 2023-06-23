package gptconnector.service;

import gptconnector.model.GptImageRequest;
import gptconnector.model.GptMessage;
import gptconnector.model.GptRequest;
import gptconnector.model.GptTextRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public HashMap<String,Object> models(HttpServletRequest request) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();

        String apiUrl = "https://api.openai.com/v1/models";
        GptTextRequest gptRequest = GptTextRequest.builder().build();
        JSONObject resp = requestGpt(apiUrl, HttpMethod.GET, gptRequest);
        JSONArray dataJSONArray = resp.getJSONArray("data");

        List<HashMap<String, Object>> dataArray = new ArrayList<>();
        for(int i =0; i< dataJSONArray.length(); i++) {
            HashMap<String, Object> dataMap = new HashMap<>();
            JSONObject data = dataJSONArray.getJSONObject(i);
            dataMap.put("url", data.getString("url"));
            dataArray.add(dataMap);
        }
        System.out.println(resp);
        resultMap.put("data", dataArray);
        return resultMap;
    }

    public List<GptMessage> query(HttpServletRequest request, String prompt, List<GptMessage> prevMessages) throws Exception {
        String apiUrl = "https://api.openai.com/v1/chat/completions";
        GptTextRequest gptRequest = GptTextRequest.builder()
                .model("gpt-3.5-turbo")
                .temperature(0.7)
                .build();
        if (prevMessages != null) {
            gptRequest.setMessages(prevMessages);
        }
        gptRequest.addMessage(GptMessage.builder()
                .role("user")
                .content(prompt)
                .build());
        JSONObject resp = requestGpt(apiUrl, HttpMethod.POST, gptRequest);
        JSONArray choicesArray = resp.getJSONArray("choices");
        JSONObject choice = choicesArray.getJSONObject(0);
        JSONObject message = choice.getJSONObject("message");

        GptMessage returnMessage = GptMessage.builder()
                .role(message.getString("role"))
                .content(message.getString("content"))
                .build();
        System.out.println(resp);
        System.out.println(returnMessage);
        prevMessages.add(returnMessage);
        return prevMessages;
    }

    public HashMap<String,Object> generations(HttpServletRequest request, String prompt) throws Exception {
        String apiUrl = "https://api.openai.com/v1/images/generations";
        GptImageRequest gptRequest = GptImageRequest.builder()
                .prompt(prompt)
                .n(3)
                .build();
        JSONObject resp = requestGpt(apiUrl, HttpMethod.POST, gptRequest);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("created", resp.get("created"));
        JSONArray dataJSONArray = resp.getJSONArray("data");
        List<HashMap<String, Object>> dataArray = new ArrayList<>();
        for(int i =0; i< dataJSONArray.length(); i++) {
            HashMap<String, Object> dataMap = new HashMap<>();
            JSONObject data = dataJSONArray.getJSONObject(i);
            dataMap.put("url", data.getString("url"));
            dataArray.add(dataMap);
        }
        resultMap.put("data", dataArray);
        return resultMap;
    }

    public HashMap<String,Object> imageEdits(String prompt, MultipartFile file) throws Exception {
        String apiUrl = "https://api.openai.com/v1/images/edits";
        GptImageRequest gptRequest = GptImageRequest.builder()
                .prompt(prompt)
                .n(1)
                .build();
        JSONObject resp = multiPartRequestGpt(apiUrl, gptRequest, file);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("created", resp.get("created"));
        JSONArray dataJSONArray = resp.getJSONArray("data");
        List<HashMap<String, Object>> dataArray = new ArrayList<>();
        for(int i =0; i< dataJSONArray.length(); i++) {
            HashMap<String, Object> dataMap = new HashMap<>();
            JSONObject data = dataJSONArray.getJSONObject(i);
            dataMap.put("url", data.getString("url"));
            dataArray.add(dataMap);
        }
        resultMap.put("data", dataArray);
        return resultMap;
    }

    public JSONObject multiPartRequestGpt(String apiUrl, GptImageRequest gptRequest, MultipartFile file) throws IOException {
        WebClient client = WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + OPENAI_API_KEY)
                .build();

        // Read the input stream once
        InputStream inputStream = file.getInputStream();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        File tempFile = File.createTempFile("image", null);
        file.transferTo(tempFile);
        FileSystemResource resource = new FileSystemResource(tempFile) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        body.add("image", resource);
        body.add("prompt", gptRequest.getPrompt());
        body.add("n", String.valueOf(gptRequest.getN()));
        body.add("size", gptRequest.getSize());

        ClientResponse clientResponse = client.post()
                .uri(apiUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .exchange()
                .block();

        // Get response body as string
        String responseBody = clientResponse.bodyToMono(String.class).block();

        // Convert response body to JSONObject
        JSONObject jsonResponse = new JSONObject(responseBody);
        System.out.println(jsonResponse);

        // Close the input stream
        inputStream.close();

        return jsonResponse;
    }

    public JSONObject requestGpt(String apiUrl, HttpMethod httpMethod, GptRequest gptRequest) {
        WebClient client = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + OPENAI_API_KEY)
                .build();

        ClientResponse clientResponse = null;
        if(httpMethod == HttpMethod.POST) {
            clientResponse = client
                .method(httpMethod)
                .uri(apiUrl)
                .body(BodyInserters.fromValue(gptRequest))
                .exchange()
                .block();
        }else if(httpMethod == HttpMethod.GET) {
            clientResponse = client
                .method(httpMethod)
                .uri(apiUrl)
                .exchange()
                .block();
        }

        // Get response body as string
        String responseBody = clientResponse.bodyToMono(String.class).block();

        // Convert response body to JSONObject
        JSONObject jsonResponse = new JSONObject(responseBody);
        System.out.println(jsonResponse);
        return jsonResponse;
    }
}