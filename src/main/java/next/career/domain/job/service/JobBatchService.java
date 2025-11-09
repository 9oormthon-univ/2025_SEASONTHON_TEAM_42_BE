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
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

        long startTime = System.currentTimeMillis();
        log.info("[START] fetchAndSaveJobs 시작 pageNo={}, numOfRows={}", pageNo, numOfRows);

        // 🌐 1. API 호출
        String xmlResponse = seoulJobClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/xml/GetJobInfo/{pageNo}/{numOfRows}")
                        .build(pageNo, numOfRows))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        long apiElapsed = System.currentTimeMillis() - startTime;
        log.info("[TIME] 서울시 API 응답 완료 ({}ms)", apiElapsed);

        // 📦 2. XML 파싱
        List<Job> jobs;
        try {
            long parseStart = System.currentTimeMillis();
            jobs = parseAndConvertJobs(xmlResponse);
            long parseElapsed = System.currentTimeMillis() - parseStart;
            log.info("[TIME] XML 파싱 완료: {}개, {}ms", jobs.size(), parseElapsed);
        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 실패", e);
        }

        // 💾 3. DB 저장
        long dbStart = System.currentTimeMillis();
        List<Job> saved = jobRepository.saveAll(jobs);
        long dbElapsed = System.currentTimeMillis() - dbStart;
        log.info("[TIME] DB 저장 완료: {}개, {}ms", saved.size(), dbElapsed);

        // ✅ 4. 전체 수행 시간
        long totalElapsed = System.currentTimeMillis() - startTime;
        log.info("[TIME] fetchAndSaveJobs 전체 완료 (총 {}ms)", totalElapsed);

        return saved;
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

    public Mono<List<Job>> fetchAndSaveJobsAsync(int pageNo, int numOfRows) {
        long startTime = System.currentTimeMillis();

        return seoulJobClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/xml/GetJobInfo/{pageNo}/{numOfRows}")
                        .build(pageNo, numOfRows))
                .retrieve()
                .bodyToMono(String.class)
                .elapsed()
                .flatMap(tuple -> {
                    long elapsed = tuple.getT1();
                    log.info("[TIME] 서울시 API 호출 완료 ({}ms)", elapsed);
                    return Mono.just(tuple.getT2());
                })
                .flatMap(xmlResponse ->
                        Mono.fromCallable(() -> parseAndConvertJobs(xmlResponse))
                                .subscribeOn(Schedulers.boundedElastic())
                )
                .elapsed()
                .flatMap(tuple -> {
                    long parsingElapsed = tuple.getT1();
                    List<Job> jobs = tuple.getT2();
                    log.info("[TIME] XML 파싱 완료: {}개, {}ms", jobs.size(), parsingElapsed);

                    return Mono.fromCallable(() -> jobRepository.saveAll(jobs))
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .elapsed()
                .map(tuple -> {
                    long dbElapsed = tuple.getT1();
                    List<Job> jobs = tuple.getT2();
                    log.info("[TIME] DB 저장 완료: {}개, {}ms", jobs.size(), dbElapsed);
                    return jobs;
                })
                .doOnError(e -> log.error("[ERROR] fetchAndSaveJobsAsync 실패", e));
    }
}
