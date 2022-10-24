### 로그 레벨
---
심각도 : ERROR > WARN > INFO > DEBUG > TRACE

- ERROR : 예상하지 못한 심각한 문제가 발생한 경우, 즉시 조취를 취해야하는 경우
- WARN : 로직상 유효성 확인, 예상 가능한 문제로 인한 예외 처리, 당장 서비스를 운영하는데는 문제가 없지만 주의해야하는 경우
- INFO : 운영에 참고할만한 사항, 중요한 비지니스 프로세스가 완료됨
- DEBUG : 개발 단계에서 사용하며, SQL 로깅이 가능
- TRACE : 모든 레벨에 대한 로깅이 추적되므로 개발 단계에서 사용


### 스프링 부트에서 환경별 로깅 설정하기
---
- 실제 프로젝트를 진행하다보면 각 환경별로 어떻게 로깅을 남겨야 할지를 고민할 때가 있다.
    - 로컬환경에서는 굳이 파일로 확인할 필요가 없으니 콘솔에 출력할지 고민
    - 개발환경에서는 다른 개발자도 참가할 수 있는데 파일에 로그 기록을 남길지 고민
    - 실제 운영환경에서는 로그파일 때문에 디스크 풀이 일어나서 서버가 다운되면 안되니 어떤 정책으로 삭제해줄지 고민
    - 등등..
#### 스프링 환경별에 따른 application-${profile}.yml 설정
```text
└── resources
    ├── application.yml
    ├── application-local.yml
    ├── application-dev.yml
    └── application-prod.yml
```
- 스프링 프로젝트를 실행하기전 profile property를 셋팅하면 각 환경에 맞는 application.yml 파일을 셋팅해준다.
    ```bash
    java -Dspring.profiles.active=${profile 명} -jar ./build/libs/${jar 파일명}.jar
    ```
#### logback.xml 파일을 작성하는 이유
- application.yml 파일을 통해서 log에 필요한 속성을 셋팅은 할 수 있지만 실제 프로덕트 환경에서는 사용하기에는 세부적인 설정을 하기 어렵기 때문에 logback.xml을 작성한다.

### local 환경에 맞는 logback.xml 작성하기
---

- local 환경에서는 콘솔 출력을 위한 셋팅을 한다.
- `src/main/resources/logback-spring.xml` 이 스프링 logback.xml 을 작성하는 기본 경로 및 파일 명이다. 일단 로컬환경에서는 logback 파일명을 변경해서 사용을 해보자.
#### 1. application-local.yml
- logback-spring-local.xml 파일에 작성된 로깅 설정 사용하기
```groovy
logging:
    config: classpath:logback-spring-local.xml
```

#### 2. logback-properties.xml에 로그 관련 프로퍼티 값 셋팅하기
```xml
<?xml version="1.0" encoding="UTF-8"?>

<included>
    <!-- 로그 패턴에 색상 적용 %clr(pattern){color} -->
    <conversionRule conversionWord="clr" converterClass="org.springframework.boot.logging.logback.ColorConverter" />

    <property name="CONSOLE_LOG_PATTERN" value="${CONSOLE_LOG_PATTERN:-%clr(%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd'T'HH:mm:ss.SSSXXX}}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>
</included>
```

#### 3. logback-spring-local.xml 파일 적성
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <!-- logback-properties.xml 파일 포함-->
    <include resource="logback-properties.xml"></include>

    <!-- 콘솔(STDOUT) -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>${CONSOLE_LOG_PATTERN}</Pattern>
        </layout>
    </appender>
    <!-- root 로깅 레벨 설정-->
    <root level="INFO">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

### dev 환경에 맞는 logback.xml 작성하기
---

- dev 환경에서는 콘솔과 파일을 통해서 로그를 출력한다.
- 파일 로그
    1. 파일 로그는 info, error 레벨의 파일로그를 각각 남긴다.
    2. info level의 파일 로그는 info, warn, error 로그까지 남긴다.
    3. error level의 파일 로그는 error 로그만 남긴다.

#### 1. application-dev.yml
- logback-spring-dev.xml 파일에 작성된 로깅 설정 사용하기
```groovy
logging:
    config: classpath:logback-spring-dev.xml
```

#### 2. logback-properties.xml에 로그 관련 프로퍼티 값 셋팅하기
```xml
<?xml version="1.0" encoding="UTF-8"?>

<included>
    <!-- 로그 패턴에 색상 적용 %clr(pattern){color} -->
    <conversionRule conversionWord="clr" converterClass="org.springframework.boot.logging.logback.ColorConverter" />

    <property name="CONSOLE_LOG_PATTERN" value="${CONSOLE_LOG_PATTERN:-%clr(%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd'T'HH:mm:ss.SSSXXX}}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>
    <property name="FILE_LOG_PATTERN" value="${FILE_LOG_PATTERN:-%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd'T'HH:mm:ss.SSSXXX}} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>

</included>
```

#### 3. logback-spring-dev.xml 파일 적성
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>

    <!-- logback-properties.xml 파일 포함-->
    <include resource="logback-properties.xml"></include>
    <!-- 로그 파일 입력할 절대 경로 -->
    <property name="LOGS_ABSOLUTE_PATH" value="./logs"></property>
    <!-- 콘솔(STDOUT) -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>${CONSOLE_LOG_PATTERN}</Pattern>
        </layout>
    </appender>


    <!-- 파일(FILE) -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- log 기록할 파일 위치 설정 -->
        <file>${LOGS_ABSOLUTE_PATH}/info.log</file>
        <!--     log 기록 타입 인코딩 -->
        <encoder>
            <pattern>${FILE_LOG_PATTERN}</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- daily rollover -->
            <fileNamePattern>${LOGS_ABSOLUTE_PATH}/info.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <!-- keep 30 days' worth of history capped at 3GB total size -->
            <maxHistory>30</maxHistory>
            <maxFileSize>100MB</maxFileSize>
            <totalSizeCap>20GB</totalSizeCap>
        </rollingPolicy>
    </appender>

    <!-- 파일(FILE) -->
    <appender name="FILE_ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- log 기록할 파일 위치 설정 -->
        <file>${LOGS_ABSOLUTE_PATH}/error.log</file>
        <!-- 로깅 레벺 필터 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <!--     log 기록 타입 인코딩 -->
        <encoder>
            <pattern>${FILE_LOG_PATTERN}</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- daily rollover -->
            <fileNamePattern>${LOGS_ABSOLUTE_PATH}/error.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <!-- keep 30 days' worth of history capped at 3GB total size -->
            <maxHistory>30</maxHistory>
            <maxFileSize>100MB</maxFileSize>
            <totalSizeCap>20GB</totalSizeCap>
        </rollingPolicy>
    </appender>

    <root level="INFO">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="FILE"/>
        <appender-ref ref="FILE_ERROR"/>
    </root>
</configuration>
```



### prod 환경에 맞는 logback.xml 작성하기
- prod 환경에서는 elk 스택을 활용하여 로그 기록을 남긴다.
> **Reference**
> - [logback > setting](https://loosie.tistory.com/829)
> - [logback > properties](https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot/src/main/resources/org/springframework/boot/logging/logback/defaults.xml)
> - [logback > RollingFileAppender](https://ckddn9496.tistory.com/82)
> - [logstash > docker](https://www.elastic.co/guide/en/logstash/current/docker-config.html)