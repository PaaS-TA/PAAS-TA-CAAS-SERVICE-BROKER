package org.openpaas.servicebroker.kubernetes.service.impl;

import java.util.Map;

import org.openpaas.servicebroker.kubernetes.exception.KubernetesServiceException;
import org.openpaas.servicebroker.kubernetes.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;

/**
 * TemplateService 인터페이스에 대한 CaaS Service broker에서의 구현체.
 * @author hyerin
 * @since 2018/07/24
 * @version 20180801
 */
@Service
public class TemplateServiceImpl implements TemplateService {
    
    private static final Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);

    private Configuration configuration;

    /**
     * Template 내용 중 일부를 입력받은 모델의 내용으로 치환하여 YAML 문자열을 반환하는 클래스.
     * 내부적으로는 FreeMarker를 사용함.
     * @param templateName
     * @param model
     * @return
     * @throws KubernetesServiceException
     */
    @Override
    public String convert(String templateName, Map<String, Object> model) throws KubernetesServiceException {
        String yml;
        try {
            yml = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate(templateName), model);
            logger.info("original yml {}",yml);
        } catch (Exception e) {
            logger.error( "Occured unexpected exception...", e );
            return null;
        }
        return yml;
    }
}
