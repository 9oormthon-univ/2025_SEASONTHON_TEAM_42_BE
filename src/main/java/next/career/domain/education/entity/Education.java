package next.career.domain.education.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.career.domain.job.entity.Job;
import next.career.global.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationId;

    private String certificate;      //키워드

    private String title;            //제목

    private String subTitle;         //교육기관

    private String traStartDate;     //훈련시작일

    private String traEndDate;       //훈련마감일

    private String address;          //주소

    private String courseMan;        //가격

    private String trainTarget;      //키워드

    private String trprDegr;         //수강횟수

    private String titleLink;        //링크

    private String imageUrl;

    public static Education ofWork24Education(
            String certificate,
            String title,
            String subTitle,
            String traStartDate,
            String traEndDate,
            String address,
            String courseMan,
            String trainTarget,
            String trprDegr,
            String titleLink
    ) {
        return Education.builder()
                .certificate(certificate)
                .title(title)
                .subTitle(subTitle)
                .traStartDate(traStartDate)
                .traEndDate(traEndDate)
                .address(address)
                .courseMan(courseMan)
                .trainTarget(trainTarget)
                .trprDegr(trprDegr)
                .titleLink(titleLink)
                .imageUrl(null)
                .build();
    }
}
