package next.career.domain.education.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import next.career.domain.UserJobMap.repository.MemberJobMapRepository;
import next.career.domain.job.entity.Job;
import next.career.domain.job.repository.JobCustomRepository;
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
    private final JobCustomRepository jobCustomRepository;
    private final MemberJobMapRepository memberJobMapRepository;

    private Boolean getIsBookmark(Job job, Member member) {
        return memberJobMapRepository.existsByMemberIdAndJobId(member.getId(), job.getJobId());
    }

    @Transactional
    public Page<JobDto.AllResponse> recommendEducation(Member member, Pageable pageable) {
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

    public String getRandomImageUrl() {
        List<String> imageUrls = List.of(
                "https://kr.object.ncloudstorage.com/ilhaeng-artifacts-dev/educationThumbnail/600689.png",
                "https://kr.object.ncloudstorage.com/ilhaeng-artifacts-dev/educationThumbnail/600690.png",
                "https://kr.object.ncloudstorage.com/ilhaeng-artifacts-dev/educationThumbnail/600691.png",
                "https://kr.object.ncloudstorage.com/ilhaeng-artifacts-dev/educationThumbnail/600692.png"
        );
        int randomIndex = (int) (Math.random() * imageUrls.size());
        return imageUrls.get(randomIndex);
    }
}
