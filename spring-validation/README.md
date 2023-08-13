### 유효성 검증의 필요성
---

- 웹 프로젝트에서는 외부의 요청 -> 비지니스 로직 수행 -> 데이터 쓰기 및 읽기 작업을 주로 진행한다.
    - 각 계층을 넘나들며 개발자는 저장소에 데이터를 잘못 작성하지 않기 위해 유효성 검증 코드를 작성하게 된다.
    - 유효성 검증 로직을 작성하는 의도는 좋으나, ***유효성 검증 로직과 비지니스 로직을 수행해야 하는 코드들이 한곳에 작성되어 잇으면 해당 코드들을 파악하기가 힘들어진다.***
    - 이로 인해서 비지니스 로직에 집중할 수 없고, 유지보수가 힘들어지는 코드가 되기 때문에 유효성 검증과 비지니스 로직을 분리할 수 있는 코드를 작성하는 것이 중요하다.
- 유효성 검증과 비지니스 로직이 함께 있는 코드란 어떤 것인지 간단한 예시를 통해서 알아보자.
```java
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final JoinMemberService joinMemberService;

    @PostMapping("/members")
    public void joinMember(@RequestBody MemberJoinerRequest memberJoinerRequest) {

        //유효성 검증
        String id = memberJoinerRequest.getId();
        String password = memberJoinerRequest.getPassword();
        int age = memberJoinerRequest.getAge();

        if (StringUtils.hasText(id)) {
            throw new IllegalArgumentException("id가 공백입니다.");
        }

        if (StringUtils.hasText(password)) {
            throw new IllegalArgumentException("password가 공백입니다.");
        }

        if (age <= 0) {
            throw new IllegalArgumentException("age는 0보다 작을 수 없습니다.");
        }

        //회원가입 요청
        joinMemberService.joinMember(memberJoinerRequest);
    }
}
```
- 외부의 요청에 의해 회원가입 요청 객체를 받아 회원가입을 시켜주는 간단한 로직이다.
  - 해당 코드에서는 회원가입을 요청하기전 유효성 검증과 회원가입을 요청하는 기능이 담겨 있다.
- 회원가입 요청 객체의 필드 개수가 얼마 안되어 딱히 어렵지 않은 코드처럼 보이지만 필드가 수십개가 넘어가고 해당 필드에대해 전부 유효성 체크를 하는 로직과 비지니스 로직 수행 요청 로직이 몇개씩 섞여 들어가게 된다면 보기 어려운 코드가 탄생할 수 있다.
- 때문에 우리는 유효성 검증과 비지니스 로직의 관심사를 분리하여 보기 좋은 코드, 유지 보수가 쉬운 코드를 작성할 수 있어야 한다. 

### Spring Boot Validation
---

- 스프링 부트에서는 유효성 검증과 비지니스 로직의 수행에 대해 관심사를 분리할 수 있는 라이브러리를 제공한다.

```groovy
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

### @Valid
---

- @Valid는 JSR-303 표준 스펙(자바 진영 스펙)으로써 빈 검증기(Bean Validator)를 이용해 객체의 제약 조건을 검증하도록 지시하는 어노테이션이다.
- 스프링 웹은 외부 요청으로 부터 @Valid 어노테이션이 선언된 모델에 대해서 유효성 검증을 진행할 수 있는 기능을 제공하고 있다.  

#### @Valid 사용법
1. 먼저 요청시(런타임) 모델이 어떤 유효성 검증을 할지 알기 위해서, 요청 모델에 유효성 검사 제약 조건 어노테이션을 선언한다.
  - 요청 모델 필드에 선언하는 어노테이션은 `javax.validation`에서 기본적으로 제공하는 제약 조건이나 사용자 정의 제약 조건을 정의할 수 있다.
    ```java
    @RequiredArgsConstructor
    @Getter
    public class MemberJoinerRequest {

        @NotBlank
        private final String id;

        @NotBlank
        private final String password;

        @Min(1)
        private final int age;

    }
    ```
2. 컨트롤러로 부터 요청이 들어올 때, 요청 모델이 유효성 체크를 진행해야한다는 것을 알 수 있도록 @Valid 어노테이션을 선언한다.
    ```java
    @RestController
    @RequiredArgsConstructor
    public class MemberController {

        private final JoinMemberService joinMemberService;

        @PostMapping("/members-valid")
        public void joinValidMember(@RequestBody @Valid MemberJoinerRequest memberJoinerRequest) {
            //회원가입 요청
            joinMemberService.joinMember(memberJoinerRequest);
        }

    }
    ```
- 요청 모델에 대한 유효성 검증이 실패시 응답으로 Bad Request와 MethodArgumentNotValidException 에러가 발생한다.
```json
{
  "timestamp": "2023-08-12T08:38:55.523+00:00",
  "status": 400,
  "error": "Bad Request",
  "path": "/validate"
}

org.springframework.web.bind.MethodArgumentNotValidException: Validation failed for argument
```

#### Controller에서 @Valid 선언으로 요청 모델의 유효성 검증이 되는 원리 파악해보기

- 스프링 웹은 외부에서 요청할 경우 DispatcherServlet을 통해서 요청을 처리할 Controller를 찾아 위임하고, 그 결과를 받아아는 구조이다.
  - DispatcherServlet에서 Controller에게 요청을 위임하고 응답하는 일련의 과정 속에서 원하는 형태로 메시지를 요청, 응답을 받기위해 가공해주는 핸들러 객체가 있다.
  - `HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler`
  



- 그 전에 유효성 검증과 비지니스 로직이 함께 있는 코드란 무엇인지 한번 알아보자.



- Spring에서는 `LocalValidatorFactoryBean`가 제약조건 검증을 담당한다.

디스패처 서블릿을 통해서 @Valid

- 디스패처 서블릿을 컨트롤러를 요

- `org.springframework.web.bind.MethodArgumentNotValidException` 에러가 발생하며 DefaultHandlerExceptionResolver을 통해서 에러를 핸들링하여
  400으로 응답을 한다.
- @Valid는 기본적으로 컨트롤러에서만 동작하며 기본적으로 다른 계층에서는 검증이 되지 않는다.

```json
{
  "timestamp": "2023-08-12T08:38:55.523+00:00",
  "status": 400,
  "error": "Bad Request",
  "path": "/validate"
}

org.springframework.web.bind.MethodArgumentNotValidException: Validation failed for argument
```

- https://mangkyu.tistory.com/174

https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html

- https://docs.spring.io/spring-framework/reference/core/validation/validator.html


- @Validated
- 스프링 웹에서는 컨트롤러를 통해 들어오는 요청에 대해 유효성 체크를 하기 위해서 @Valid를 이용하여 입력 파라미터의 검증을 진행하고 있다.
- 하지만 그 이외의 계층에서 입력 유효성 체크는 어떻게 진행하는 것이 좋을까?
- 가장 간단한 방법은 @Validated를 활용하는 것이다.
- 스프링에서 제공하는 @Validated 어노테이션을 활용하면 @Validated가 선언된 클래스의 메소드 요청을 가로채서 유효성 검증을 진행한다.
    - @Validated를 선언하면 해당 클래스의 메서드 요청을 가로채서 유효성 검증을 할 수 있도록 AOP 어드바이스에 등록된다.
        - MethodValidationPostProcessor, MethodValidationInterceptor
    - @Validated를 선언된 클래스들의 메소드들이 호출될 때 AOP의 포인트 컷으로써 요청을 가로채서 유효성 검증을 진행한다.

```java
public class MethodValidationPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor
        implements InitializingBean {

    private final Class<? extends Annotation> validatedAnnotationType = Validated.class;

    @Nullable
    private Validator validator;

    //.......
    @Override
    public void afterPropertiesSet() {
        Pointcut pointcut = new AnnotationMatchingPointcut(this.validatedAnnotationType, true);
        this.advisor = new DefaultPointcutAdvisor(pointcut, createMethodValidationAdvice(this.validator));
    }

    protected Advice createMethodValidationAdvice(@Nullable Validator validator) {
        return (validator != null ? new MethodValidationInterceptor(validator) : new MethodValidationInterceptor());
    }
}
```

- `validator`필드의 값을 가져오기 위해서 @Valid가 선언된 곳에서만 활용이 가능하다.
- javax.validation.ConstraintViolationException 에러가 발생한다.

커스텀 애노테이션(Custom Annotation) 만들어 직접 유효성 검사하기

Validator 활용해보

- MethodValidationInterceptor