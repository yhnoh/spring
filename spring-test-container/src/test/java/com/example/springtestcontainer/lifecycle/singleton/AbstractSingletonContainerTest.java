package com.example.springtestcontainer.lifecycle.singleton;

import org.testcontainers.containers.MariaDBContainer;

//https://www.testcontainers.org/test_framework_integration/junit_5/
public class AbstractSingletonContainerTest {


    //여러 테스트 클래스에 대해 한 번만 시작되는 컨테이너를 정의하는 것이 유용
    //Testcontainers 확장에서 제공하는 이 사용 사례에 대한 특별한 지원은 없습니다. 대신 다음 패턴을 사용하여 구현할 수 있습니다.
    //모든 상속 테스트 클래스에서 컨테이너를 사용할 수 있습니다
    static final MariaDBContainer MARIA_DB_CONTAINER;
    static {
        MARIA_DB_CONTAINER = new MariaDBContainer("mariadb:10.3.8");
        MARIA_DB_CONTAINER.start();
    }
}
