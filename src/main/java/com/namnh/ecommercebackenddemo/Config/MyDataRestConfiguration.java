package com.namnh.ecommercebackenddemo.Config;

import com.namnh.ecommercebackenddemo.Entity.Product;
import com.namnh.ecommercebackenddemo.Entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.*;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfiguration implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfiguration(EntityManager theEntityManager){
        entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] theUnAllowedActions = {HttpMethod.PUT,HttpMethod.POST,HttpMethod.DELETE};

        //disable HTTP METHOD for Products: PUT,POST & DELETE
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnAllowedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnAllowedActions));

        //same goes for ProductCategory
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnAllowedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnAllowedActions));

        //call an internal helper method
        exposedIds(config);
    }

    private void exposedIds(RepositoryRestConfiguration config) {
        //expose entity ids

        //get a list of all entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        //create an array list of the entity types
        List<Class> entityClasses = new ArrayList<>();

        //get the entity type for the entity
        for(EntityType tempEntityType : entities){
            entityClasses.add(tempEntityType.getJavaType());
        }
        //expose the entity ids for the array of the entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);

    }
}
