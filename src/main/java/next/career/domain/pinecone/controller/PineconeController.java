package next.career.domain.pinecone.controller;

import lombok.RequiredArgsConstructor;
import next.career.domain.pinecone.service.PineconeService;
import next.career.global.apiPayload.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PineconeController {

    private final PineconeService pineconeService;

    @PostMapping("/job/piencone")
    public ApiResponse<?> jobPienconeSave() {
        pineconeService.saveAllJobVector();
        return ApiResponse.success();
    }
}
