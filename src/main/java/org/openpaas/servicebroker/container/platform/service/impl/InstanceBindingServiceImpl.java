package org.openpaas.servicebroker.container.platform.service.impl;

import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.openpaas.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.ServiceInstanceBinding;
import org.openpaas.servicebroker.service.ServiceInstanceBindingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Kubernetes service broker에서는 Binding/Unbinding 동작을 수행하지는 않지만,
 * ServiceInstanceBindingService 자체는 존재해야 하므로, 더미 클래스를 작성함.
 *
 * @author Hyungu Cho
 * @since 2018.07.24
 * @version 20180724
 */
@Service
public class InstanceBindingServiceImpl implements ServiceInstanceBindingService{
    
    private static final Logger logger = LoggerFactory.getLogger(InstanceBindingServiceImpl.class);


    @Override
    public ServiceInstanceBinding createServiceInstanceBinding(
            CreateServiceInstanceBindingRequest request)
            throws ServiceInstanceBindingExistsException, ServiceBrokerException {

        logger.debug("ScmManagerServiceInstanceBindingService CLASS createServiceInstanceBinding");
        logger.debug("ServiceInstanceBinding not supported.");

        throw new ServiceBrokerException("Not Supported");

    }

    @Override
    public ServiceInstanceBinding deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request)
            throws ServiceBrokerException {
        logger.debug("ScmManagerServiceInstanceBindingService CLASS deleteServiceInstanceBinding");
        logger.debug("ServiceInstanceBinding not supported");

        throw new ServiceBrokerException("Not Supported");

    }
}
