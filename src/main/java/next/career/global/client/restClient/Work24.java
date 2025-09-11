package next.career.global.client.restClient;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record Work24() {

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

    // 컨트롤러 응답용 (도메인 페이지)
    public record CardCoursePage(
            int pageNum,

            int pageSize,

            int scn_cnt,

            List<CardCourseItem> srchList
    ){}
}
