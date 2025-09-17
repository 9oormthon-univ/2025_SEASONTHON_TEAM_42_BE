package next.career.domain.education.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.education.service.dto.SaveWork24EducationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class HrdCourseService {

    private final RestClient work24Client;

    @Value("${work24.endpoints.card-course-list}")
    private String apiPath;

    @Value("${work24.api-key}")
    private String apiKey;

    public SaveWork24EducationDto.Response getEducations(String keyword, int pageNo, int pageSize, String startYmd, String endYmd) {
        return work24Client.get()
                .uri(urlBuilder -> urlBuilder.path(apiPath)
                        .queryParam("authKey", apiKey)
                        .queryParam("returnType", "JSON")
                        .queryParam("outType", 1)
                        .queryParam("pageNum", pageNo)
                        .queryParam("pageSize", pageSize)
                        .queryParam("srchTraStDt", startYmd)
                        .queryParam("srchTraEndDt", endYmd)
                        .queryParam("srchTraProcessNm", keyword)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(SaveWork24EducationDto.Response.class);
    }
}

