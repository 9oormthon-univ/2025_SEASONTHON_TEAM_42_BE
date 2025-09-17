package next.career.domain.education.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import next.career.domain.education.controller.dto.GetEducationDto;
import next.career.domain.education.facade.EducationFacadeService;
import next.career.domain.education.service.EducationService;
import next.career.domain.education.service.dto.EducationDto;
import next.career.domain.user.entity.Member;
import next.career.global.apiPayload.response.ApiResponse;
import next.career.global.security.AuthDetails;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/education")
@Tag(name = "Education API", description = "교육 및 맞춤형 일자리 추천 관련 API")
public class EducationController {

    private final EducationService educationService;
    private final EducationFacadeService educationFacadeService;

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
    public ApiResponse<GetEducationDto.SearchAllResponse> getBookMarkedJobs(
            @ParameterObject GetEducationDto.SearchRequest searchRequest,
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthDetails authDetails) {
        Page<EducationDto.AllResponse> EducationDtoList = educationService.getBookMarkedEducations(searchRequest, authDetails.getUser(), pageable);
        return ApiResponse.success(GetEducationDto.SearchAllResponse.of(EducationDtoList));
    }

    @GetMapping("/education-data")
    @Operation(
            summary = "서울시 채용 데이터 조회 및 저장",
            description = "서울시 채용 데이터를 가져와 DB에 저장하고, Pinecone 벡터 DB에 업서트합니다."
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
}