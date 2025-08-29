package next.career.global.clova;

import next.career.global.clova.dto.ClovaMessage;
import next.career.global.clova.dto.ClovaRole;
import next.career.global.clova.dto.request.ClovaRequest;
import next.career.global.clova.dto.response.ClovaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ClovaAiClientTest {

    @Autowired
    private ClovaAiClient clovaAiClient;

    @Value("${clova.api-key}")
    private String apiKey;

    @Value("${clova.model-name}")
    private String modelName;

    private ClovaRequest clovaRequest;

    @BeforeEach
    public void setUp() {
        clovaRequest = ClovaRequest.from(
                List.of(
                        ClovaMessage.of(ClovaRole.SYSTEM.getValue(), "대한민국 역사를 알려줘"),
                        ClovaMessage.of(ClovaRole.USER.getValue(), "임진왜란에 대해 자세하게 알려줘")
                ));
    }

    @Test
    public void 클로바API_연동_성공() {
        String authorizationHeader = "Bearer " + apiKey;
        ClovaResponse storyLines = clovaAiClient.getCompletions(authorizationHeader, modelName, clovaRequest);
        System.out.println(storyLines);
    }

}