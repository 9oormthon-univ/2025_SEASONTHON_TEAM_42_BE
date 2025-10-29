package next.career.domain.job.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import next.career.domain.job.entity.Job;
import next.career.domain.job.repository.JobRepository;
import next.career.domain.job.service.dto.SaveSeoulJobDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobBatchService {

    private final WebClient seoulJobClient;
    private final XmlMapper xmlMapper = new XmlMapper();
    private final JobRepository jobRepository;
    private final JobService jobService;

    /**
     * 서울일자리포털 API에서 데이터 가져와 DB 저장
     */
    @Transactional
    public List<Job> fetchAndSaveJobs(int pageNo, int numOfRows) {

        log.info("pageNo = {}, numofRows = {}", pageNo, numOfRows);

        String xmlResponse = seoulJobClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/xml/GetJobInfo/{pageNo}/{numOfRows}")
                        .build(pageNo, numOfRows))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("xmlResposne = {}", xmlResponse);

        try {
            List<Job> jobs = parseAndConvertJobs(xmlResponse);

            return jobRepository.saveAll(jobs);

        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 실패", e);
        }
    }

    @Transactional()
    public List<Job> fetchAndSaveJobsSchedule() {

        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        try {
            List<Job> saveJobs = new ArrayList<>();
            String seoulJobDataXmlResponse = getSeoulJobData();
            List<Job> jobs = parseAndConvertJobs(seoulJobDataXmlResponse);
            for (Job job : jobs) {
                boolean isSuccess = jobService.saveJob(job);
                if(isSuccess) {
                    saveJobs.add(job);
                }
            }
            return saveJobs;
        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 실패", e);
        }
    }

    private List<Job> parseAndConvertJobs(String xmlResponse) {
        try {
            SaveSeoulJobDto.Response response = xmlMapper.readValue(xmlResponse, SaveSeoulJobDto.Response.class);
            log.info("서울일자리포털 응답 데이터 개수 = {}", response.getSeoulJobDtoList().size());
            return filterAndConvertToEntity(response);
        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 실패", e);
        }
    }

    private List<Job> filterAndConvertToEntity(SaveSeoulJobDto.Response response) {
        List<Job> jobs = response.getSeoulJobDtoList().stream()
                .filter(dto -> !jobRepository.findAlreadyExists(dto.getJobTitle(), dto.getCompanyName()))
                .map(this::toEntity)
                .toList();
        return jobs;
    }

    private String getSeoulJobData() {
        String xmlResponse = seoulJobClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/xml/GetJobInfo/{pageNo}/{numOfRows}/{id}/{area}/{occupation}/{edu}/{career}/{regDate}")
                        .build(
                                7,
                                100,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "2025-08-18"
                        )
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return xmlResponse;
    }

    private Job toEntity(SaveSeoulJobDto.SeoulJobDto dto) {
        return Job.ofSeoulJob(
                dto.getCompanyName(),
                dto.getJobCodeName(),
                dto.getRecruitNumber(),
                dto.getEmploymentType(),
                dto.getWorkLocation(),
                dto.getDescription(),
                dto.getWage(),
                dto.getInsurance(),
                dto.getWorkTime(),
                dto.getManagerPhone(),
                dto.getJobTitle(),
                dto.getScreeningMethod(),
                dto.getReceptionMethod(),
                dto.getRequiredDocuments(),
                dto.getJobCategory(),
                dto.getPostingDate(),
                extractClosingDate(dto.getClosingDate())
        );
    }

    private String extractClosingDate(String closingDate) {
        if (closingDate == null || closingDate.isBlank()) {
            return null;
        }

        // 괄호 안 날짜만 추출
        int start = closingDate.indexOf("(");
        int end = closingDate.indexOf(")");

        if (start != -1 && end != -1 && start < end) {
            return closingDate.substring(start + 1, end); // yyyy-MM-dd
        }

        // 괄호가 없으면 그대로 반환
        return closingDate;
    }

}
