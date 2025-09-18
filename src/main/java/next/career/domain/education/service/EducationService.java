package next.career.domain.education.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import next.career.domain.UserEducationMap.repository.MemberEducationMapRepository;
import next.career.domain.UserEducationMap.service.EducationBookMarkFinder;
import next.career.domain.education.controller.dto.GetEducationDto;
import next.career.domain.education.entity.Education;
import next.career.domain.education.repository.EducationCustomRepository;
import next.career.domain.education.service.dto.EducationDto;
import next.career.domain.job.controller.dto.GetJobDto;
import next.career.domain.job.service.dto.JobDto;
import next.career.domain.job.service.dto.PineconeRecommendDto;
import next.career.domain.openai.service.OpenAiService;
import next.career.domain.user.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EducationService {

    private final OpenAiService openAiService;
    private final EducationCustomRepository educationCustomRepository;
    private final MemberEducationMapRepository memberEducationMapRepository;
    private final EducationBookMarkFinder educationBookMarkFinder;

    private Boolean getIsBookmark(Education education, Member member) {
        return memberEducationMapRepository.existsByMemberIdAndEducationId(member.getId(), education.getEducationId());
    }

    @Transactional
    public Page<EducationDto.AllResponse> recommendEducation(Member member, Pageable pageable) {
        List<PineconeRecommendDto> recommendEducations = openAiService.getRecommendEducation(member);

        if (recommendEducations == null || recommendEducations.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> recommendEducationIds = recommendEducations.stream()
                .map(PineconeRecommendDto::getJobId)
                .toList();

        Page<Education> educations = educationCustomRepository.getRecommendEducations(recommendEducationIds, pageable);

        Map<Long, Long> scoreMap = recommendEducations.stream()
                .collect(Collectors.toMap(PineconeRecommendDto::getJobId, PineconeRecommendDto::getScore));

        return educations.map(education -> EducationDto.AllResponse.ofRecommend(
                education,
                getIsBookmark(education, member),
                scoreMap.getOrDefault(education.getEducationId(), 0L)
        ));
    }

    public Page<EducationDto.AllResponse> getBookMarkedEducations(Member member, Pageable pageable) {
        List<Long> bookMarkedJobIds = educationBookMarkFinder.findBookMarkedEducations(member.getId());

        return educationCustomRepository.getBookMarkedEducations(bookMarkedJobIds, pageable)
                .map(education -> EducationDto.AllResponse.of(
                        education,
                        getIsBookmark(education, member)
                ));
    }

    public Page<EducationDto.AllResponse> getAllJobAnonymous(GetEducationDto.SearchRequest request, Pageable pageable) {

        return educationCustomRepository.findAll(request.getKeyword(), pageable)
                .map(EducationDto.AllResponse::ofAnonymous);
    }

    public GetEducationDto.SearchAllResponse getEducations(String keyword, Pageable pageable, String startYmd, String endYmd) {

        Page<Education> response = educationCustomRepository.findAll(keyword, pageable);

        List<EducationDto.AllResponse> educationList = response.getContent().stream()
                .map(education -> EducationDto.AllResponse.of(education, false)) // 기본은 북마크 X
                .toList();

        return GetEducationDto.SearchAllResponse.of(response, educationList);
    }
}
