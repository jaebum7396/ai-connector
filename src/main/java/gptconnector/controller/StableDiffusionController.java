package gptconnector.controller;

import gptconnector.model.Response;
import gptconnector.model.StableDiffusionApiRequest;
import gptconnector.service.KarloService;
import gptconnector.service.StableDiffusionService;
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
import java.util.Map;

@Slf4j
@Api(tags = "StableDiffusionController")
@Tag(name = "StableDiffusionController", description = "StableDiffusion 컨트롤러")
@RestController
public class StableDiffusionController {

    private static final Logger logger = LoggerFactory.getLogger(StableDiffusionController.class);
    private final StableDiffusionService stableDiffusionService;
    @Autowired
    public StableDiffusionController(StableDiffusionService stableDiffusionService){
        this.stableDiffusionService = stableDiffusionService;
    }

    public ResponseEntity<Response> okResponsePackaging(Map<String, Object> result) {
        Response response = Response.builder()
                .message("요청 성공")
                .result(result).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/stable-diffusion/v4/dreambooth")
    @Operation(summary="", description="")
    public void dreambooth(HttpServletRequest request, @RequestBody StableDiffusionApiRequest stableDiffusionApiRequest) throws Exception {
        //return okResponsePackaging(stableDiffusionService.dreambooth(request, stableDiffusionApiRequest));
        stableDiffusionService.dreambooth(request, stableDiffusionApiRequest);
    }

    /*@PostMapping("/stable-diffusion/v4/dreambooth/img2img")
    @Operation(summary="", description="")
    public ResponseEntity<Response> dreamboothImg2Img(HttpServletRequest request, @RequestParam String prompt, @RequestParam String negative_prompt, @RequestPart("file") MultipartFile file) throws Exception {
        return okResponsePackaging(stableDiffusionService.dreamboothImg2Img(request, prompt, negative_prompt, file));
    }*/
}