package next.career.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.career.domain.user.enumerate.MemberType;
import next.career.global.BaseTimeEntity;
import org.hibernate.annotations.SQLDelete;

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

    @Column(columnDefinition = "varchar(20)")
    private String phoneNumber;

    @Column(columnDefinition = "varchar(2000)")
    private String profileImageUrl;

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

    @Column(length = 20)
    private String provider;

    @Column(length = 64)   // 공급자 고유키
    private String providerId;

    @Builder
    public Member(String email, String phoneNumber, MemberType memberType, Credential credential) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.memberType = memberType != null ? memberType : MemberType.GENERAL;
        this.credential = credential;
    }

    public static Member newSocial(String name, String profileImageUrl, String email, String provider, String providerId,
                                   String phoneNumber, MemberType memberType) {
        Member m = new Member(email, phoneNumber, memberType, null); // 소셜은 Credential 없음
        m.name = name;
        m.profileImageUrl = profileImageUrl;
        m.provider = provider;
        m.providerId = providerId;
        return m;
    }

    public void linkSocial(String provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }

    public boolean isSocial() {
        return this.provider != null && this.providerId != null;
    }

    public Long getId() { return id; }
}
