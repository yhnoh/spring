package org.example.keystore.member;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberCacheRepository memberCacheRepository;
    @GetMapping("/{id}")
    public Member getMember(@PathVariable("id") long id, @RequestParam Member.Level level) {
        return memberCacheRepository.findByIdAndLevel(id, level);
    }

    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable("id") long id, @RequestParam Member.Level level){
        memberCacheRepository.deleteMember(id, level);
    }
}
