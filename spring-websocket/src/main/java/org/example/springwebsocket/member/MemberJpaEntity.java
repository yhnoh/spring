package org.example.springwebsocket.member;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "member")
public class MemberJpaEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * 유니크키 설정 필요
     */
    @Column(name = "email")
    private String email;

    /**
     * 유니크키 설정 필요
     */
    @Column(name = "nickname")
    private String nickname;

}
