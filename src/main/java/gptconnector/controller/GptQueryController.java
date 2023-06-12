package gptconnector.controller;

import gptconnector.model.Response;
import gptconnector.service.GptQueryService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Api(tags = "GptQueryController")
@Tag(name = "GptQueryController", description = "챗GPT 프롬프트 컨트롤러")
@RestController
public class GptQueryController {

    private static final Logger logger = LoggerFactory.getLogger(GptQueryController.class);
    private final GptQueryService gptQueryService;

    @Autowired
    public GptQueryController(GptQueryService gptQueryService){
        this.gptQueryService = gptQueryService;
    }

    @GetMapping("/query")
    @Operation(summary="", description="")
    public ResponseEntity<Response> query(HttpServletRequest request, @RequestParam String prompt) throws Exception {
        String answer = gptQueryService.query(request, prompt);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("answer", answer);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("요청 성공")
                .result(resultMap)
                .build();

        return ResponseEntity.ok().body(response);
    }
}