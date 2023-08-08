package aiconnector.controller;

import aiconnector.model.Response;
import aiconnector.model.sdapi.SdApiRequest;
import aiconnector.service.SdApiService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Api(tags = "sdApiController")
@Tag(name = "sdApiController", description = "StableDiffusionApi 컨트롤러")
@RestController
public class SdApiController {

    private static final Logger logger = LoggerFactory.getLogger(SdApiController.class);
    private final SdApiService sdApiService;
    @Autowired
    public SdApiController(SdApiService sdApiService){
        this.sdApiService = sdApiService;
    }

    public ResponseEntity<Response> okResponsePackaging(Map<String, Object> result) {
        Response response = Response.builder()
                .message("요청 성공")
                .result(result).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/sdapi/v4/dreambooth")
    @Operation(summary="", description="")
    public void dreambooth(HttpServletRequest request, @RequestBody SdApiRequest sdApiRequest) throws Exception {
        sdApiService.dreambooth(request, sdApiRequest);
    }
}