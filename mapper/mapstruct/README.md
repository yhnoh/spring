### MapStruct

---

### MapStruct 설명
- MapStruct는 Java Bean 타입 간의 매핑 구현을 단순화하는 Code Generator이다.
  - Model <-> DTO 의 매핑 시 자주 사용
  - 컴파일 시, 내가 작성한 매핑 방식을 코드로 생성
- 컴파일을 통해서 코드를 생성하기 때문에 잘못 사용할 시, 애플리케이션 자체가 실행이 안되거나, warning을 개발자에게 알려준다.
  - 해당 필드가 매핑이 되지 않는다는 warning
  ```
  > Task :compileJava
  /Users/yhnoh/git/spring/mapper/mapstruct/src/main/java/com/example/mapstruct/mapper/MemberMapper.java:13: warning: Unmapped target property: "orders".
      Member toMember(MemberDTO memberDTO);
          ^
  1 warning
  ```

#### MapStruct 특징

- Annotation Processor를 이용하여 객체 간 매핑을 자등으로 제공
- ***컴파일 시점에 코드를 생성하여 런타임에서의 안정성을 보장***
  - 컴파일 시점에 코드를 생성하면서 타입이나 매핑이 불가능한 상태 등의 문제가 발생한 경우 컴파일 에러를 발생
- 컴파일 시점에 코드를 생성하기 때문에 런타임 시점에 코드를 매팡하는 다른 매핑 라이브러리보다 속도가 빠르다. 
- 코드를 미리 생성하기 때문에 매핑이 어떻게 이루어지는지 확인 가능
- 매핑되는 코드들을 추적하기 쉬움 
  - ***개발자가 클래스를 작성한 기반으로 코드를 매핑하기 때문에 다른 개발자가 와도 MapStruct의 기본 지식만 있다면 쉽게 확인할 수 있음***
  - 문제 원인 발견과 디버깅이 쉬움
  - 특정 필드 누락 시에도 이를 인지하기 쉬움
- 다른 형태의 매핑 또는 복잡한 형태의 매핑을 시도하는 경우 왠만해서는 해결이 되지만 MapStruct 로직이 매우 복잡해 짐
#### MapStruct 간단한 예제

```java
//내가 작성한 코드
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING) //해당 Mappeer를 Bean으로 만든다.
public interface MemberMapper {

    @Mapping(target = "id", ignore = true)
    Member toMember(MemberDTO memberDTO);

    MemberDTO toMemberDTO(Member member);
}

//컴파일 시, MapStruct가 생성해준 코드
@Component
public class MemberMapperImpl implements MemberMapper {
    public MemberMapperImpl() {
    }

    public Member toMember(MemberDTO memberDTO) {
        if (memberDTO == null) {
            return null;
        } else {
            Member.MemberBuilder member = Member.builder();
            member.username(memberDTO.getUsername());
            member.createdDatetime(memberDTO.getCreatedDatetime());
            if (memberDTO.getMemberType() != null) {
                member.memberType((MemberType) Enum.valueOf(MemberType.class, memberDTO.getMemberType()));
            }

            member.memberStatus(memberDTO.getMemberStatus());
            return member.build();
        }
    }

    public MemberDTO toMemberDTO(Member member) {
        if (member == null) {
            return null;
        } else {
            MemberDTO.MemberDTOBuilder memberDTO = MemberDTO.builder();
            memberDTO.id(member.getId());
            memberDTO.username(member.getUsername());
            memberDTO.createdDatetime(member.getCreatedDatetime());
            if (member.getMemberType() != null) {
                memberDTO.memberType(member.getMemberType().name());
            }

            memberDTO.memberStatus(member.getMemberStatus());
            return memberDTO.build();
        }
    }
}
```

###



### Reference
> [https://mapstruct.org/](https://mapstruct.org/) <br/>
> [https://mapstruct.org/documentation/stable/reference/html/#mapping-composition](https://mapstruct.org/documentation/stable/reference/html/#mapping-composition) <br/>
> [https://medium.com/naver-cloud-platform/%EA%B8%B0%EC%88%A0-%EC%BB%A8%ED%85%90%EC%B8%A0-%EB%AC%B8%EC%9E%90-%EC%95%8C%EB%A6%BC-%EB%B0%9C%EC%86%A1-%EC%84%9C%EB%B9%84%EC%8A%A4-sens%EC%9D%98-mapstruct-%EC%A0%81%EC%9A%A9%EA%B8%B0-8fd2bc2bc33b](https://medium.com/naver-cloud-platform/%EA%B8%B0%EC%88%A0-%EC%BB%A8%ED%85%90%EC%B8%A0-%EB%AC%B8%EC%9E%90-%EC%95%8C%EB%A6%BC-%EB%B0%9C%EC%86%A1-%EC%84%9C%EB%B9%84%EC%8A%A4-sens%EC%9D%98-mapstruct-%EC%A0%81%EC%9A%A9%EA%B8%B0-8fd2bc2bc33b) <br/>
