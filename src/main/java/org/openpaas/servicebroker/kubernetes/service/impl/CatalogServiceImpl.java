package org.openpaas.servicebroker.kubernetes.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.openpaas.servicebroker.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Catalog 사용을 위한 서비스 클래스
 * @author Hyungu Cho
 * @since 2018.07.24
 * @version 20180724
 */
@Service
public class CatalogServiceImpl implements CatalogService {
	
	private Catalog catalog;
	private Map<String,ServiceDefinition> serviceDefs = new HashMap<String,ServiceDefinition>();
	
	@Autowired
	public CatalogServiceImpl(Catalog catalog) {
		this.catalog = catalog;
		initializeMap();
	}

	/**
	 * 카탈로그에 대한 Service definition 정보를 내부의 map에 추가함.
	 */
	private void initializeMap() {
		for (ServiceDefinition def: catalog.getServiceDefinitions()) {
			serviceDefs.put(def.getId(), def);
		}

	}

	@Override
	public Catalog getCatalog() throws ServiceBrokerException {
		return catalog;
	}

	@Override
	public ServiceDefinition getServiceDefinition(String serviceId) throws ServiceBrokerException {
		return serviceDefs.get(serviceId);
	}
}
