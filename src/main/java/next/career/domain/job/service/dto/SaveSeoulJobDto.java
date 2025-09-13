package next.career.domain.job.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import java.util.List;

public class SaveSeoulJobDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response{
        @JacksonXmlProperty(localName = "list_total_count")
        private Integer totalCount;

        @JacksonXmlProperty(localName = "row")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<SeoulJobDto> seoulJobDtoList;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SeoulJobDto{
        @JacksonXmlProperty(localName = "CMPNY_NM")
        private String companyName;

        @JacksonXmlProperty(localName = "JOBCODE_NM")
        private String jobCodeName;

        @JacksonXmlProperty(localName = "RCRIT_NMPR_CO")
        private Integer recruitNumber;

        @JacksonXmlProperty(localName = "EMPLYM_STLE_CMMN_MM")
        private String employmentType;

        @JacksonXmlProperty(localName = "WORK_PARAR_BASS_ADRES_CN")
        private String workLocation;

        @JacksonXmlProperty(localName = "DTY_CN")
        private String description;

        @JacksonXmlProperty(localName = "HOPE_WAGE")
        private String wage;

        @JacksonXmlProperty(localName = "JO_FEINSR_SBSCRB_NM")
        private String insurance;

        @JacksonXmlProperty(localName = "WORK_TIME_NM")
        private String workTime;

        @JacksonXmlProperty(localName = "MNGR_PHON_NO")
        private String managerPhone;

        @JacksonXmlProperty(localName = "JO_SJ")
        private String jobTitle;

        @JacksonXmlProperty(localName = "MODEL_MTH_NM")
        private String screeningMethod;

        @JacksonXmlProperty(localName = "RCEPT_MTH_NM")
        private String receptionMethod;

        @JacksonXmlProperty(localName = "PRESENTN_PAPERS_NM")
        private String requiredDocuments;

        @JacksonXmlProperty(localName = "BSNS_SUMRY_CN")
        private String jobCategory;

        @JacksonXmlProperty(localName = "JO_REG_DT")
        private String postingDate;

        @JacksonXmlProperty(localName = "RCEPT_CLOS_NM")
        private String closingDate;

        @JacksonXmlProperty(localName = "RCEPT_MTH_IEM_NM")
        private String applyLink;
    }


}
