package org.openpaas.servicebroker.kubernetes.service.impl;

import java.util.List;

import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceExistsException;
import org.openpaas.servicebroker.kubernetes.config.EnvConfig;
import org.openpaas.servicebroker.kubernetes.model.JpaServiceInstance;
import org.openpaas.servicebroker.kubernetes.repo.JpaServiceInstanceRepository;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.Plan;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.openpaas.servicebroker.service.CatalogService;
import org.openpaas.servicebroker.service.ServiceInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service Instance를 관리하기 위한 Service 클래스이다.
 *
 * @author Hyungu Cho
 * @since 2018/07/24
 * @version 20180725
 */
@Service
public class InstanceServiceImpl implements ServiceInstanceService {

	@Autowired
	private CatalogService catalog;

	@Autowired
	private JpaServiceInstanceRepository instanceRepository;

	@Autowired
	KubernetesService kubernetesService;

	@Autowired
	EnvConfig config;

	@Autowired
	AdminTokenService adminTokenService;

	private static final Logger logger = LoggerFactory.getLogger(InstanceServiceImpl.class);

	/**
	 * <p>
	 * Service Instance 생성 : request 온 내용을 바탕으로 다음의 내용을 구성
	 * </p>
	 * 
	 * <pre>
	 *     1. Request parameters에서 Org GUID, Space GUID과 선택한 Plan의 이름(혹은 GUID)를 추출
	 *     2. ServiceInstance 객체 생성 후 Request에서 추출한 데이터 사전 기입
	 *     3. KubernetesService를 통해 Namespace, ServiceAccount 생성
	 *     4. KubernetesService에서 생성된 Namespace, ServiceAccount
	 * </pre>
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public JpaServiceInstance createServiceInstance(final CreateServiceInstanceRequest request) throws ServiceInstanceExistsException, ServiceBrokerException {
		logger.info("Create Kubernetes service instance : {}", request.getServiceInstanceId());
		
		adminTokenService.checkToken();
		
		// 요청한 instance id를 이용해 해당 instance id가 있는지 확인
		JpaServiceInstance findInstance = instanceRepository.findByServiceInstanceId(request.getServiceInstanceId());
		
		JpaServiceInstance instance = (JpaServiceInstance) new JpaServiceInstance(request);
		
		if (findInstance != null) {
			if (findInstance.getServiceInstanceId().equals(instance.getServiceInstanceId())) {
				logger.info("ServiceInstance : {} OR OrgGuid : {} is exist.", request.getServiceInstanceId(), request.getOrganizationGuid());
				throw new ServiceBrokerException("ServiceInstance already exists in your organization.");
			} else {
				throw new ServiceInstanceExistsException(instance);
			}
		}
		
		if(instanceRepository.existsByOrganizationGuid(instance.getOrganizationGuid())) {
			logger.error("ServiceInstance already exists in your organization: OrganizationGuid : {}, spaceId : {}", request.getOrganizationGuid(), request.getSpaceGuid());
			throw new ServiceBrokerException("ServiceInstance already exists in your organization.");
		}

		// kubernetes에 생성된 namespace가 있는지 확인한다.
		if (existsNamespace(instance.getServiceInstanceId()))
			throw new ServiceBrokerException("Already exists namespace to given same name.");

		kubernetesService.createNamespaceUser(instance, getPlan(instance));
		instance.withDashboardUrl(config.getDashboardUrl(instance.getServiceInstanceId()));

		instanceRepository.save(instance);

		return instance;
	}

	/**
	 * Service Instance 정보를 테이블에서 가져와 ServiceInstance 객체로 반환한다.
	 * TODO : getOne으로 바꾸어도 테스트코드 돌아가는지 확인 (아마될것..?)
	 * @param serviceInstanceId
	 * @return
	 */
	@Override
	public ServiceInstance getServiceInstance(String serviceInstanceId) {
		return instanceRepository.findByServiceInstanceId(serviceInstanceId);
	}

	/**
	 * 외부에서의 요청으로 전달받은 service instance id 등을 비교하여 Service Instance ID 정보를 찾은 다음,
	 * kubernetes의 namespace의 삭제와 service instance 정보를 차례대로 삭제한다.
	 * 
	 * @param request
	 * @return
	 * @throws ServiceBrokerException
	 */
	@Override
	public ServiceInstance deleteServiceInstance(DeleteServiceInstanceRequest request) throws ServiceBrokerException {
		logger.info("Delete Kubernetes service instance : {}", request.getServiceInstanceId());
		
		adminTokenService.checkToken();
		
		//JpaServiceInstance instance = null;
		
		JpaServiceInstance instance = (JpaServiceInstance) getServiceInstance(request.getServiceInstanceId());
		
		// DB에 정보가 없을 때
		if (instance == null) {
			String spaceName = "paas-" + request.getServiceInstanceId() + "-caas";
			logger.info("instance is not in DB. check existsNamespace {}", spaceName);
			// 실제로 Namespace에도 없을 때 
			if (!existsNamespace(spaceName)) {
				logger.info("No more delete thing {}", spaceName);
				return null;
			}
			kubernetesService.deleteNamespace(spaceName);
			return null;
		}
		
		kubernetesService.deleteNamespace(instance.getCaasNamespace());
		instanceRepository.delete(instance);

		// unbind(delete binding information)는 구현하지 않기로 결정함.

		return instance;
	}

	/**
	 * Service Instance의 정보를 갱신한다. 단, Plan ID, Kubernetes의 계정의 이름(ID), Kubernetes
	 * 계정의 엑세스 토큰만 변경이 가능하다.
	 * 
	 * @param request
	 *            (update available only these; plan id, account name, account
	 *            access token)
	 * @return ServiceInstance
	 */
	@Override
	public ServiceInstance updateServiceInstance(UpdateServiceInstanceRequest request) throws ServiceBrokerException {
		logger.info("Update Kubernetes service instance : {}", request.getServiceInstanceId());
		
		adminTokenService.checkToken();
		
		JpaServiceInstance findInstance = instanceRepository.findByServiceInstanceId(request.getServiceInstanceId());
		if (null == findInstance)
			throw new ServiceBrokerException("Cannot find service instance id : " + request.getServiceInstanceId());

		JpaServiceInstance instance = new JpaServiceInstance(request);

		if (findInstance.equals(instance)) {
			String planId = instance.getPlanId();
			logger.debug("Plan ID : {}", planId);

			if (null != planId) {
				// 지정한 Plan ID가 실제 있는 Plan의 UUID가 맞는지 유효성 확인.
				logger.info("Change Plan : {} -> {}", findInstance.getPlanId(), instance.getPlanId());
				Plan oldPlan = this.getPlan(findInstance);
				Plan newPlan = this.getPlan(instance);
				if (oldPlan.getWeight() > newPlan.getWeight())
					throw new ServiceBrokerException("Cannot change lower plan. (current: " + oldPlan.getName() + " / new: " + newPlan.getName() + ")");

				findInstance.setPlanId(planId);
				kubernetesService.changeResourceQuota(findInstance.getCaasNamespace(), newPlan);
			}
			instanceRepository.save(findInstance);
		}

		return findInstance;
	}

	/**
	 * Namespace가 Kubernetes에 존재하는지 확인한다.
	 * 
	 * @param namespace
	 * @return
	 */
	private boolean existsNamespace(String namespace) {
		return kubernetesService.existsNamespace(namespace);
	}

	/**
	 * 클래스의 메소드에서 Plan 정보가 필요한 경우, Service instance의 plan id를 이용해 plan을 찾아준다.
	 * 
	 * @param instance
	 * @return Plan
	 * @throws ServiceBrokerException
	 */
	private Plan getPlan(JpaServiceInstance instance) throws ServiceBrokerException {
		logger.info("Get plan info. from Catalog service in this broker.");
		ServiceDefinition serviceDefinition = catalog.getServiceDefinition(instance.getServiceDefinitionId());
		final List<Plan> plans = serviceDefinition.getPlans();
		Plan plan = null;
		for (int size = plans.size(), i = 0; i < size; i++) {
			if (plans.get(i).getId().equals(instance.getPlanId())) {
				plan = plans.get(i);
				return plan;
			}
		}

		throw new ServiceBrokerException("Cannot find plan using plan id into service instance info.");
	}

}
