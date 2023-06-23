package gptconnector.controller;

import gptconnector.model.GptMessage;
import gptconnector.model.Response;
import gptconnector.service.GptQueryService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "GptQueryController")
@Tag(name = "GptQueryController", description = "챗GPT query 컨트롤러")
@RestController
public class GptQueryController {

    private static final Logger logger = LoggerFactory.getLogger(GptQueryController.class);
    private final GptQueryService gptQueryService;

    @Autowired
    public GptQueryController(GptQueryService gptQueryService){
        this.gptQueryService = gptQueryService;
    }

    @PostMapping("/models")
    @Operation(summary="", description="")
    public ResponseEntity<Response> models(HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap = gptQueryService.models(request);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("요청 성공")
                .result(resultMap)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/query")
    @Operation(summary="", description="")
    public ResponseEntity<Response> query(HttpServletRequest request, @RequestParam String prompt, @RequestBody(required = false) List<GptMessage> prevMessages) throws Exception {
        List<GptMessage> messages = gptQueryService.query(request, prompt, prevMessages);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("messages", messages);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("요청 성공")
                .result(resultMap)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/images/generations")
    @Operation(summary="", description="")
    public ResponseEntity<Response> imageGenerations(HttpServletRequest request, @RequestParam String prompt) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap = gptQueryService.generations(request, prompt);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("요청 성공")
                .result(resultMap)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/images/edits")
    @Operation(summary="", description="")
    public ResponseEntity<Response> imageEdits(@RequestParam String prompt, @RequestPart("file") MultipartFile file) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap = gptQueryService.imageEdits(prompt, file);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("요청 성공")
                .result(resultMap)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/images/variations")
    @Operation(summary="", description="")
    public ResponseEntity<Response> imageVariations(HttpServletRequest request, @RequestParam String prompt, @RequestBody(required = false) List<GptMessage> prevMessages) throws Exception {
        List<GptMessage> messages = gptQueryService.query(request, prompt, prevMessages);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("messages", messages);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("요청 성공")
                .result(resultMap)
                .build();
        return ResponseEntity.ok().body(response);
    }
}