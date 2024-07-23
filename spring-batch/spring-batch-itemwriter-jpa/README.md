### 1. Spring Batch Multi DataSource
- Spring Batch Job을 실행하는 도중에 여러 데이터베이스에 연결하여 Job을 처리해야하는 경우가 발생하였다. 
  - 마이크로서비스에서 데이터베이스를 서비스별로 잘 나누어 두었다면 상관없겠지만 모든 회사가 그렇게 대응할 수 없는 것도 현실이다.
- 위의 기능을 수행하기 위한 필요 사항
  - Multi DataSource 설정
  - Multi DataSoruce에 따른 JPA 설정
  - ItemWriter에 여러 ItemWriter 구성


#### 1. MultiDataSource 설정
```java
@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    public static final String MEMBER_DATA_SOURCE_BEAN_NAME = "memberDataSource";
    public static final String ORDER_DATA_SOURCE_BEAN_NAME = "orderDataSource";
    
    //...

    private HikariDataSource createDataSource(DataSourceProperties hikariDataSourceProperties,
            JdbcConnectionProperties memberJdbcConnectionProperties) {
        HikariDataSource dataSource =
                DataSourceBuilder.create(hikariDataSourceProperties.getClassLoader()).type(HikariDataSource.class)
                        .driverClassName(memberJdbcConnectionProperties.getDriverClassName())
                        .url(memberJdbcConnectionProperties.getUrl())
                        .username(memberJdbcConnectionProperties.getUsername())
                        .password(memberJdbcConnectionProperties.getPassword()).build();

        dataSource.setPoolName(memberJdbcConnectionProperties.getHikari().getPoolName());
        return dataSource;
    }    
}
```
[자세한 코드](./src/main/java/org/example/springbatchitemwriterjpa/jpa/DataSourceConfig.java)
- DataSoruce는 HikariDataSource를 사용하고 해당 객체를 생성하기 위해서는 `createDataSoruce` 메서드를 만들었다.
  - `DataSourceConfiguration` 클래스를 참고하여 DataStocue 생성 메서드 작성
- `createDataSoruce` 메서드를 이용하여 회원과 주문 데이터소스를 생성하고, @Primary는 회원 데이터 소스에 설정해두었다.
    > Spring Batch Meta Data 스키마를 셋팅하기 위해서는 데이터베이스 하나가 필요하므로, Multi DataSoruce 설정시에는 @Primary를 통해서 어떤 데이터베이스에 Spring Batch Meta Data를 설정할지를 알려주어야한다.
-  
