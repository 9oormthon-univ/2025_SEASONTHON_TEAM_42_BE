package next.career.global.client.restClient;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class Work24RawClient {

    private final RestClient restClient;

    @Value("${work24.endpoints.card-course-list}")
    private String apiPath;

    @Value("${work24.api-key}")
    private String apiKey;

    public Work24.CardCoursePage callRaw(String keyword, int pageNo, int pageSize, String startYmd, String endYmd) {
        return restClient.get()
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

