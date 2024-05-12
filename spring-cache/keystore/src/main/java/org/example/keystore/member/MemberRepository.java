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
        Member member1 = this.createMember(1);
        MEMBERS.put(member1.getId(), member1);

        Member member2 = this.createMember(2);
        MEMBERS.put(member2.getId(), member2);
    }

    private Member createMember(long id){
        Set<Member.Level> levels = new HashSet<>();
        levels.add(Member.Level.BRONZE);
        levels.add(Member.Level.SILVER);
        return Member.builder().id(id).name("name" + id).levels(levels).build();

    }

    public Member findByIdAndLevel(long id, Member.Level level) {
        Member member = MEMBERS.get(id);
        if (member == null) {
            return null;
        }

        return member.findByLevel(level);
    }

}
