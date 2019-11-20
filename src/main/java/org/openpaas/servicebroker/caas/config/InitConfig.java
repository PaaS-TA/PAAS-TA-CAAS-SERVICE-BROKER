package org.openpaas.servicebroker.caas.config;

import org.openpaas.servicebroker.caas.service.impl.AdminTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 기본 Spring boot packages의 component 검색하고,
 * jpa사용을 위해서 @EntityScan, @EnableJpaRepositories를 추가
 *
 * @author 박혜린
 * @version 20180724
 * @since 2018.07.24
 */
@Component
public class InitConfig {

    private static final Logger logger = LoggerFactory.getLogger(InitConfig.class);


    @Autowired
    AdminTokenService adminTokenService;

    @EventListener(ApplicationReadyEvent.class)
    public List<String> createAdminToken() {
        List<String> empty = new ArrayList<>();

        logger.info("createAdminToken======================================================Start");
        adminTokenService.setContext();
        logger.info("createAdminToken======================================================End");

        return empty;
    }
}
