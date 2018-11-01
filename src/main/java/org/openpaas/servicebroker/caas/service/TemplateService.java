package org.openpaas.servicebroker.caas.service;

import java.util.Map;

import org.openpaas.servicebroker.caas.exception.CaasException;

/**
 * Template 파일의 변수 부분을 치환 후 내용을 완성하여, 해당 내용을 특정한 프로세스에 의해 실행하거나
 * 해당 내용 자체를 반환하는 클래스에 대한 인터페이스.
 * @author hyerin
 * @since 2018/07/24
 * @version 20180730
 */
public interface TemplateService {

    /**
     * Template 내용 중 일부를 입력받은 모델의 내용으로 치환하여 반환하는 클래스
     * @param templateName
     * @param model
     * @return
     * @throws CaasException
     */
    public String convert(String templateName, Map<String, Object> model) throws CaasException;
    
}
