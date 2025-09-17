package next.career.domain.education.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import next.career.domain.education.entity.Education;
import next.career.domain.education.repository.EducationRepository;
import next.career.domain.education.service.dto.SaveWork24EducationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationBatchService {

    @Value("${work24.api-key}")
    private String apiKey;

    @Value("${work24.endpoints.card-course-list}")
    private String apiPath;

    private final RestClient work24Client;
    private final XmlMapper xmlMapper = new XmlMapper();
    private final EducationRepository educationRepository;

    /**
     * 고용24 API에서 데이터 가져와 DB 저장
     */
    @Transactional
    public List<Education> fetchAndSaveEducations(int pageNo, int numOfRows) throws Exception {

        log.info("pageNo = {}, numofRows = {}", pageNo, numOfRows);

        SaveWork24EducationDto.Response response = work24Client.get()
                .uri(urlBuilder -> urlBuilder.path(apiPath)
                        .queryParam("authKey", apiKey)
                        .queryParam("returnType", "JSON")
                        .queryParam("outType", 1)
                        .queryParam("pageNum", pageNo)
                        .queryParam("pageSize", numOfRows)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(SaveWork24EducationDto.Response.class);

        try {
            log.info("SaveWork24 education dto response = {}", response);

            List<Education> educations = response.srchList().stream()
                    .filter(dto -> !educationRepository.findAlreadyExists(dto.title(), dto.subTitle()))
                    .map(this::toEntity)
                    .toList();

            return educationRepository.saveAll(educations);

        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 실패", e);
        }
    }

    private Education toEntity(SaveWork24EducationDto.Work24EducationDto dto) {
        return Education.ofWork24Education(
                dto.certificate(),
                dto.title(),
                dto.subTitle(),
                dto.traStartDate(),
                dto.traEndDate(),
                dto.address(),
                dto.courseMan(),
                dto.trainTarget(),
                dto.trprDegr(),
                dto.titleLink()
        );
    }
}
