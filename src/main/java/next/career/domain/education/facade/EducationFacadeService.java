package next.career.domain.education.facade;

import lombok.RequiredArgsConstructor;
import next.career.domain.education.entity.Education;
import next.career.domain.education.service.EducationBatchService;
import next.career.domain.job.service.JobBatchService;
import next.career.domain.pinecone.service.PineconeService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationFacadeService {

    private final EducationBatchService educationBatchService;
    private final PineconeService pineconeService;

    public void getEducationDataFromWork24(int pageNumber, int pageSize) throws  Exception {
        List<Education> educations = educationBatchService.fetchAndSaveEducations(pageNumber, pageSize);

        Flux.fromIterable(educations)
                .flatMap(education -> pineconeService.saveEducationVector(education.getEducationId()))
                .then()
                .block();
    }
}
