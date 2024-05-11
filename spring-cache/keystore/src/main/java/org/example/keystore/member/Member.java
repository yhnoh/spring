package org.example.keystore.member;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    private long id;
    private String name;
    @Builder.Default
    private Set<Level> levels = new HashSet<>();


    public Member findByLevel(Level level){
        return levels.stream().anyMatch(level::equals) ? this : null;
    }

    enum Level {
        BRONZE, SILVER, GOLD
    }
}
