package next.career.domain.job.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import next.career.domain.job.controller.dto.GetJobDto;
import next.career.domain.job.entity.Job;
import next.career.domain.job.entity.QJob;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JobCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Job> findAll(GetJobDto.SearchRequest request, Pageable pageable) {
        QJob job = QJob.job;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        Optional.ofNullable(request.getKeyword()).ifPresent(n -> booleanBuilder.and(job.jobTitle.contains(n)));
        Optional.ofNullable(request.getKeyword()).ifPresent(n -> booleanBuilder.and(job.workLocation.contains(n)));
        Optional.ofNullable(request.getJobCategory()).ifPresent(n -> booleanBuilder.and(job.jobCategory.contains(n)));
        Optional.ofNullable(request.getEmploymentType()).ifPresent(n -> booleanBuilder.and(job.employmentType.contains(n)));
        Optional.ofNullable(request.getWorkLocation()).ifPresent(n -> booleanBuilder.and(job.workLocation.contains(n)));


        List<Job> content = queryFactory
                .selectFrom(job)
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(job.jobId.count())
                .from(job)
                .where(booleanBuilder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
