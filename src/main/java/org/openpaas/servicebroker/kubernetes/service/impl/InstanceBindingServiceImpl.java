package org.openpaas.servicebroker.kubernetes.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.openpaas.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.ServiceInstanceBinding;
import org.openpaas.servicebroker.service.ServiceInstanceBindingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

	@Autowired
	private Environment env;
	
	@Autowired
	private InstanceServiceImpl instanceService;

	private static final String DUMMY_BINDING_ID = "bb5b5ecc-1389-4562-b922-8c616fc76e8c";
	private static ServiceInstanceBinding DUMMY_BINDING_VARIABLE = null;

	static String username;
	static String password;

	/*
	* ServiceInstance Binding(create)
	*
	* */

	public ServiceInstanceBinding createServiceInstanceBinding(CreateServiceInstanceBindingRequest request)
			throws ServiceInstanceBindingExistsException, ServiceBrokerException {
		logger.info("OracleServiceInstanceBindingService CLASS createServiceInstanceBinding");

		// 최초 ServiceInstanceBinding 생성 요청시에는 해당 ServiceInstanceBinding 가 존재하지 않습니다.
		ServiceInstanceBinding findBinding =
			getServiceInstanceBinding(request.getBindingId(), request.getServiceInstanceId(), request.getAppGuid());

		return findBinding;
	}

	/**
	 * Binding을 사용하지는 않지만 메소드 자체는 존재해야하므로, dummy 값을 생성한 후 해당 값을 반환해줌.
	 * @param bindingId
	 * @param serviceInstanceId
	 * @param appGuid
	 * @return
	 */
	private ServiceInstanceBinding getServiceInstanceBinding(
		String bindingId, String serviceInstanceId, String appGuid) {
		if (null == DUMMY_BINDING_VARIABLE) {
			DUMMY_BINDING_VARIABLE = new ServiceInstanceBinding(
				DUMMY_BINDING_ID, serviceInstanceId, null, null, appGuid);
		}

		return DUMMY_BINDING_VARIABLE;
	}

	@Override
	public ServiceInstanceBinding deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request)
			throws ServiceBrokerException {

		String bindingId = request.getBindingId();

		// ServiceInstanceBinding 정보를 조회합니다.
		ServiceInstanceBinding binding = getServiceInstanceBinding(
			request.getBindingId(), request.getInstance().getServiceInstanceId(), null);

		DUMMY_BINDING_VARIABLE = null;

		return binding;
	}
}
