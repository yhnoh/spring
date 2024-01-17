- 
- 때문에 Spring Web에서 해당 내용을 어떻게 처리해야하는지 한번 확인해볼 필요성이 있다.

### 1. 메시지 국제화


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

#### 1.2. 스프링 부트에서 메시지 국제화 사용



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