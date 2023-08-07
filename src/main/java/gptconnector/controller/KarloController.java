package gptconnector.controller;

import gptconnector.model.GptMessage;
import gptconnector.model.Response;
import gptconnector.service.GptQueryService;
import gptconnector.service.KarloService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
@Api(tags = "KarloController")
@Tag(name = "KarloController", description = "Karlo 컨트롤러")
@RestController
public class KarloController {

    private static final Logger logger = LoggerFactory.getLogger(KarloController.class);
    private final KarloService karloService;
    @Autowired
    public KarloController(KarloService karloService){
        this.karloService = karloService;
    }

    public ResponseEntity<Response> okResponsePackaging(Map<String, Object> result) {
        Response response = Response.builder()
                .message("요청 성공")
                .result(result).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/karlo/generations")
    @Operation(summary="", description="")
    public ResponseEntity<Response> imageGenerations(HttpServletRequest request, @RequestParam String prompt, @RequestParam String negative_prompt) throws Exception {
        return okResponsePackaging(karloService.generations(request, prompt, negative_prompt));
    }

    @PostMapping("/karlo/edits")
    @Operation(summary="", description="")
    public ResponseEntity<Response> imageEdits(HttpServletRequest request, @RequestParam String prompt, @RequestParam String negative_prompt, @RequestPart("file") MultipartFile file) throws Exception {
        return okResponsePackaging(karloService.edits(request, prompt, negative_prompt, file));
    }
}