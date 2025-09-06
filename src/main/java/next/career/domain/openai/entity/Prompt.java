package next.career.domain.openai.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Prompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promptId;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String tag;
}
