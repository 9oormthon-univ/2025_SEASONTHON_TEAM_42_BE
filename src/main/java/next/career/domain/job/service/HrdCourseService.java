package next.career.domain.job.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.job.controller.dto.Work24;
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

    public Work24.CardCoursePage callRaw(String keyword, int pageNo, int pageSize, String startYmd, String endYmd) {
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
                .body(Work24.CardCoursePage.class);
    }
}

