package org.example.keystore.member;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class MemberRepository {

    private static final Map<Long, Member> MEMBERS = new HashMap<>();

    @PostConstruct
    public void init() {
        Set<Member.Level> levels = new HashSet<>();
        levels.add(Member.Level.BRONZE);
        levels.add(Member.Level.SILVER);
        Member member = Member.builder().id(1).name("name").levels(levels).build();
        MEMBERS.put(member.getId(), member);
    }

    public Member findByIdAndLevel(long id, Member.Level level) {
        Member member = MEMBERS.get(id);
        if (member == null) {
            return null;
        }

        return member.findByLevel(level);
    }

}
