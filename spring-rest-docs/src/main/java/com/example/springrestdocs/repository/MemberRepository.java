package com.example.springrestdocs.repository;

import com.example.springrestdocs.entity.Member;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemberRepository {

    private Map<String, Member> repository = new HashMap<>();

    public Member save(Member member){
        if(!StringUtils.hasText(member.getUserId())){
            throw new IllegalArgumentException("member username empty");
        }

        if(repository.containsKey(member.getUserId())){
            repository.replace(member.getUserId(), member);
        }else{
            repository.put(member.getUserId(), member);
        }

        return member;
    }

    public Optional<Member> findOne(String userId){

        if(repository.containsKey(userId)){
            Member member = repository.get(userId);
            return Optional.of(member);
        }

        return Optional.empty();
    }

    public List<Member> findAll(){
        return repository.values()
                .stream()
                .collect(Collectors.toList());
    }

    public boolean remove(Member member){
        if(!StringUtils.hasText(member.getUserId())){
            throw new IllegalArgumentException("member username empty");
        }

        if(repository.containsKey(member.getUserId())){
            repository.remove(member.getUserId());
            return true;
        }

        return false;
    }


}
