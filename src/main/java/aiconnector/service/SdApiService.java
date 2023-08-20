package aiconnector.service;

import aiconnector.model.sdapi.SdApiRequest;
import aiconnector.model.sdapi.SdApiResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Service
public class SdApiService {
    @Value("${jwt.secret.key}")
    private String JWT_SECRET_KEY;
    @Value("${stable-diffusion.api.key}")
    private String STABLE_DIFFUSION_API_KEY;

    public static String convertMapToString(Map<String, Object> inputMap) {
        StringBuilder sb = new StringBuilder("{\n");

        for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                sb.append("  \"").append(key).append("\": \"").append(value).append("\",\n");
            } else {
                sb.append("  \"").append(key).append("\": ").append(value).append(",\n");
            }
        }

        sb.setLength(sb.length() - 2); // Removing the last comma and newline
        sb.append("\n}");

        return sb.toString();
    }

    public Claims getClaims(HttpServletRequest request) {
        Key secretKey = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        Claims claim = Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(request.getHeader("authorization")).getBody();
        return claim;
    }

    public void dreambooth(HttpServletRequest request, SdApiRequest sdApiRequest) throws Exception {
        String apiUrl = "";
        if(sdApiRequest.getInit_image() != null){
            apiUrl = "https://stablediffusionapi.com/api/v4/dreambooth/img2img";
        } else {
            apiUrl = "https://stablediffusionapi.com/api/v4/dreambooth";
        }
        SdApiRequest requestObject = sdApiRequest;
        requestObject.setKey(STABLE_DIFFUSION_API_KEY);
        requestStableDiffusionApi(apiUrl, requestObject);
    }

    public void requestStableDiffusionApi(String apiUrl, SdApiRequest requestObject) {
        WebClient webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        System.out.println("requestObject: " + requestObject.toString());
        System.out.println("requestObjectMap: " + requestObject.toMap().toString());

        Mono<SdApiResponse> responseMono = webClient.post()
                .body(BodyInserters.fromValue(requestObject.toMap()))
                .retrieve()
                .bodyToMono(SdApiResponse.class); // ApiResponse is a class to represent the response

        responseMono.subscribe(
                response -> {
                    // Handle the response here
                    System.out.println("Response: " + response);
                },
                error -> {
                    // Handle error here
                    System.err.println("Error: " + error.getMessage());
                }
        );
    }

    public Response OkHttpClientRequestStableDiffusionApi(String apiUrl, SdApiRequest requestObject) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
        System.out.println("requestObject: " + convertMapToString(requestObject.toMap()));
        RequestBody body = RequestBody.create(mediaType, convertMapToString(requestObject.toMap()));
        Request request = new Request.Builder()
                .url(apiUrl)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        // Print the response body
        String responseBody = response.body().string();
        System.out.println("Response Body:");
        System.out.println(responseBody);

        return response;
    }
}