package next.career.domain.education.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SaveWork24EducationDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        @JacksonXmlProperty(localName = "pageNum")
        private Integer pageNum;

        @JacksonXmlProperty(localName = "pageSize")
        private Integer pageSize;

        @JacksonXmlProperty(localName = "scn_cnt")
        private Integer scnCnt;

        @JacksonXmlProperty(localName = "srchList")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Work24EducationDto> Work24EducationDtoList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Work24EducationDto {
        @JacksonXmlProperty(localName = "eiEmplCnt3Gt10")
        private String eiEmplCnt3Gt10;

        @JacksonXmlProperty(localName = "eiEmplRate6")
        private String eiEmplRate6;

        @JacksonXmlProperty(localName = "eiEmplCnt3")
        private String eiEmplCnt3;

        @JacksonXmlProperty(localName = "eiEmplRate3")
        private String eiEmplRate3;

        @JacksonXmlProperty(localName = "certificate")
        private String certificate;

        @JacksonXmlProperty(localName = "title")
        private String title;

        @JacksonXmlProperty(localName = "realMan")
        private String realMan;

        @JacksonXmlProperty(localName = "telNo")
        private String telNo;

        @JacksonXmlProperty(localName = "stdgScor")
        private String stdgScor;

        @JacksonXmlProperty(localName = "traStartDate")
        private String traStartDate;

        @JacksonXmlProperty(localName = "grade")
        private String grade;

        @JacksonXmlProperty(localName = "ncsCd")
        private String ncsCd;

        @JacksonXmlProperty(localName = "regCourseMan")
        private String regCourseMan;

        @JacksonXmlProperty(localName = "trprDegr")
        private String trprDegr;

        @JacksonXmlProperty(localName = "address")
        private String address;

        @JacksonXmlProperty(localName = "traEndDate")
        private String traEndDate;

        @JacksonXmlProperty(localName = "subTitle")
        private String subTitle;

        @JacksonXmlProperty(localName = "instCd")
        private String instCd;

        @JacksonXmlProperty(localName = "trngAreaCd")
        private String trngAreaCd;

        @JacksonXmlProperty(localName = "trprId")
        private String trprId;

        @JacksonXmlProperty(localName = "yardMan")
        private String yardMan;

        @JacksonXmlProperty(localName = "courseMan")
        private String courseMan;

        @JacksonXmlProperty(localName = "trainTarget")
        private String trainTarget;

        @JacksonXmlProperty(localName = "trainTargetCd")
        private String trainTargetCd;

        @JacksonXmlProperty(localName = "trainstCstId")
        private String trainstCstId;

        @JacksonXmlProperty(localName = "contents")
        private String contents;

        @JacksonXmlProperty(localName = "subTitleLink")
        private String subTitleLink;

        @JacksonXmlProperty(localName = "titleLink")
        private String titleLink;

        @JacksonXmlProperty(localName = "titleIcon")
        private String titleIcon;
    }
}