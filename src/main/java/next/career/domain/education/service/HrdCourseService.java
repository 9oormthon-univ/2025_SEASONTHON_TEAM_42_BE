package next.career.domain.education.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.education.controller.dto.GetEducationDto;
import next.career.domain.education.service.dto.EducationDto;
import next.career.domain.education.service.dto.SaveWork24EducationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HrdCourseService {

    private final RestClient work24Client;

    @Value("${work24.endpoints.card-course-list}")
    private String apiPath;

    @Value("${work24.api-key}")
    private String apiKey;

//    public GetEducationDto.SearchAllResponse getEducations(String keyword, int pageNo, int pageSize, String startYmd, String endYmd) {
//        SaveWork24EducationDto.Response response = work24Client.get()
//                .uri(urlBuilder -> urlBuilder.path(apiPath)
//                        .queryParam("authKey", apiKey)
//                        .queryParam("returnType", "JSON")
//                        .queryParam("outType", 1)
//                        .queryParam("pageNum", pageNo)
//                        .queryParam("pageSize", pageSize)
//                        .queryParam("srchTraStDt", startYmd)
//                        .queryParam("srchTraEndDt", endYmd)
//                        .queryParam("srchTraProcessNm", keyword)
//                        .build())
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .body(SaveWork24EducationDto.Response.class);
//
//        List<EducationDto.AllResponse> educationList = response.srchList().stream()
//                .map(EducationDto::fromWork24EducationDto)
//                .collect(Collectors.toList());
//
//        return GetEducationDto.SearchAllResponse.of(response, educationList);
//    }
}

