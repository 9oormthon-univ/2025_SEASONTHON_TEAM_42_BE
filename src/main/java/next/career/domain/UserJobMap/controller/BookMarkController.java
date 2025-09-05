package next.career.domain.UserJobMap.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import next.career.domain.UserJobMap.entity.MemberJobMap;
import next.career.domain.UserJobMap.service.BookMarkService;
import next.career.global.apiPayload.response.ApiResponse;
import next.career.global.security.AuthDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "BookMark")
@RequestMapping("/v1/bookmark")
@RequiredArgsConstructor
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @PostMapping
    public ApiResponse<List<MemberJobMap>> bookmarkJob(@AuthenticationPrincipal AuthDetails authDetails,
                                                       @RequestParam Long jobId) {
        return ApiResponse.success(bookMarkService.register(authDetails.getMemberId(), jobId));
    }

    @DeleteMapping
    public ApiResponse<List<MemberJobMap>> cancelBookmark(@AuthenticationPrincipal AuthDetails authDetails,
                                                          @RequestParam Long jobId) {
        return ApiResponse.success(bookMarkService.unregister(authDetails.getMemberId(), jobId));
    }
}
