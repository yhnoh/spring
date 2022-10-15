
### ORM의 특징
---

- OOP에서 가장 큰 특징 중 하나가 연관 되어 있는 ***객체끼리 서로 메시지를 주고 받으면서 객체의 상태를 변경한다는 점***이다.
- 데이터베이스의 테이블 관계에서도 이와 비슷한 경우가 발생할 수 있다. 
    - 예를 들어 회원이 쿠폰을 사용하면 테이블에서는 회원이 쿠폰을 사용했다는 flag 값을 변경할 것이고, 다른 테이블에서는 전체 쿠폰 개수 중에서 사용량을 할 것이다.
> **ORM (Object Relational Mapping)** <br/>
> - 객체 지향 프로그래밍은 클래스를 이용하고 관계형 데이터베이스는 테이블을 이용하는데 객체 모델과 관계형 모델 간의 불일치가 존재한다.
> - ***ORM은 객체간의 관계를 바탕으로 관계형 데이터 베이스를 매핑한다.***
- 개발자가 어떤 객체의 상태를 변경했거나 관련된 객체의 상태를 변경하면 SQL문을 작성하지 않고 같이 변경되었으면 좋겠고, 때로는 서로 연관된 객체를 불러오면 데이터베이스에있는 내용이 자동으로 불러와지면 좋을 것이라고 생각할 것이다. ORM은 이를 가능하게 한다.
- 그렇다면 객체와 테이블의 관계라는 것의 큰 차이점은 뭐가 있을까?
    1. 객체는 상속을 사용할 수 있다. 데이터베이스는 상속의 개념이 없다.
    2. 객체는 관련된 객체를 가지고 올때 참조를 활용하지만, 데이터베이스는 JOIN문을 활용해 값을 통해서 관련된 테이블을 가지고 온다.
        - 막말로 테이블은 값만 일치한다면 관련되어 있지 않은 테이블의 정보도 가지고 올 수 있다. 이 점이 데이터 모델링을 할 때 가장 어렵게 만드는 점 중에 하나인거 같다.
        - 객체는 본인이 속한 필드 내에서 객체 그래프 탐색을 통해서 다른 객체를 가지고올 수 있다.
    3. 객체는 단방향으로만 탐색이 가능하지만, 데이터베이스는 양방향으로 탐색이 가능하다.

### 연관관걔 매핑
---

- 위에서 설명했듯이 ORM은 관계형 데이터베이스의 테이블 정보들을 객체에 매핑시킬 수 있다. 그렇다면 어떻게 관계를 매핑을 시키는지 확인해보자.
- Team와 Member 클래스를 만들어서 1:N 구조로 만들 것이다.
    - Member는 하나의 Team에만 소속될 수 있는 구조이다.

#### Member와 Team 연관관계 매핑하기

1. Member 클래스에 `@ManyToOne`을 활용하여 Team객체 저장
```java
@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public static Member createMember(String username, Team team){
        Member member = new Member();
        member.username = username;
        member.team = team;
        team.addMember(member);
        return member;
    }
}
```
- Member 객체는 기본적으로 생성될 때 Team 객체를 저장해야하기 때문에 Team 객체를 매개변수로 넘겨준다.
- Team 객체를 넘겨 받은 이후에 필드에 저장하고 있기 때문에 해당 Member 객체는 Team의 정보를 탐색할 수있다.
- 관계형 데이터 베이스에서도 매핑을 하기 위해서 `@ManyToOne`을 활용한다.
- `@JoinColumn`은 해당 필드를 외래키로 지정하겠다는 의미이다.
- 데이터베이스 내에서는 Member 테이블에서 team_id 컬럼에 Team의 id값이 저장될 것이다.
    - Member 테이블에서는 team_id 값을 통해서 Team 테이블의 값을 조회할 수 있다.

2. Team클래스에 `@OneToMany`활용하여 Team에 해당하는 Member들 전부 불러오기

```java
@Entity
@Getter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;
    private String teamName;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();


    public static Team createTeam(String teamName){
        Team team = new Team();
        team.teamName = teamName;
        return team;
    }

    public void addMember(Member member){
        if(member == null){
            throw new NotFoundDateException("member is null when add member in team");
        }
        members.add(member);
    }

}
```
- Team 객체를 생성할 때는 Member 객체의 정보를 저장할 필요가 없기 때문에 teamName의 정보만 저장한다.
- `members` 필드는 해당 팀에 소속된 Member객체의 정보를 가져와야 하기 때문에 필요하다.
    - `addMember(Member member)` 메소드를 활용하여 Member 객체가 생성될 때 members에 데이터를 추가한다.
- 관계형 데이터베이스에도 매핑을 하기위해서 `@OneToMany`를 활용한다.
- 데이터베이스 내에서 Team 테이블에는 실제로 Member 테이블에 관련된 데이터가 없다.
    - 하지만 JOIN문을 통해서 Member의 정보를 가져올 수 있다.
    - 객체의 경우는 단방향으로만 정보를 탐색이 가능하기 때문에 이렇게 Member와 Team 객체 둘다 관련 정보를 가지고 있어야 한다.
- `mappedBy="team"`은 Member 클래스의 어떤 필드와 매핑을 할지를 의미하는 것이다.
    - 예를 들어 Team에서 getMembers()라는 메소드를 활용해 Member들의 정보를 가져온다고 할때, 데이터베이스에 정보가 들어 있고 해당 객체에는 members의 값이 안들어 있을 경우 관련된 SQL문을 사용하여 members에 매핑을 해준다. 때문에 어떤 필드와 연관이 있는지 적어주어야한다.
    - 한마디로 연관관계의 주인을 적어주어야하는데 이후에 설명하겠다. 

#### 3. 연관관계가 설정된 객체를 데이터베이스에 저장
```java
@Test
@Transactional
@Commit
public void saveTest(){


    Team team = Team.createTeam("한국");
    teamJpaRepository.save(team);

    Member member = Member.createMember("member1", team);
    memberJpaRepository.save(member);
}
```
- member1은 한국이라는 team에 소속시키기 위해서 위와 같이 코드를 작성하였다.

- SQL Log 확인하기
    ```sql
    Hibernate: 
        insert 
        into
            team
            (team_name) 
        values
            (?)
    Hibernate: 
        insert 
        into
            member
            (team_id, username) 
        values
            (?, ?)
    ```
    - Member table에 insert할 때 team_id 컬럼에 값이 들어가는 것을 확인할 수 있다.

#### 4. Member 객체에서 team 필드의 값 가져오기
```java
@Test
@Transactional
public void findTeamByMember(){
    Member member = memberJpaRepository.findById(1l).get();
    Team team = member.getTeam();

    Assertions.assertEquals("한국", team.getTeamName());
}
```
- SQL문을 작성하지 않았는데도 객체 그래프 탐색을 통해서 연관된 객체의 값을 가져오는지 확인해보자.
- SQL Log 확인하기
```sql
select
    member0_.member_id as member_i1_0_0_,
    member0_.team_id as team_id3_0_0_,
    member0_.username as username2_0_0_,
    team1_.team_id as team_id1_1_1_,
    team1_.team_name as team_nam2_1_1_ 
from
    member member0_ 
left outer join
    team team1_ 
        on member0_.team_id=team1_.team_id 
where
    member0_.member_id=?
```
- join문을 통해서 해당 값을 가져오는지 확인할 수 있다.

#### 5. Team 객체에서 members 필드의 값 가져오기
```java
@Test
@Transactional
public void findMembersByTeam(){
    Team team = teamJpaRepository.findById(1l).get();
    List<Member> members = team.getMembers();

    Assertions.assertNotEquals(0, members.size());
}
```
- SQL log 확인하기
```sql
Hibernate: 
    select
        team0_.team_id as team_id1_1_0_,
        team0_.team_name as team_nam2_1_0_ 
    from
        team team0_ 
    where
        team0_.team_id=?
Hibernate: 
    select
        members0_.team_id as team_id3_0_0_,
        members0_.member_id as member_i1_0_0_,
        members0_.member_id as member_i1_0_1_,
        members0_.team_id as team_id3_0_1_,
        members0_.username as username2_0_1_ 
    from
        member members0_ 
    where
        members0_.team_id=?
```
- 해당 멤버를 가져오기는 했는데 이번에는 JOIN문을 사용하지 않았다.

### 연관관계 주인
---

- 데이터베이스의 경우 회원 <-> 팀 양방향으로 검색이 가능하지만 객체의 경우에는 단방향으로만 검색이 가능하다.
    - Team객체에서 members의 필드가 없으면 member에 대한 검색이 불가능하다.
    - members의 객체를 읽어오기 위하여 `mappedBy` 속성을 사용하였다.
- 이러한 차이점으로 인해서 두 객체의 연관관계중 ***하나는 외래키를 관리하는 객체, 나머지 하나는 기본키를 기반으로 외래키에 해당하는 데이터를 읽어올 수 있는 객체로 양방향 매핑***을 해주어야한다.
- 양방향 연관관계 매핑 시 주의해야할 점이 하나를 연관관계의 주인으로 정해야한다.
    - ***연관관계의 주인만이 외래 키를 관리(등록, 수정,삭제)할 수 있다.***
    - ***주인이 아닌쪽은 읽기만 가능하다.***
- 위의 Member와 Team 클래스에서 외래키를 관리하는 클래스는 Member 였기 때문에 연관관계 주인이된다.

#### 양방향 연관관계시 주의점
- ***양방향 연관관계를 사용할때는 관련된 객체의 추가 및 수정,삭제 시 주의***를 해야한다.
#### Member 생성 시 Team에 member 데이터 추가하지 않는 경우
```java
@Entity
@Getter
public class Member {

    //...

    public static Member createMemberNotAddMemberInTeam(String username, Team team){
        Member member = new Member();
        member.username = username;
        member.team = team;
        //team.addMember(member); 호출 안함
        return member;

    }
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
```
- 위와같이 하나의 트랜잭션 내에서 기대한것과 다른 경우가 나와버릴 수 있다. 때문에 양방향 연관관계시 객체의 관계 설정을 잘해야한다.

#### 연관관계 편의 메소드 활용
```java
@Entity
@Getter
public class Member {
    //...

    public static Member createMember(String username, Team team){
        Member member = new Member();
        member.username = username;
        member.team = team;
        team.addMember(member);
        return member;
    }
}
```

> **Reference**
> - https://velog.io/@conatuseus/%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84-%EB%A7%A4%ED%95%91-%EA%B8%B0%EC%B4%88-2-%EC%96%91%EB%B0%A9%ED%96%A5-%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84%EC%99%80-%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84%EC%9D%98-%EC%A3%BC%EC%9D%B8