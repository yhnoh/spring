### 1. 메시지 국제화
---

- 다국어를 지원해야하는 서비스의 경우에는 다양한 언어별로 메시지를 클라이언트에게 전달해야한다. 스프링에서는 다국어를 지원하는 방법들을 제공해주는데 이를 활용하여 다국어 처리를 쉽게 할 수 있다.
- 다국어뿐만아니라 통화별, 나라별에 따라서 대응해 주려 할 때 어떻게 해당 문제를 쉽게 해결할 수 있을지에 대한 고민도 같이 해볼 수 있다.

> 현재 사내에서는 클라이언트가 요청하는 통화별, 나라별, 언어별로 대응을 해주고 있기 때문에 해당 내용을 상세히 알아본다.  


#### 1.1. 스프링에서 메시지 국제화를 위해 알아야할 기본 키워드 
- 스프링에서 메시지 국제화를 진행하기 위한 키워드 클래스는 `Locale, LocaleResolver, MessageSource`이다.
- `Locale` 클래스는 나라, 언어를 표현할 수 있도록 제공하는 클래스이다.
    ```java
        public Locale(String language) { ... }
        public Locale(String language, String country) { ... }
      ```
    - 
- `LocaleResolver`는 Spring Web에서 웹에서 제공하는 인터페이스며, 웹 요청에 따라서 다양한 방식(헤더, 세션, 쿠키...)으로 Locale을 확인할 수 있도록 제공해주는 인터페이스이다.
  - `LocaleResolver`를 제정의 하거나, application.yml에서 `spring.web.locale`을 설정하여 `Default Locale`을 변경할 수 있다.
  - Spring Boot Web에서는 `AcceptHeaderLocaleResolver`를 구현체를 기본적으로 사용하고 있으며, `Accept-Language` 요청 헤더를 통해서 `HttpServletRequest.getLocale()`로 `Locale` 객체 정보를 가져올 수 있다.
- `MessageSource`는 `Locale`를 활용하여 국제화 메시지 제공하기 위한 인터페스이며, Locale과 일치하는 리소스에서 메시지 내용을 가져와 사용자에게 제공한다.
  - Spring Boot에서는 `ResourceBundleMessageSource` 구현체를 기본적으로 사용하고 있으며 해당 구현체는 `ResourceBundle`에서 사용자가 정의한 메시지를 Locale에 맞게 가져올 수 있다.
  - 만약 `ResourceBundleMessageSource` 구현체에서 사용하는 메시지 리소스를 다른 저장소로 변경하고자 한다면 `MessageSource`를 재정의하면 된다.

#### 1.2. 스프링 부트 웹에서 메시지 국제화 사용

- 기본 흐름
  - 클라이언트가 `Accept-Language` 요청 헤더를 통해서 서버에 요청을 하면, 서버는 `Accept-Language`값을 이용하여 지원 가능한 `Locale`을 `HttpServletRequest` 객체를 통해서 가져온다.
  - 요청 받은 `Locale`을 이용하여 `MessageSource` 에서 언어별로 사용자가 정의한 메시지를 가져올 수 있다.


1. Resource Bundle에 언어별로 정의할 메시지를 입력한다.

- Resource Bundle에 언어별로 메시지 정의
    ```properties
    ## src/main/resources/messages.properties
    hello=안녕
    ```
    ```properties
    ## src/main/resources/messages_en.properties
    hello=hello
    ```
- 현재 기본 언어는 한국어로 설정해 두었고 `hello` 코드를 입력 받았을 때, `안녕`이라는 메시지가 나오도록 작성하였고, `Locale`객체에서 `language`값을 가져올 때 `en`의 값이 나오면 `hello`라는 메시지가 나오도록 작성하였다.

2. MessageSource 인터페이스를 이용하여 클라이언트에게 언어별로 메시지 전달하기

```java
@RestController
@RequiredArgsConstructor
public class HelloController {

    private final MessageSource messageSource;

    @GetMapping("/hello")
    public String hello(HttpServletRequest httpServletRequest){
        return messageSource.getMessage("hello", null, httpServletRequest.getLocale());
    }
}
``` 

- 클라이언트가 `Accept-Language` 요청 헤더에 값을 입력하면 `HttpServletRequest` 객체에서 `Locale` 객체를 가져올 수 있고, `MessageSource`에서 `Locale`객체를 이용하여 언어별로 필요한 메시지를 클라이언트에게 응답하고 있다.

```sh
curl localhost:8080/hello --header 'Accept-Language: ko-KR'
## 응답 값: 안녕

curl localhost:8080/hello --header 'Accept-Language: en-US'
## hello
```

3. 정적인 메시지를 동적으로 변경하기

- Resource Bundle에 언어별로 동적으로 메시지 정의
    ```properties
    ## src/main/resources/messages.properties
    dynamic-hello={0} 안녕
    ```
    ```properties
    ## src/main/resources/messages_en.properties
    dynamic-hello={0} hello
    ```
- `MessageSource`를 통하여 동적 메시지 처리
    ```java
    @RestController
    @RequiredArgsConstructor
    public class HelloController {

        private final MessageSource messageSource;

        @GetMapping("/dynamic-hello")
        public String dynamicHello(HttpServletRequest httpServletRequest, @RequestParam String name){
            return messageSource.getMessage("dynamic-hello", new Object[]{name}, httpServletRequest.getLocale());
        }

    }    
    ```
    - `String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException;`를 사용하는데 `args`에 매개변수를 전달하는 것을 확인할 수 있다.
- 요청
    ```sh
    curl 'localhost:8080/dynamic-hello?name=name1' --header 'Accept-Language: ko-KR'
    ## 응답 값: name1 안녕

    curl 'localhost:8080/dynamic-hello?name=name2' --header 'Accept-Language: en-US'
    ## 응답 값: name2 hello
    ```

#### 1.3. 스프링 메시지 국제화에 대해서 생각해보기
- `LocaleResolver, MessageSource`인터페이스를 이용하여 스프링에서 메시지 국제화 처리를 간단하게 진행할 수 있는 것을 확인할 수 있다.
- 스프링에서 제공해주는 기본적인 구현체를 통해서도 메시지 국제화를 처리할 수 있지만, 스프링의 가장 큰 장점은 해당 인터페이스들을 재정의하는 것만으로도 개발 상황에 맞는 처리를 할 수 있다는 점이다.
- 때로는 클라이언트가 `Accept-Language` 헤더가 아닌 다른 방식으로 전달할 수 도 있고, 꼭 `ResourceBundle`에 있는 메시지가 아닌 다른 저장소를 이용하여 메시지 국제화를 처리해야하는 요구사항이 있을 수 있다.
- 메시지 국제화를 처리해야하는 모든 코드를 변경할 필요 없이, 해당 인터페이스들을 직접 구현하여 관심사를 분리할 수 있다는 점을 꼭 인지하여야한다. 이는 메시지 국제화에만 해당되는 내용은 아니다.

### 타입존 변경
```java
    /**
     * Gets the default {@code TimeZone} of the Java virtual machine. If the
     * cached default {@code TimeZone} is available, its clone is returned.
     * Otherwise, the method takes the following steps to determine the default
     * time zone.
     *
     * <ul>
     * <li>Use the {@code user.timezone} property value as the default
     * time zone ID if it's available.</li>
     * <li>Detect the platform time zone ID. The source of the
     * platform time zone and ID mapping may vary with implementation.</li>
     * <li>Use {@code GMT} as the last resort if the given or detected
     * time zone ID is unknown.</li>
     * </ul>
     *
     * <p>The default {@code TimeZone} created from the ID is cached,
     * and its clone is returned. The {@code user.timezone} property
     * value is set to the ID upon return.
     *
     * @return the default {@code TimeZone}
     * @see #setDefault(TimeZone)
     */
    public static TimeZone getDefault() {
        return (TimeZone) getDefaultRef().clone();
    }

```

### 메시지 국제화
- Spring의 AutoConfiguration을 통해서 `this.webProperties.getLocale()` 값을 디폴트 값으로 설정하고 있다.
- 해당 Bean을 직접 재정의하거나 yml 파일을 통해서 locale 설정을 변경할 수 있다.
```java
public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer, ServletContextAware {
    //...
    @Override
    @Bean
    @ConditionalOnMissingBean(name = DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME)
    public LocaleResolver localeResolver() {
        if (this.webProperties.getLocaleResolver() == WebProperties.LocaleResolver.FIXED) {
            return new FixedLocaleResolver(this.webProperties.getLocale());
        }
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(this.webProperties.getLocale());
        return localeResolver;
    }
    //...
}
```

> https://www.baeldung.com/spring-boot-internationalization