package next.career.domain.education.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import next.career.domain.education.controller.dto.GetEducationDto;
import next.career.domain.education.entity.Education;
import next.career.domain.education.entity.QEducation;
import next.career.domain.job.controller.dto.GetJobDto;
import next.career.domain.job.entity.Job;
import next.career.domain.job.entity.QJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class EducationCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Education> findAll(GetEducationDto.SearchRequest request, Pageable pageable) {
        QEducation education = QEducation.education;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        Optional.ofNullable(request.getKeyword()).ifPresent(n -> booleanBuilder.and(education.title.contains(n)));
        Optional.ofNullable(request.getKeyword()).ifPresent(n -> booleanBuilder.and(education.address.contains(n)));
        Optional.ofNullable(request.getWorkLocation()).ifPresent(n -> booleanBuilder.and(education.address.contains(n)));


        List<Education> content = queryFactory
                .selectFrom(education)
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(education.educationId.count())
                .from(education)
                .where(booleanBuilder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public Page<Education> getBookMarkedEducations(List<Long> bookmarkedIds, Pageable pageable) {
        QEducation education = QEducation.education;

        if (bookmarkedIds == null || bookmarkedIds.isEmpty()) {
            return Page.empty(pageable);
        }

        BooleanBuilder where = new BooleanBuilder();

        where.and(education.educationId.in(bookmarkedIds));

        List<Education> content = queryFactory
                .selectFrom(education)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(education.educationId.count())
                .from(education)
                .where(where);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public Page<Education> getRecommendEducations(List<Long> recommendEducationIds, Pageable pageable) {
        QEducation education = QEducation.education;

        if (recommendEducationIds == null || recommendEducationIds.isEmpty()) {
            return Page.empty(pageable);
        }

        CaseBuilder.Cases<Integer, NumberExpression<Integer>> orderCases =
                new CaseBuilder().when(education.educationId.eq(recommendEducationIds.get(0))).then(0);

        for (int i = 1; i < recommendEducationIds.size(); i++) {
            orderCases = orderCases.when(education.educationId.eq(recommendEducationIds.get(i))).then(i);
        }

        NumberExpression<Integer> orderByCase = orderCases.otherwise(recommendEducationIds.size());

        List<Education> content = queryFactory
                .selectFrom(education)
                .where(education.educationId.in(recommendEducationIds))
                .orderBy(orderByCase.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(education.educationId.count())
                .from(education)
                .where(education.educationId.in(recommendEducationIds));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
