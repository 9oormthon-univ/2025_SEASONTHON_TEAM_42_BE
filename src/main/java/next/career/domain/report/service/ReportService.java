package next.career.domain.report.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.openai.service.OpenAiService;
import next.career.domain.report.controller.dto.GetStrengthReportDto;
import next.career.domain.report.entity.StrengthReport;
import next.career.domain.report.repository.StrengthReportRepository;
import next.career.domain.user.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final OpenAiService openAiService;
    private final StrengthReportRepository strengthReportRepository;

    @Transactional
    public GetStrengthReportDto.Response createStrengthReport(Member member) {
        GetStrengthReportDto.Response response = openAiService.createStrengthReport(member);

        strengthReportRepository.deleteAllByMember(member);

        List<StrengthReport> reports = response.getReportList().stream()
                .map(r -> StrengthReport.of(
                        member,
                        r.getStrength(),
                        r.getExperience(),
                        r.getKeyword(),
                        r.getJob()
                ))
                .toList();

        strengthReportRepository.saveAll(reports);

        return response;


    }
}
