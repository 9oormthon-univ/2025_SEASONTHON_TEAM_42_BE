package next.career.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.zapply.product.domain.user.enumerate.MemberType;
import org.zapply.product.global.BaseTimeEntity;

import java.time.LocalDate;

@Getter
@Entity
@SQLDelete(sql = "UPDATE \"member\" SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint")
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(320)")
    private String email;

    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String phoneNumber;

    @Column(nullable = false, columnDefinition = "varchar(30)")
    private String residentNumber;

    @Column(columnDefinition = "varchar(20)")
    private String name;

    @Column(columnDefinition = "varchar(20)")
    private LocalDate birthDate;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credential_id", referencedColumnName = "id")
    private Credential credential;

    @Builder
    public Member(String email, String phoneNumber, String residentNumber, MemberType memberType, Credential credential) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.residentNumber = residentNumber;
        this.memberType = memberType != null ? memberType : MemberType.GENERAL;
        this.credential = credential;
    }
}

