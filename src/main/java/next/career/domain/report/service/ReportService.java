package next.career.domain.report.service;

import lombok.RequiredArgsConstructor;
import next.career.domain.openai.service.OpenAiService;
import next.career.domain.report.controller.dto.GetStrengthReportDto;
import next.career.domain.report.entity.StrengthReport;
import next.career.domain.report.repository.StrengthReportRepository;
import next.career.domain.user.entity.Member;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final OpenAiService openAiService;
    private final StrengthReportRepository strengthReportRepository;

    @Transactional
    public GetStrengthReportDto.Response createStrengthReport(Member member) {
        Integer latestVersion = strengthReportRepository.findMaxVersionByMember(member)
                .orElse(0);

        GetStrengthReportDto.Response response = openAiService.createStrengthReport(member);

        int newVersion = latestVersion + 1;

        List<StrengthReport> reports = response.getReportList().stream()
                .map(r -> StrengthReport.of(
                        member,
                        r.getStrength(),
                        r.getExperience(),
                        r.getKeyword(),
                        r.getJob(),
                        r.getAppeal(),
                        newVersion
                ))
                .toList();

        strengthReportRepository.saveAll(reports);

        return response;
    }

    public GetStrengthReportDto.Response getStrengthReport(Member member) {

        List<StrengthReport> strengthReportList = strengthReportRepository.findAllByMemberAndIsDeletedFalse(member);

        List<GetStrengthReportDto.Report> reportList = strengthReportList.stream()
                .map(r -> GetStrengthReportDto.Report.of(
                        r.getStrengthReportId(),
                        r.getStrength(),
                        r.getExperience(),
                        r.getKeyword(),
                        r.getJob()
                        , r.getAppeal()
                ))
                .toList();


        return GetStrengthReportDto.Response.of(reportList);
    }

    public GetStrengthReportDto.Response getStrengthReportCurrentHistory(Member member) {

        List<StrengthReport> strengthReportList = strengthReportRepository.findAllByMemberAndIsDeletedFalse(member);

        int latestVersion = strengthReportList.stream()
                .mapToInt(StrengthReport::getVersion)
                .max()
                .orElse(0);

        List<StrengthReport> latestReports = strengthReportList.stream()
                .filter(r -> r.getVersion() == latestVersion)
                .toList();

        List<GetStrengthReportDto.Report> reportList = latestReports.stream()
                .map(r -> GetStrengthReportDto.Report.of(
                        r.getStrengthReportId(),
                        r.getStrength(),
                        r.getExperience(),
                        r.getKeyword(),
                        r.getJob()
                        , r.getAppeal()
                ))
                .toList();


        return GetStrengthReportDto.Response.of(reportList);
    }

    @Transactional
    public void deleteStrengthReport(Member member, Long strengthReportId) {
        StrengthReport strengthReport = strengthReportRepository.findById(strengthReportId)
                .orElseThrow(() -> new CoreException(GlobalErrorType.STRENGTH_REPORT_NOT_FOUND));

        if(!Objects.equals(strengthReport.getMember().getId(), member.getId())){
            throw new CoreException(GlobalErrorType.FORBIDDEN);
        }

        strengthReport.markAsDeleted();
    }
}
