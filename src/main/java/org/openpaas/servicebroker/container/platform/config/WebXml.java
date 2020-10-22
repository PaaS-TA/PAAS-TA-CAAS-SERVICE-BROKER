package org.openpaas.servicebroker.container.platform.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * WebXml 설정 클래스
 *
 * @author Hyerin
 * @since 2018.07.24
 * @version 20180724
 */
public class WebXml extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}