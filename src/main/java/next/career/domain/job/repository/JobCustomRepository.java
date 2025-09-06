package next.career.domain.job.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import next.career.domain.job.controller.dto.GetJobDto;
import next.career.domain.job.entity.Job;
import next.career.domain.job.entity.QJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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

    public Page<Job> getBookMarkedJobs(GetJobDto.SearchRequest request, List<Long> bookmarkedIds, Pageable pageable) {
        QJob job = QJob.job;

        if (bookmarkedIds == null || bookmarkedIds.isEmpty()) {
            return Page.empty(pageable);
        }

        BooleanBuilder where = new BooleanBuilder();

        // 기존 검색 조건 (keyword는 제목/근무지 OR 매칭)
        Optional.ofNullable(request.getKeyword()).ifPresent(k -> where.and(job.jobTitle.contains(k).or(job.workLocation.contains(k))));
        Optional.ofNullable(request.getJobCategory()).ifPresent(c -> where.and(job.jobCategory.contains(c)));
        Optional.ofNullable(request.getEmploymentType()).ifPresent(e -> where.and(job.employmentType.contains(e)));
        Optional.ofNullable(request.getWorkLocation()).ifPresent(w -> where.and(job.workLocation.contains(w)));

        // 북마크 ID 필터 (Long PK)
        where.and(job.jobId.in(bookmarkedIds));

        List<Job> content = queryFactory
                .selectFrom(job)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(job.jobId.count())
                .from(job)
                .where(where);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public Page<Job> getRecommendJobs(List<Long> recommendJobIds, Pageable pageable) {
        QJob job = QJob.job;

        if (recommendJobIds == null || recommendJobIds.isEmpty()) {
            return Page.empty(pageable);
        }

        BooleanBuilder where = new BooleanBuilder();
        where.and(job.jobId.in(recommendJobIds));

        List<Job> content = queryFactory
                .selectFrom(job)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(job.jobId.count())
                .from(job)
                .where(where);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
