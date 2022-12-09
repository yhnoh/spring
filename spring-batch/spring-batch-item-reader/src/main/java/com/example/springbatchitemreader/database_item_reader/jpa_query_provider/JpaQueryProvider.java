package com.example.springbatchitemreader.database_item_reader.jpa_query_provider;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class JpaQueryProvider extends AbstractJpaQueryProvider {
    private String cityName;

    @Override
    public Query createQuery() {

        EntityManager entityManager = super.getEntityManager();
        Query query = entityManager.createQuery("select c from CustomerEntity c where c.city = :city");
        query.setParameter("city", cityName);
        return query;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cityName, "City name is required");
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
