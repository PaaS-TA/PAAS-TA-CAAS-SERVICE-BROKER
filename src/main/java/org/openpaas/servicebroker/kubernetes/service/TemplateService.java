package org.openpaas.servicebroker.kubernetes.service;

import java.util.Map;

import org.openpaas.servicebroker.kubernetes.exception.KubernetesServiceException;

public interface TemplateService {

	public boolean execute(String templateName, Map<String, Object> model) throws KubernetesServiceException;
	
	public String convert(String templateName, Map<String, Object> model) throws KubernetesServiceException;
	
}
