package com.example.associatedrelationship;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Commit
@DataJpaTest
public class RelationshipTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @Autowired
    private ApplicationContext context;

    @Test
    public void dataSourceInfo() throws SQLException {
        DataSource ds = context.getBean(DataSource.class);
        DatabaseMetaData metaData = ds.getConnection().getMetaData();

        System.out.println("DataSource info ->" +
                "\nURL: " + metaData.getURL()
                        + ", \ndriver: " + metaData.getDriverName()
                        + ", \nusername: " + metaData.getUserName());
    }

    @Test
    @Transactional
    @Commit
    public void saveTest(){


        Team team = Team.createTeam("한국");
        teamJpaRepository.save(team);

        Member member = Member.createMember("member1", team);
        memberJpaRepository.save(member);
    }


    @Test
    @Transactional
    public void findTeamByMember(){
        Member member = memberJpaRepository.findById(1l).get();
        Team team = member.getTeam();

        Assertions.assertEquals("한국", team.getTeamName());
    }

    @Test
    @Transactional
    public void findMembersByTeam(){
        Team team = teamJpaRepository.findById(1l).get();
        List<Member> members = team.getMembers();

        Assertions.assertNotEquals(0, members.size());
    }

    @Test
    @Transactional
    public void notAddMemberTest(){
        Team team = Team.createTeam("일본");
        teamJpaRepository.save(team);

        Member member = Member.createMemberNotAddMemberInTeam("member1", team);
        memberJpaRepository.save(member);
        //팀에서 members 데이터가 없다.
        Assertions.assertEquals(0, team.getMembers().size());
    }
}
