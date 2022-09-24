package com.example.springrestdocs.service;

import com.example.springrestdocs.entity.Member;
import com.example.springrestdocs.repository.MemberRepository;
import com.example.springrestdocs.request.MemberChangePasswordRequest;
import com.example.springrestdocs.request.MemberJoinRequest;
import com.example.springrestdocs.response.MemberJoinResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberJoinResponse joinMember(MemberJoinRequest request){

        Optional<Member> findMember = memberRepository.findOne(request.getUserId());
        if(findMember.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        Member joinMember = Member.builder()
                .userId(request.getUserId())
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        memberRepository.save(joinMember);

        return MemberJoinResponse.builder()
                 .userId(joinMember.getUserId())
                 .username(joinMember.getUsername())
                 .build();

    }

    public boolean deleteMember(String userId, String password){

        Member findMember = memberRepository.findOne(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 회원입니다."));

        if(!findMember.getPassword().equals(password)){
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }

        return memberRepository.remove(findMember);
    }

    public boolean changePassword(String userId, MemberChangePasswordRequest request){

        Member findMember = memberRepository.findOne(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 회원입니다."));

        if(!findMember.getPassword().equals(request.getOldPassword())){
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }

        findMember.setPassword(request.getNewPassword());
        memberRepository.save(findMember);
        return true;
    }

    public List<MemberJoinResponse> findMembers(){
        return memberRepository.findAll().stream()
                .map(member -> MemberJoinResponse.builder()
                        .userId(member.getUserId())
                        .username(member.getUsername())
                        .build())
                .collect(Collectors.toList());
    }

    public MemberJoinResponse findMember(String userId){
        Optional<Member> findMember = memberRepository.findOne(userId);
        if(!findMember.isPresent()){
            return null;
        }

        Member member = findMember.get();
        return MemberJoinResponse.builder()
                .userId(member.getUserId())
                .username(member.getUsername())
                .build();

    }

}
