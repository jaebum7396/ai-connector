package aiconnector.controller;

import aiconnector.model.gpt.GptMessage;
import aiconnector.model.Response;
import aiconnector.service.GptService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GptController {
    private static final Logger logger = LoggerFactory.getLogger(GptController.class);
    private final GptService gptService;

    @Autowired
    public GptController(GptService gptService){
        this.gptService = gptService;
    }

    public ResponseEntity<Response> okResponsePackaging(Map<String, Object> result) {
        Response response = Response.builder()
                .message("요청 성공")
                .result(result).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/models")
    @Operation(summary="", description="")
    public ResponseEntity<Response> models(HttpServletRequest request) throws Exception {
        return okResponsePackaging(gptService.models(request));
    }

    @PostMapping("/query")
    @Operation(summary="", description="")
    public ResponseEntity<Response> query(HttpServletRequest request, @RequestParam String prompt, @RequestBody(required = false) List<GptMessage> prevMessages) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        List<GptMessage> messages = gptService.query(request, prompt, prevMessages);
        resultMap.put("messages", messages);

        return okResponsePackaging(resultMap);
    }
    @PostMapping("/images/generations")
    @Operation(summary="", description="")
    public ResponseEntity<Response> imageGenerations(HttpServletRequest request, @RequestParam String prompt) throws Exception {
        return okResponsePackaging(gptService.generations(request, prompt));
    }

    @PostMapping("/images/edits")
    @Operation(summary="", description="")
    public ResponseEntity<Response> imageEdits(@RequestParam String prompt, @RequestPart("file") MultipartFile file) throws Exception {
        return okResponsePackaging(gptService.imageEdits(prompt, file));
    }
}