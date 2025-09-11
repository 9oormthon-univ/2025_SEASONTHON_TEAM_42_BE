package next.career.domain.job.controller.dto;

import java.util.List;

public record Work24() {

    public record CardCoursePage(
            int pageNum,

            int pageSize,

            int scn_cnt,

            List<CardCourseItem> srchList
    ){}

    public record CardCourseItem(
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
