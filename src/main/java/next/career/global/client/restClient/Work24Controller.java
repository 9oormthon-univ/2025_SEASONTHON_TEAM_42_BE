package next.career.global.client.restClient;

import lombok.RequiredArgsConstructor;
import next.career.global.apiPayload.response.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/work24")
@RequiredArgsConstructor
public class Work24Controller {

    private final Work24RawClient rawClient;

    @GetMapping("/card-courses-new")
    public ApiResponse<Work24.CardCoursePage> raw(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "20250101") String startYmd,
            @RequestParam(defaultValue = "20251231") String endYmd
    ) {
        Work24.CardCoursePage src = rawClient.callRaw(keyword, pageNo, pageSize, startYmd, endYmd);

        return ApiResponse.success(src);
    }
}
