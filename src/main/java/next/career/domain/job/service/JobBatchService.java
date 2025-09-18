package next.career.domain.job.service;

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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobBatchService {

    private final WebClient seoulJobClient;
    private final XmlMapper xmlMapper = new XmlMapper();
    private final JobRepository jobRepository;

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
            SaveSeoulJobDto.Response response =
                    xmlMapper.readValue(xmlResponse, SaveSeoulJobDto.Response.class);

            log.info("saveseoul job dto response = {}", response);

            List<Job> jobs = response.getSeoulJobDtoList().stream()
                    .filter(dto -> !jobRepository.findAlreadyExists(dto.getJobTitle(), dto.getCompanyName()))
                    .map(this::toEntity)
                    .toList();

            return jobRepository.saveAll(jobs);

        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 실패", e);
        }
    }

    @Transactional()
    public List<Job> fetchAndSaveJobsSchedule() {

        try {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String xmlResponse = seoulJobClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/xml/GetJobInfo/{pageNo}/{numOfRows}/{id}/{area}/{occupation}/{edu}/{career}/{regDate}")
                        .build(
                                0,
                                999,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "2025-09-17"
                        )
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();


            SaveSeoulJobDto.Response response =
                    xmlMapper.readValue(xmlResponse, SaveSeoulJobDto.Response.class);

            List<Job> jobs = response.getSeoulJobDtoList().stream()
                    .filter(dto -> !jobRepository.findAlreadyExists(dto.getJobTitle(), dto.getCompanyName()))
                    .map(this::toEntity)
                    .toList();

            return jobRepository.saveAll(jobs);

        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 실패", e);
        }
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
                dto.getClosingDate()
        );
    }
}
