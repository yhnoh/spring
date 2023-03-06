package com.example.springmsaresilience4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemberRepository {

    private final Map<Long, Member> members = new HashMap<>();

    public MemberRepository() {

        members.put(1L, Member.builder()
                .id(1L)
                .username("member1")
                .password("password1")
                .build());
        members.put(2L, Member.builder()
                .id(2L)
                .username("member2")
                .password("password2")
                .build());
        members.put(3L, Member.builder()
                .id(3L)
                .username("member3")
                .password("password3")
                .build());

    }

    public List<Member> findAll() {
        return members.values()
                .stream()
                .collect(Collectors.toList());
    }
}
