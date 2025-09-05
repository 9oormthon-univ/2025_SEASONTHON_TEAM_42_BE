package next.career.domain.job.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.UserJobMap.service.BookMarkFinder;
import next.career.domain.job.controller.dto.GetJobDto;
import next.career.domain.job.controller.dto.GetRoadMapDto;
import next.career.domain.job.entity.Job;
import next.career.domain.job.repository.JobCustomRepository;
import next.career.domain.job.repository.JobRepository;
import next.career.domain.job.service.dto.JobDto;
import next.career.domain.openai.dto.AiChatDto;
import next.career.domain.openai.dto.RecommendDto;
import next.career.domain.openai.repository.PromptRepository;
import next.career.domain.openai.service.OpenAiService;
import next.career.domain.pinecone.service.PineconeService;
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

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final OpenAiService openAiService;
    private final JobCustomRepository jobCustomRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final MemberRepository memberRepository;
    private final BookMarkFinder bookMarkFinder;

    public Page<JobDto.AllResponse> getAllJob(GetJobDto.SearchRequest request, Member member, Pageable pageable) {

        return jobCustomRepository.findAll(request, pageable)
                .map(job -> JobDto.AllResponse.of(
                        job,
                        getRecommendScore(job, member),
                        getIsScrap(job, member)
                ));
    }

    public Page<JobDto.AllResponse> getBookMarkedJobs(GetJobDto.SearchRequest request, Member member, Pageable pageable) {
        List<Long> bookMarkedJobIds = bookMarkFinder.findBookMarkedJobs(member.getId());

        return jobCustomRepository.getBookMarkedJobs(request, bookMarkedJobIds, pageable)
                .map(job -> JobDto.AllResponse.of(
                        job,
                        getRecommendScore(job, member),
                        getIsScrap(job, member)
                ));
    }

    public JobDto.Response getJob(Long jobId, Member member ) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CoreException(GlobalErrorType.JOB_NOT_FOUND_ERROR));

        Integer recommendScore = getRecommendScore(job, member);
        Boolean isScrap = getIsScrap(job, member);

        return JobDto.Response.of(job, recommendScore, isScrap);

    }

    private Boolean getIsScrap(Job job, Member member) {
        return null;
    }

    private Integer getRecommendScore(Job job, Member member) {
        return null;
    }

    public RecommendDto.OccupationResponse recommendOccupation(Member member) {
        return openAiService.getRecommendOccupation(member);
    }

    @Transactional
    public RecommendDto.JobResponse recommendJob(Member member) {
        RecommendDto.JobResponse recommendJob = openAiService.getRecommendJob(member);

        String jobIdStr = recommendJob.getJobId();
        Long jobId = Long.valueOf(jobIdStr);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CoreException(GlobalErrorType.JOB_NOT_FOUND_ERROR));

        recommendJob.isScrap(getIsScrap(job, member));
        return recommendJob;
    }

    public RecommendDto.RoadMapResponse recommendRoadMap(GetRoadMapDto.Request roadmapRequest, Member member) {
        return openAiService.getRecommendRoadMap(roadmapRequest, member);


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
            case 3 -> memberDetail.updatePersonalityType(answer);
            case 4 -> memberDetail.updateInterests(answer);
            case 5 -> memberDetail.updatePreferredWorkStyles(answer);
            case 6 -> memberDetail.updateAvoidConditions(answer);
            case 7 -> memberDetail.updateAvailableWorkingTime(answer);
            case 8 -> memberDetail.updatePhysicalCondition(answer);
            case 9 -> memberDetail.updateEducationAndCareerGoal(answer);
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

//    public void getRoadMap(Member member) {
//        member.getRoadMap();
//    }
}
