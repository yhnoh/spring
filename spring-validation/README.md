### 1. 유효성 검증의 필요성
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

### 2. Spring Boot Validation
---

- 스프링 부트에서는 유효성 검증과 비지니스 로직의 수행에 대해 관심사를 분리할 수 있는 라이브러리를 제공한다.

```groovy
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

### 3. @Valid
---

- @Valid는 JSR-303 표준 스펙(자바 진영 스펙)으로써 빈 검증기(Bean Validator)를 이용해 객체의 제약 조건을 검증하도록 지시하는 어노테이션이다.
- 스프링 웹은 외부 요청으로 부터 @Valid 어노테이션이 선언된 모델에 대해서 유효성 검증을 진행할 수 있는 기능을 제공하고 있다.  

#### 3.1. @Valid 사용해보기
1. 먼저 요청시(런타임) 모델이 어떤 유효성 검증을 해야하는지, 요청 모델에 유효성 검사 제약 조건 어노테이션을 선언한다.
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

#### 3.2. Controller에서 @Valid 선언으로 요청 모델의 유효성 검증이 되는 원리 파악해보기

- 스프링 웹은 외부에서 요청할 경우 DispatcherServlet을 통해서 요청을 처리할 Controller를 찾아 위임하고, 그 결과를 전달하는 구조이다.
  - DispatcherServlet에서 Controller에게 요청을 위임하고 응답하는 일련의 과정 속에서 원하는 형태로 메시지를 요청, 응답을 받기위해 가공해주는 핸들러 객체가 있다.
  - `HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler`
- 외부에서 전달한 메서지를 어떤형태의 매개변수로 전달할지 `HandlerMethodArgumentResolver`가 결정하기 때문에 해당 클래스를 상속받은 클래스를 살펴보면 어떻게 동작하는지 확인할 수 있다. 
- 위 예제에서는 @RequestBody를 선언하여 json을 역직렬화한 객체를 컨트롤러에게 전달하고 있기 때문에 `RequestResponseBodyMethodProcessor` 클래스를 통해서 유효성 검증 로직을 확인할 수 있다.
    ```java
    public class RequestResponseBodyMethodProcessor extends AbstractMessageConverterMethodProcessor {
        @Override
        public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

            //...

            if (binderFactory != null) {
                WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
                if (arg != null) {
                    //@Valid가 선언되어 있는 요청 모델에 대한 유효성 검증 진행
                    validateIfApplicable(binder, parameter);
                    if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                        throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
                    }
                }
                if (mavContainer != null) {
                    mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
                }
            }

            return adaptArgumentIfNecessary(arg, parameter);
        }
    }
    ```  
    - `validateIfApplicable(binder, parameter);`를 통해서 @Valid가 선언되어 있는지 확인한다.
    - 이후 유효성 검증을 실패할 경우 `MethodArgumentNotValidException`에러를 던진다.


### 4. @Validated

- 스프링 웹에서 @Valid를 통해서 간단하게 유효성 검증을 진행할 수 있다는 것을 확인햇다.
- 하지만 그 이외의 계층에서 입력 유효성 체크는 어떻게 진행하는 것이 좋을까?
  - `@Validated`와 `@Valid`를 활용하면 다른 계층에서도 유효성 검증을 할 수 있다.

#### 4.1. @Validated 사용해보기 

1. 입력 모델 필드에 제약 조건들을 선언한다.
    ```java
    @RequiredArgsConstructor
    @Getter
    public class MemberJoinerCommend {

        @NotBlank
        private final String id;

        @NotBlank
        private final String password;

        @Min(1)
        private final int age;

    }
    ```
2. 유효성 검증을 해당 클래스에서 진행한다는 것을 알려주기 위하여 @Validated를 선언하고, 해당 입력 모델이 유효성 체크를 해야한다는 것을 일려주기 위해서 @Valid를 선언한다.
    ```java
    @RequiredArgsConstructor
    @Service
    @Validated
    public class JoinMemberService {
        public void joinMember(@Valid MemberJoinerCommend memberJoinerCommend) {

        }
    }
    ```
- 요청 모델에 대한 유효성 검증이 실패시 응답으로 Internal Server Error와 ConstraintViolationException 에러가 발생한다.
    ```json
    {
        "timestamp": "2023-08-15T06:44:39.120+00:00",
        "status": 500,
        "error": "Internal Server Error",
        "path": "/members-validated"
    }

    javax.validation.ConstraintViolationException
    ```

#### 4.2. @Validated 선언으로 입력 모델의 유효성 검증이 되는 원리 파악해보기

- 스프링에서는 관심사의 분리를 위하여 AOP 기술을 많이 활용하고 있으며 @Validated를 통한 유효성 검증도 AOP를 활용한다.
- @Validated를 선언하면 해당 클래스의 메서드 요청을 가로채서 유효성 검증을 할 수 있도록 Advisor가 생성된다.
    - `MethodValidationPostProcessor, MethodValidationInterceptor`
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
- 유효성 검증 로직이 정의되어있는 Advice와 @Validated가 선언되어 있는 곳에서 검증을 할 수 있도록 Pointcut이 생성된다.
- 유효성 검증을 실패할 경우 `ConstraintViolationException`에러를 던진다.




> https://mangkyu.tistory.com/174 <br/>
> https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html <br/>
> https://docs.spring.io/spring-framework/reference/core/validation/validator.html <br/>
