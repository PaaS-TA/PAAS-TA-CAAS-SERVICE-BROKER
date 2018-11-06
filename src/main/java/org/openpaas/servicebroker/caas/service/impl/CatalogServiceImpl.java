package org.openpaas.servicebroker.caas.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.openpaas.servicebroker.service.CatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private static final Logger logger = LoggerFactory.getLogger(CatalogServiceImpl.class);
    
    @Autowired
    public CatalogServiceImpl(Catalog catalog) {
    	logger.info("--------------  CatalogServiceImpl  --------------");
        this.catalog = catalog;
        logger.info("--------------  CatalogServiceImpl call initializeMap() --------------");
        initializeMap();
    }

    /**
     * 카탈로그에 대한 Service definition 정보를 내부의 map에 추가함.
     */
    private void initializeMap() {
    	logger.info("--------------  initializeMap  --------------");
        for (ServiceDefinition def: catalog.getServiceDefinitions()) {
        	logger.info("--------------  initializeMap for() -------------- {}", def);
            serviceDefs.put(def.getId(), def);
        }

    }

    @Override
    public Catalog getCatalog() throws ServiceBrokerException {
    	logger.info("--------------  getCatalog  -------------- {}", catalog);
        return catalog;
    }

    @Override
    public ServiceDefinition getServiceDefinition(String serviceId) throws ServiceBrokerException {
    	logger.info("--------------  getServiceDefinition  -------------- {}", serviceDefs.get(serviceId));
        return serviceDefs.get(serviceId);
    }
}
