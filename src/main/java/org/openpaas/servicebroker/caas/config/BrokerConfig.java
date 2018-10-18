package org.openpaas.servicebroker.caas.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * 기본 Spring boot packages의 component 검색하고,
 * jpa사용을 위해서 @EntityScan, @EnableJpaRepositories를 추가
 *
 * @author 박혜린
 * @since 2018.07.24
 * @version 20180724
 */
@Configuration
@ComponentScan(basePackages = { "org.openpaas.servicebroker" })
@EnableJpaRepositories("org.openpaas.servicebroker.caas.repo")
@EntityScan(value = "org.openpaas.servicebroker.caas.model")
public class BrokerConfig {

    @Bean
    public PropertyPlaceholderConfigurer properties() {
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[] {
            new ClassPathResource("application.yml")
        };
        propertyPlaceholderConfigurer.setLocations(resources);
        propertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        return propertyPlaceholderConfigurer;
    }
}
