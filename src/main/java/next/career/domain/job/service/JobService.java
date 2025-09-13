package next.career.domain.job.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import next.career.domain.UserJobMap.repository.MemberJobMapRepository;
import next.career.domain.UserJobMap.service.BookMarkFinder;
import next.career.domain.job.controller.dto.GetJobDto;
import next.career.domain.job.controller.dto.GetRoadMapDto;
import next.career.domain.job.entity.Job;
import next.career.domain.job.repository.JobCustomRepository;
import next.career.domain.job.repository.JobRepository;
import next.career.domain.job.repository.OccupationRepository;
import next.career.domain.job.service.dto.JobDto;
import next.career.domain.job.service.dto.PineconeRecommendDto;
import next.career.domain.openai.dto.AiChatDto;
import next.career.domain.openai.dto.RecommendDto;
import next.career.domain.openai.service.OpenAiService;
import next.career.domain.roadmap.entity.RoadMap;
import next.career.domain.roadmap.entity.RoadMapAction;
import next.career.domain.roadmap.entity.RoadmapInput;
import next.career.domain.roadmap.repository.RoadMapRepository;
import next.career.domain.roadmap.repository.RoadmapActionRepository;
import next.career.domain.roadmap.repository.RoadmapInputRepository;
import next.career.domain.user.entity.Member;
import next.career.domain.user.entity.MemberDetail;
import next.career.domain.user.repository.MemberDetailRepository;
import next.career.domain.user.repository.MemberRepository;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
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
public class JobService {

    private final JobRepository jobRepository;
    private final OpenAiService openAiService;
    private final JobCustomRepository jobCustomRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final MemberRepository memberRepository;
    private final BookMarkFinder bookMarkFinder;
    private final RoadMapRepository roadMapRepository;
    private final RoadmapActionRepository roadmapActionRepository;
    private final MemberJobMapRepository memberJobMapRepository;
    private final RoadmapInputRepository roadmapInputRepository;
    private final OccupationRepository occupationRepository;

    public Page<JobDto.AllResponse> getAllJob(GetJobDto.SearchRequest request, Member member, Pageable pageable) {

        return jobCustomRepository.findAll(request, pageable)
                .map(job -> JobDto.AllResponse.of(
                        job,
                        getIsBookmark(job, member)
                ));
    }

    public Page<JobDto.AllResponse> getAllJobAnonymous(GetJobDto.SearchRequest request, Pageable pageable) {

        return jobCustomRepository.findAll(request, pageable)
                .map(JobDto.AllResponse::ofAnonymous);
    }

    public Page<JobDto.AllResponse> getBookMarkedJobs(GetJobDto.SearchRequest request, Member member, Pageable pageable) {
        List<Long> bookMarkedJobIds = bookMarkFinder.findBookMarkedJobs(member.getId());

        return jobCustomRepository.getBookMarkedJobs(request, bookMarkedJobIds, pageable)
                .map(job -> JobDto.AllResponse.of(
                        job,
                        getIsBookmark(job, member)
                ));
    }

    public JobDto.Response getJob(Long jobId, Member member ) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CoreException(GlobalErrorType.JOB_NOT_FOUND_ERROR));

        Boolean isScrap = getIsBookmark(job, member);

        return JobDto.Response.of(job, isScrap);

    }

    private Boolean getIsBookmark(Job job, Member member) {
        return memberJobMapRepository.existsByMemberIdAndJobId(member.getId(), job.getJobId());
    }


    public RecommendDto.OccupationResponse recommendOccupation(Member member) {
        RecommendDto.OccupationResponse recommendOccupation = openAiService.getRecommendOccupation(member);
        List<RecommendDto.OccupationResponse.Occupation> occupationList = recommendOccupation.getOccupationList();

        List<RecommendDto.OccupationResponse.Occupation> updatedList = occupationList.stream()
                .map(occ -> {
                    String occupationImageUrl = occupationRepository.findImageUrlByOccupationName(occ.getOccupationName());
                    return RecommendDto.OccupationResponse.Occupation.builder()
                            .occupationName(occ.getOccupationName())
                            .description(occ.getDescription())
                            .strength(occ.getStrength())
                            .workCondition(occ.getWorkCondition())
                            .wish(occ.getWish())
                            .score(occ.getScore())
                            .imageUrl(occupationImageUrl)
                            .build();
                })
                .toList();

        return RecommendDto.OccupationResponse.builder()
                .occupationList(updatedList)
                .build();
    }


    @Transactional
    public Page<JobDto.AllResponse> recommendJob(Member member, Pageable pageable) {
        List<PineconeRecommendDto> recommendJobs = openAiService.getRecommendJob(member);

        if (recommendJobs == null || recommendJobs.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> recommendJobIds = recommendJobs.stream()
                .map(PineconeRecommendDto::getJobId)
                .toList();

        Page<Job> jobs = jobCustomRepository.getRecommendJobs(recommendJobIds, pageable);

        Map<Long, Long> scoreMap = recommendJobs.stream()
                .collect(Collectors.toMap(PineconeRecommendDto::getJobId, PineconeRecommendDto::getScore));

        return jobs.map(job -> JobDto.AllResponse.ofRecommend(
                job,
                getIsBookmark(job, member),
                scoreMap.getOrDefault(job.getJobId(), 0L)
        ));
    }





    @Transactional
    public void answerAIChat(Integer sequence, String answer, Member member) {
        MemberDetail memberDetail = member.getMemberDetail();

        if (memberDetail == null) {
            memberDetail = MemberDetail.createOf(member);
            member.createMemberDetail(memberDetail);
            memberDetailRepository.save(memberDetail);
            memberRepository.save(member);
        }

        switch (sequence) {
            case 1 -> memberDetail.updateExperience(answer);
            case 2 -> memberDetail.updateCertificateOrSkill(answer);
            case 3 -> memberDetail.updateWorkingStyle(answer);
            case 4 -> memberDetail.updateAvoidConditions(answer);
            case 5 -> memberDetail.updatePersonalityType(answer);
            case 6 -> memberDetail.updateInterests(answer);
            case 7 -> memberDetail.updatePreferredWorkStyles(answer);
            case 8 -> memberDetail.updateAvailableWorkingTime(answer);
            case 9 -> memberDetail.updatePhysicalCondition(answer);
            case 10 -> memberDetail.updateEducationAndCareerGoal(answer);
            default -> throw new CoreException(GlobalErrorType.MEMBER_DETAIL_SEQUENCE_NOT_FOUND);
        }
        memberDetailRepository.save(memberDetail);
    }

    public AiChatDto.OptionResponse getAIChat(Integer sequence, Member member) {
        MemberDetail memberDetail = member.getMemberDetail();
        return  openAiService.getOptions(sequence, memberDetail);
    }

    public MemberDetail getAiChatHistory(Member member) {
        return member.getMemberDetail();

    }

}
