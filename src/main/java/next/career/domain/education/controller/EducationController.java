package next.career.domain.education.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import next.career.domain.education.controller.dto.GetEducationDto;
import next.career.domain.education.facade.EducationFacadeService;
import next.career.domain.education.service.EducationService;
import next.career.domain.education.service.HrdCourseService;
import next.career.domain.education.service.dto.EducationDto;
import next.career.domain.education.service.dto.SaveWork24EducationDto;
import next.career.domain.job.controller.dto.GetJobDto;
import next.career.domain.job.service.dto.JobDto;
import next.career.domain.user.entity.Member;
import next.career.global.apiPayload.response.ApiResponse;
import next.career.global.security.AuthDetails;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/education")
@Tag(name = "Education API", description = "교육 및 맞춤형 일자리 추천 관련 API")
public class EducationController {

    private final EducationService educationService;
    private final EducationFacadeService educationFacadeService;
    private final HrdCourseService hrdCourseService;

    @GetMapping("/recommend")
    @Operation(summary = "맞춤형 교육 추천", description = "사용자의 정보를 기반으로 맞춤형 일자리를 추천합니다.")
    public ApiResponse<GetEducationDto.SearchAllResponse> recommendJob(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails,
            @Parameter(hidden = true) Pageable pageable) {
        Member member = authDetails.getUser();
        Page<EducationDto.AllResponse> EducationDtoList = educationService.recommendEducation(member, pageable);
        return ApiResponse.success(GetEducationDto.SearchAllResponse.of(EducationDtoList));
    }

    @GetMapping("/bookmarks")
    @Operation(summary = "북마크된 교육 조회", description = "북마크된 교육을 조회합니다.")
    public ApiResponse<GetEducationDto.SearchAllResponse> getBookMarkedEducations(
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails) {
        Page<EducationDto.AllResponse> EducationDtoList = educationService.getBookMarkedEducations(authDetails.getUser(), pageable);
        return ApiResponse.success(GetEducationDto.SearchAllResponse.of(EducationDtoList));
    }

    @GetMapping("/education-data")
    @Operation(
            summary = "교육 데이터 조회 및 저장 - 사용 X",
            description = "교육 데이터를 가져와 DB에 저장하고, Pinecone 벡터 DB에 업서트합니다."
    )
    public ApiResponse<?> getEducationDataFromWork24(
            @Parameter(
                    description = "페이징 정보 (page, size)"
            )
            Pageable pageable
    ) throws Exception {
        educationFacadeService.getEducationDataFromWork24(pageable.getPageNumber(), pageable.getPageSize());
        return ApiResponse.success();
    }

    @GetMapping
    @Operation(summary = "교육 조회", description = "교육을 조회합니다.")
    public ApiResponse<GetEducationDto.SearchAllResponse> raw(@RequestParam(defaultValue = "") String keyword,
                                                              @Parameter(hidden = true) Pageable pageable,
                                                              @RequestParam(required = false) String region,
                                                              @RequestParam(required = false) String type,
                                                              @AuthenticationPrincipal AuthDetails authDetails) {
        return ApiResponse.success(educationService.getEducations(keyword, region, type, pageable, authDetails.getUser()));
    }

    @GetMapping("/anonymous")
    @Operation(
            summary = "전체 교육 조회 (비로그인 사용자)",
            description = """
                로그인하지 않은 사용자가 전체 교육(채용 공고) 목록을 조회합니다.  
                검색 키워드(`keyword`)와 기간(`startYmd`, `endYmd`)을 설정할 수 있으며,  
                기본값은 전체 기간(2025-01-01 ~ 2025-12-31)입니다.  
                페이징(`page`, `size`) 파라미터를 함께 사용할 수 있습니다.
                """
    )
    public ApiResponse<GetEducationDto.SearchAllResponse> getAllJobAnonymous(
            @Parameter(description = "검색 키워드", example = "백엔드 개발자")
            @RequestParam(defaultValue = "") String keyword,

            @Parameter(description = "지역 (예: 서울, 경기, 부산 등)", example = "서울")
            @RequestParam(required = false) String region,

            @Parameter(description = "교육형태 (ONLINE, OFFLINE)", example = "ONLINE")
            @RequestParam(required = false) String type,

            @Parameter(hidden = true, description = "페이징 정보 (page, size)")
            Pageable pageable
    ) {
        return ApiResponse.success(educationService.getEducationsForAnonymous(keyword, region, type, pageable));
    }

}