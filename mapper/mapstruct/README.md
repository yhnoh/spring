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

### Spring에서 MapStruct 셋팅

---

```groovy
    compileOnly 'org.projectlombok:lombok'
    testCompileOnly 'org.projectlombok:lombok'
    implementation "org.mapstruct:mapstruct:${mapstructVersion}"
    testImplementation "org.mapstruct:mapstruct:${mapstructVersion}"

    // mapstruct
    //lombok-mapstruct-binding은 lombok과 mapstruct가 충돌하는 것을 방지하기 위한 라이브러리
    annotationProcessor 'org.projectlombok:lombok-mapstruct-binding:0.2.0'
    annotationProcessor 'org.projectlombok:lombok'
    annotationProcessor "org.mapstruct:mapstruct-processor:${mapstructVersion}"
```

- MapStruct가 Lombok보다 뒤에 선언되어야 함
  - MapStruct는 Lombok이 생성한 코드를 기반을 매핑을 하는 경우가 많기 때문


### MapStruct가 객체의 필드를 매핑 유용한 방법들

---

#### 1. 기본 사용 방법

```java
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BasicMapper {

    @Mapping(target = "memberName", source = "username")
    MemberDTO toMemberDTO(Member member);
}
```

- `Member` 엔티티의 `usernmae` 필드를 제외하고는 자동 매핑 진행
- `Member` 엔티티의 `usernmae` 필드를 `MemberDTO`의 `memberName` 필드에 매핑 진행

#### 2. Composition Mapping

- Mapping을 선언한 어노테이션을 제작하여 Mapper 인터페이스의 Mapping 메서드에서 사용 가능

```java
//Mapping 선언 어노테이션
@Retention(RetentionPolicy.CLASS)
@Mapping(target = "id", ignore = true)
public @interface ToEntity {
}

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompositionMapper {

    //Mapping 선언한 어노테이션 사용
    @ToEntity
    @Mapping(target = "memberName", source = "username")
    MemberDTO toMemberDTO(Member member);
}
```

- 해당 예제를 통해서 id 값은 제외되고 매핑 진행

#### 3. 커스텀 메서드를 통한 매핑

- 인터페이스나 추상 클래스에서 자체 제작한 메서드들을 매핑 도구로 사용할 수 있다.
  
1. 자동 매핑

```java
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomMethodMapper {

    MemberDTO toAutoMemberDTO(Member member);

    default List<OrderDTO> toAutoOrderDTOs(List<Order> orders) {
        return orders.stream().map(order -> OrderDTO.builder()
                .id(order.getId())
                .orderName(order.getOrderName())
                .quantity(order.getQuantity())
                .createdDatetime(order.getCreatedDatetime())
                .build()).collect(Collectors.toList());
    }
}

```

- `MemberDTO` 내에 있는 `orders` 필드를 개발자가 직접 커스텀하여 매핑할 수 있음
- Mapper 클래스의 크기가 작거나, 서로 연관 없을 때 사용하기 좋음
  - 만약 다른 매핑 메서드애서 `List<OrderDTO> orders` 필드를 가진 DTO가 존재할 경우 서로 영향이 갈 수 있기 때문에 추천하지 않음

2. 지정 매핑

```java
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomMethodMapper {

    @Mapping(target = "orders", qualifiedByName = "ToOrderDTO")
    MemberDTO toQualifiedMemberDTO(Member member);

    @Mapping(target = "id", ignore = true)
    @Named("ToOrderDTO")
    OrderDTO toQualifiedOrderDTO(Order order);
}
```  
- 동일하게 커스텀 매핑이 가능하지만 `@Named`를 지정하여 특정 이름을 지정한 곳에서만 해당 매핑 방식을 사용하도록 가능
- ***Mapper 클래스 내에서도 지정하여 매핑을 하기 때문에 서로 영향 없이 작업 진행 가능*** 

#### 4. 여러 파라미터를 받아 매핑 진행

```java
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SeveralSourceParametersMapper {

    @Mapping(target = "username", source = "member.username")
    @Mapping(target = "orderName", source = "order.orderName")
    MemberOrderDTO toMemberOrderDTO(Member member, Order order);
}
```

- 여러개의 파라미터를 전달 받아도 매핑 진행이 가능하다.

### MapStruct를 이용한 데이터 타입을 변경

---

#### 1. 외부 데이터 변경 클래스 가져와 테이터 타입 변경

```java
public class InvokedMapper {


    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    public static LocalDateTime stringToLocalDateTime(String localDateTime) {
        return localDateTime != null ? LocalDateTime.parse(localDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    public static String stringToString(String string) {
        return string.replace("username", "username2");
    }
}

//uses 필드를 이용하여 외부 데이터 매핑 클래스 사용 가능
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = InvokedMapper.class)
public interface InvokingOtherMapper {

    MemberDTO toMemberDTO(Member member);

}
```

- `InvokedMapper`내에 있는 데이터 타입 변경 메서드를 사용 가능
  - 인자는 source, 리턴 타입은 target
- Mapper 클래스 내에 있는 모든 매핑 메서드에 영향을 줄 수 있기 때문에 추천하지 않는다.

#### 2. 외부 데이터 변경 클래스를 가져와 지정하여 테이터 타입 변경

- 두 가지 사용법이 존재하는데 실제 프로젝트를 진행하다보면 데이터 타입을 변경하는 util 클래스를 만드는 경우가 있는데 함께 사용하기 좋음

1. 이름 기반으로 지정하여 데이터 타입 변경
```java
@Named("NamedInvokedMapper")
public class NamedInvokedMapper {


    @Named("LocalDateTimeToString")
    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    @Named("StringToLocalDateTime")
    public static LocalDateTime stringToLocalDateTime(String localDateTime) {
        return localDateTime != null ? LocalDateTime.parse(localDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    @Named("StringToString")
    public static String stringToString(String string) {
        return string.replace("username", "username2");
    }
}

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = NamedInvokedMapper.class)
public interface NamedInvokingOtherMapper {


    @Mapping(target = "createdDateTime", qualifiedByName = "LocalDateTimeToString")
    @Mapping(target = "modifiedDateTime", qualifiedByName = {"NamedInvokedMapper", "StringToLocalDateTime"})
    MemberDTO toMemberDTO(Member member);

}
```

- 특정 매핑 필드에 대해서만 지정하여 데이터 타입 변경이 가능
  - `qualifiedByName` 필드에 값을 지정할 때 IDE의 도움을 받지 못하기 때문에 사용하기 조금 불편

2. 어노테이션 기반으로 지정하여 데이터 타입 변경


```java
@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface LocalDateTimeToString {
}

@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface StringToLocalDateTime {

}

@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface StringToString {

}

public class AnnotatedInvokedMapper {

    @LocalDateTimeToString
    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    @StringToLocalDateTime
    public static LocalDateTime stringToLocalDateTime(String localDateTime) {
        return localDateTime != null ? LocalDateTime.parse(localDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    @StringToString
    public static String stringToString(String string) {
        return string.replace("username", "username2");
    }
}

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = AnnotatedInvokedMapper.class)
public interface AnnotatedInvokingOtherMapper {


    @Mapping(target = "createdDateTime", qualifiedBy = LocalDateTimeToString.class)
    @Mapping(target = "username", qualifiedBy = StringToString.class)
    MemberDTO toMemberDTO(Member member);

}

```

- 어노테이션 기반이기 때문에 IDE의 도움을 받아 쉽게 해당 메서드가 어디서 사용되고 있는지 확인할 수 있음
- 하지만 지속적으로 어노테이션을 만들어줘야하는 불편함 존재, 많아질 경우 관리가 가능할지 의문


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
