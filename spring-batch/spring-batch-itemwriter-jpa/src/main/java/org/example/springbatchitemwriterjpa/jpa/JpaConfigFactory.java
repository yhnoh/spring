package org.example.springbatchitemwriterjpa.jpa;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@RequiredArgsConstructor
public class JpaConfigFactory {

    private final DataSource dataSource;
    private final JpaProperties jpaProperties;
    private final String[] packagesToScan;

    public JpaVendorAdapter createJpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(this.jpaProperties.isShowSql());
        if (this.jpaProperties.getDatabase() != null) {
            hibernateJpaVendorAdapter.setDatabase(this.jpaProperties.getDatabase());
        }
        if (this.jpaProperties.getDatabasePlatform() != null) {
            hibernateJpaVendorAdapter.setDatabasePlatform(this.jpaProperties.getDatabasePlatform());
        }
        hibernateJpaVendorAdapter.setGenerateDdl(this.jpaProperties.isGenerateDdl());
        return hibernateJpaVendorAdapter;
    }

    public LocalContainerEntityManagerFactoryBean createEntityManagerFactory(JpaVendorAdapter jpaVendorAdapter) {

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan(packagesToScan);
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setJpaPropertyMap(jpaProperties.getProperties());
        return entityManagerFactory;
    }

    public PlatformTransactionManager createTransactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }


}
