package next.career.domain.report.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.openai.service.OpenAiService;
import next.career.domain.report.controller.dto.GetStrengthReportDto;
import next.career.domain.user.entity.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final OpenAiService openAiService;

    public GetStrengthReportDto.Response createStrengthReport(Member member) {
        return openAiService.createStrengthReport(member);
    }
}
