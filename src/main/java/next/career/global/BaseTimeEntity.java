package next.career.global;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime modifiedAt;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;
}