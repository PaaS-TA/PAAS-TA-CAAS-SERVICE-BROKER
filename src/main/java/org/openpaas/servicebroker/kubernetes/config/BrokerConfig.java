package org.openpaas.servicebroker.kubernetes.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.sql.DataSource;


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
@EnableJpaRepositories("org.openpaas.servicebroker.kubernetes.repo")
@EntityScan(value = "org.openpaas.servicebroker.kubernetes.model")
public class BrokerConfig {
	private static final Logger logger = LoggerFactory.getLogger( BrokerConfig.class );

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
