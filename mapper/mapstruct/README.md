### MapStruct

---

### MapStruct 설명
- MapStruct는 Java Bean 타입 간의 매핑 구현을 단순화하는 Code Generator이다.
  - Model <-> DTO 의 매핑 시 자주 사용
  - 컴파일 시, 내가 작성한 매핑 방식을 코드로 생성


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
    - 해당 필드가 매핑이 되지 않는다는 warning
    ```
    > Task :compileJava
    /Users/yhnoh/git/spring/mapper/mapstruct/src/main/java/com/example/mapstruct/mapper/MemberMapper.java:13: warning: Unmapped target property: "orders".
        Member toMember(MemberDTO memberDTO);
            ^
    1 warning
    ```
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

### MapStruct가 객체의 필드를 매핑하는 여러 방법

---



### MapStruct가 객체의 필드에 접근하는 여러 방법

---

#### 1. 필드에 직접 접근
- 읽는 필드에는 public과 public final을 통해서 접근, static은 고려 대상 아님
- 작성 필드에는 public만 접근, final과 static은 고려 대상 아님

#### 2. Getter/Setter를 이용한 방법
- 필드를 private으로 두고 Getter/Setter 접근자 메서드를 통해서 매핑 가능

#### 3. Builder 패턴을 이용한 방법
- builder 패턴을 활용하여 객체를 매핑가능
- 만약 build 메서드가 여러개가 있을 경우, MapStruct는 build라는 메서드를 찾아 호출, 만약 없다면 컴파일 오류 발생 
  - `MoreThanOneBuilderCreationMethodException` 에러 발생
- 특정 build 메서드를 정의하고저 한다면 `@Builder`를 통해서 build 메서드 지정
  - `@BeanMapping, @Mapper, @MapperConfig` 함께 사용 가능

#### 4. 생성자가 여러개일 경우

```java
public class Vehicle {

    protected Vehicle() { }

    // 단일 public 생성자이기 때문에, MapStruct는 해당 객체 사용하여 매핑
    public Vehicle(String color) { }
}

public class Car {

    // public 생성자이며 파라미터가 없기 때문에, MapStruct는 해당 객체 사용하여 매핑
    public Car() { }

    public Car(String make, String color) { }
}

public class Truck {

    public Truck() { }

    // @Default 어노테이션이 있기 때문에, MapStruct는 해당 객체 사용
    @Default
    public Truck(String make, String color) { }
}

public class Van {

    //MapStruct가 어떤 생성자를 사용할지를 지정할 수 없기 때문에 에러 발생
    public Van(String make) { }

    public Van(String make, String color) { }

}
```

### Mapper에 접근할 수 있는 방법

---

#### 1. Mappers Factory
```java
public interface MemberMapper {
    /**
     * Mappers Factory를 이용하여 직접 접근
     */
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);
}
```

#### 2. Dependency injection
```java
/**
 * 스프링 컨테이너에서 Bean객체로 관리하여 주입받을 수 있는 방법
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);
}
```

### Reference
> [https://mapstruct.org/](https://mapstruct.org/) <br/>
> [https://mapstruct.org/documentation/stable/reference/html/#mapping-composition](https://mapstruct.org/documentation/stable/reference/html/#mapping-composition) <br/>
> [https://medium.com/naver-cloud-platform/%EA%B8%B0%EC%88%A0-%EC%BB%A8%ED%85%90%EC%B8%A0-%EB%AC%B8%EC%9E%90-%EC%95%8C%EB%A6%BC-%EB%B0%9C%EC%86%A1-%EC%84%9C%EB%B9%84%EC%8A%A4-sens%EC%9D%98-mapstruct-%EC%A0%81%EC%9A%A9%EA%B8%B0-8fd2bc2bc33b](https://medium.com/naver-cloud-platform/%EA%B8%B0%EC%88%A0-%EC%BB%A8%ED%85%90%EC%B8%A0-%EB%AC%B8%EC%9E%90-%EC%95%8C%EB%A6%BC-%EB%B0%9C%EC%86%A1-%EC%84%9C%EB%B9%84%EC%8A%A4-sens%EC%9D%98-mapstruct-%EC%A0%81%EC%9A%A9%EA%B8%B0-8fd2bc2bc33b) <br/>
