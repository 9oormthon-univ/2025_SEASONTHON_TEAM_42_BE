package next.career.domain.openai.repository;

import next.career.domain.openai.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromptRepository extends JpaRepository<Prompt, Long> {

    List<Prompt> findAllByTag(String tag);
}
