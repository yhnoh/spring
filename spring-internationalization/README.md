- 현재 사내에서 다양한 언어를 지원하기 위한 메시지나 해외 API 사용시 외국 시간을 한국 시간으로 변경해야하는 일들이 있다.
- 때문에 Spring Web에서 해당 내용을 어떻게 처리해야하는지 한번 확인해볼 필요성이 있다.


### 타입존 변경


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