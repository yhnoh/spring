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

Validator 활용해보기

- MethodValidationInterceptor