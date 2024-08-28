package org.example.springbatchmultidatasource.jpa;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;
import static org.example.springbatchmultidatasource.jpa.DataSourceConfig.ORDER_DATA_SOURCE_BEAN_NAME;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = OrderJpaConfig.ENTITY_MANAGER_FACTORY_BEAN_NAME, transactionManagerRef = OrderJpaConfig.JPA_TRANSACTION_MANAGER_BEAN_NAME, basePackages = OrderJpaConfig.PACKAGE_TO_SCAN)
public class OrderJpaConfig {

    public static final String JPA_VENDOR_ADAPTER_BEAN_NAME = "orderJpaVendorAdapter";
    public static final String ENTITY_MANAGER_FACTORY_BEAN_NAME = "orderEntityManagerFactory";
    public static final String JPA_TRANSACTION_MANAGER_BEAN_NAME = "orderJpaTransactionManager";
    public static final String PACKAGE_TO_SCAN = "org.example.springbatchmultidatasource.jpa.order";

    private final JpaConfigFactory jpaConfigFactory;


    public OrderJpaConfig(@Qualifier(ORDER_DATA_SOURCE_BEAN_NAME) DataSource dataSource, JpaProperties jpaProperties) {
        this.jpaConfigFactory = new JpaConfigFactory(dataSource, jpaProperties, new String[] {PACKAGE_TO_SCAN});
    }


    @Bean(name = JPA_VENDOR_ADAPTER_BEAN_NAME)
    public JpaVendorAdapter jpaVendorAdapter() {
        return jpaConfigFactory.createJpaVendorAdapter();
    }


    @Bean(name = ENTITY_MANAGER_FACTORY_BEAN_NAME)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier(JPA_VENDOR_ADAPTER_BEAN_NAME) JpaVendorAdapter jpaVendorAdapter) {
        return jpaConfigFactory.createEntityManagerFactory(jpaVendorAdapter);
    }


    @Bean(name = JPA_TRANSACTION_MANAGER_BEAN_NAME)
    public PlatformTransactionManager transactionManager(
            @Qualifier(ENTITY_MANAGER_FACTORY_BEAN_NAME) EntityManagerFactory entityManagerFactory) {
        return jpaConfigFactory.createTransactionManager(entityManagerFactory);
    }


}
