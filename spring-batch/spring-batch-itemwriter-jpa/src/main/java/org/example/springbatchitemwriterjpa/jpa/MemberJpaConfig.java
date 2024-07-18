package org.example.springbatchitemwriterjpa.jpa;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static org.example.springbatchitemwriterjpa.DataSourceConfig.MEMBER_DATA_SOURCE_BEAN_NAME;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = MemberJpaConfig.ENTITY_MANAGER_FACTORY_BEAN_NAME,
        transactionManagerRef = MemberJpaConfig.JPA_TRANSACTION_MANAGER_BEAN_NAME,
        basePackages = MemberJpaConfig.PACKAGE_TO_SCAN)
public class MemberJpaConfig {

    public static final String JPA_VENDOR_ADAPTER_BEAN_NAME = "memberJpaVendorAdapter";
    public static final String ENTITY_MANAGER_FACTORY_BEAN_NAME = "memberEntityManagerFactory";
    public static final String JPA_TRANSACTION_MANAGER_BEAN_NAME = "memberJpaTransactionManager";
    public static final String PACKAGE_TO_SCAN = "";

    private final JpaConfigFactory jpaConfigFactory;

    public MemberJpaConfig(@Qualifier(MEMBER_DATA_SOURCE_BEAN_NAME) DataSource dataSource, JpaProperties jpaProperties) {
        this.jpaConfigFactory = new JpaConfigFactory(dataSource, jpaProperties, new String[]{PACKAGE_TO_SCAN});
    }

    @Primary
    @Bean(name = JPA_VENDOR_ADAPTER_BEAN_NAME)
    public JpaVendorAdapter jpaVendorAdapter() {
        return jpaConfigFactory.createJpaVendorAdapter();
    }


    @Primary
    @Bean(name = ENTITY_MANAGER_FACTORY_BEAN_NAME)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier(JPA_VENDOR_ADAPTER_BEAN_NAME) JpaVendorAdapter jpaVendorAdapter) {
        return jpaConfigFactory.createEntityManagerFactory(jpaVendorAdapter);
    }


    @Primary
    @Bean(name = JPA_TRANSACTION_MANAGER_BEAN_NAME)
    public PlatformTransactionManager transactionManager(@Qualifier(ENTITY_MANAGER_FACTORY_BEAN_NAME) EntityManagerFactory entityManagerFactory) {
        return jpaConfigFactory.createTransactionManager(entityManagerFactory);
    }


}
