package next.career.domain.education.service.dto;

import java.util.List;

public record SaveWork24EducationDto() {

    public record Response(
            int pageNum,

            int pageSize,

            int scn_cnt,

            List<Work24EducationDto> srchList
    ){}

    public record Work24EducationDto(
            String eiEmplCnt3Gt10,
            String eiEmplRate6,
            String eiEmplCnt3,
            String eiEmplRate3,
            String certificate,
            String title,
            String realMan,
            String telNo,
            String stdgScor,
            String traStartDate,
            String grade,
            String ncsCd,
            String regCourseMan,
            String trprDegr,
            String address,
            String traEndDate,
            String subTitle,
            String instCd,
            String trngAreaCd,
            String trprId,
            String yardMan,
            String courseMan,
            String trainTarget,
            String trainTargetCd,
            String trainstCstId,
            String contents,
            String subTitleLink,
            String titleLink,
            String titleIcon
    ){}
}
