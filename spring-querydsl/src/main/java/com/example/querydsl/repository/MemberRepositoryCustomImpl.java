package com.example.querydsl.repository;

import com.example.querydsl.entity.dto.MemberSearchCondition;
import com.example.querydsl.entity.dto.MemberTeamDto;
import com.example.querydsl.entity.dto.QMemberTeamDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.querydsl.entity.QMember.member;
import static com.example.querydsl.entity.QTeam.team;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();

        if(hasText(condition.getUsername())){
            builder.and(member.username.eq(condition.getUsername()));
        }

        if(hasText(condition.getTeamName())){
            builder.and(team.name.eq(condition.getTeamName()));
        }

        if(condition.getAgeGoe() != null){
            builder.and(member.age.goe(condition.getAgeGoe()));
        }

        if(condition.getAgeLoe() != null){
            builder.and(member.age.loe(condition.getAgeLoe()));
        }

        return queryFactory
                .select(new QMemberTeamDto(member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(builder)
                .fetch();
    }

    @Override
    public List<MemberTeamDto> searchByParameters(MemberSearchCondition condition) {

        return queryFactory
                .select(new QMemberTeamDto(member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )

                .fetch();


    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private BooleanExpression usernameEq(String username) {
        return hasText(username) ? member.username.eq(username) : null;
    }

    @Override
    public Page<MemberTeamDto> searchPaging(MemberSearchCondition condition, Pageable pageable){

        List<MemberTeamDto> contents = selectQuery(condition
                ,new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = selectQuery(condition, member.count());

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }


    private <T> JPAQuery<T> selectQuery(MemberSearchCondition condition, Expression<T> expression) {
        return queryFactory
                .select(expression)
                .from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                );
    }
}
