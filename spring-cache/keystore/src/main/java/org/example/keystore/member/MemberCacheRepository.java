package org.example.keystore.member;

import lombok.RequiredArgsConstructor;
import org.example.keystore.config.KeyStoreRedisCacheHandler;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberCacheRepository {

    private final MemberRepository memberRepository;
    private final KeyStoreRedisCacheHandler keyStoreRedisCacheHandler;

    @Cacheable(value = "member", keyGenerator = "keyGenerator", cacheManager = "cacheManager")
    public Member findByIdAndLevel(long id, Member.Level level) {
        return memberRepository.findByIdAndLevel(id, level);
    }



    @CacheEvict(value = "member", keyGenerator = "keyGenerator", cacheManager = "cacheManager")
    public void deleteMember(long id, Member.Level level){

    }

    public void deleteMembersById(long id){
        keyStoreRedisCacheHandler.evictAll("member" + "::" + id + "::keys");
    }

}
