package org.example.springbatchitemwriterjpa.jpa;


import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    public static final String MEMBER_DATA_SOURCE_BEAN_NAME = "memberDataSource";
    public static final String ORDER_DATA_SOURCE_BEAN_NAME = "orderDataSource";


    @Bean
    @ConfigurationProperties(prefix = "datasource.member")
    public JdbcConnectionProperties memberJdbcConnectionProperties() {
        return new JdbcConnectionProperties();
    }


    @Bean
    @ConfigurationProperties(prefix = "datasource.order")
    public JdbcConnectionProperties orderJdbcConnectionProperties() {
        return new JdbcConnectionProperties();
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSourceProperties hikariDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean
    @Primary
    public DataSource memberDataSource(DataSourceProperties hikariDataSourceProperties,
            JdbcConnectionProperties memberJdbcConnectionProperties) {

        HikariDataSource dataSource = this.createDataSource(hikariDataSourceProperties, memberJdbcConnectionProperties);
        return dataSource;
    }


    @Bean
    public DataSource orderDataSource(DataSourceProperties hikariDataSourceProperties,
            JdbcConnectionProperties orderJdbcConnectionProperties) {
        return this.createDataSource(hikariDataSourceProperties, orderJdbcConnectionProperties);
    }


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
