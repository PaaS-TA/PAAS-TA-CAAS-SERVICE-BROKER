package org.openpaas.servicebroker.kubernetes.service.impl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceExistsException;
import org.openpaas.servicebroker.kubernetes.config.EnvConfig;
import org.openpaas.servicebroker.kubernetes.model.JpaServiceInstance;
import org.openpaas.servicebroker.kubernetes.repo.JpaServiceInstanceRepository;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.Plan;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.openpaas.servicebroker.service.CatalogService;
import org.openpaas.servicebroker.service.ServiceInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    private final ObjectMapper jsonMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger( InstanceServiceImpl.class );

    /**
     * <p>Service Instance 생성 : request 온 내용을 바탕으로 다음의 내용을 구성</p>
     * <pre>
     *     1. Request parameters에서 Org GUID, Space GUID과 선택한 Plan의 이름(혹은 GUID)를 추출
     *     2. ServiceInstance 객체 생성 후 Request에서 추출한 데이터 사전 기입
     *     3. KubernetesAdminService를 통해 Namespace, ServiceAccount 생성
     *     4. KubernetesAdminService에서 생성된 Namespace, ServiceAccount
     * </pre>
     * @param request
     * @return
     */
    @Override
    public JpaServiceInstance createServiceInstance ( final CreateServiceInstanceRequest request )
        throws ServiceInstanceExistsException, ServiceBrokerException {
        logger.info( "Create Kubernetes service instance : {}", request.getServiceInstanceId() );
        // 요청한 instance id를 이용해 해당 instance id가 있는지 확인
        JpaServiceInstance findInstance = instanceRepository.findByServiceInstanceId(request.getServiceInstanceId());

        JpaServiceInstance instance = (JpaServiceInstance) new JpaServiceInstance(request);

        if (findInstance != null) {
            if (findInstance.equals( instance )) {
                findInstance.setHttpStatusOK();
                return findInstance;
            } else {
                throw new ServiceInstanceExistsException( instance );
            }
        }

        // kubernetes에 생성된 namespace가 있는지 확인한다.
        if ( existsNamespace( instance.getKubernetesNamespace() ) )
            throw new ServiceBrokerException( "Already exists namespace to given same name." );

        kubernetesService.createNamespaceUser(instance, getPlan(instance));
        instance.withDashboardUrl(getDashboardUrl(instance.getKubernetesNamespace(), instance.getKubernetesAccountAccessToken()));
        
        this.save(instance);

        return instance;
    }

    /**
     * Service Instance 정보를 테이블에서 가져와 ServiceInstance 객체로 반환한다.
     * @param serviceInstanceId
     * @return
     */
    @Override
    public ServiceInstance getServiceInstance ( String serviceInstanceId ) {
        return instanceRepository.getOne( serviceInstanceId );
    }

    /**
     * 외부에서의 요청으로 전달받은 service instance id 등을 비교하여 Service Instance ID 정보를
     * 찾은 다음, kubernetes의 namespace의 삭제와 service instance 정보를 차례대로 삭제한다.
     * @param request
     * @return
     * @throws ServiceBrokerException
     */
    @Override
    public ServiceInstance deleteServiceInstance ( DeleteServiceInstanceRequest request ) throws ServiceBrokerException {
        logger.info( "Delete Kubernetes service instance : {}", request.getServiceInstanceId() );
        JpaServiceInstance instance = (JpaServiceInstance) getServiceInstance( request.getServiceInstanceId() );
        if ( instance == null )
            return null;

        if (existsNamespace( instance.getKubernetesNamespace() )) {
            //kubernetesAdminServiceImpl.deleteNamespace( instance.getKubernetesNamespace() );
        	kubernetesService.deleteNamespace(instance.getKubernetesNamespace());
            this.delete( instance );
        }

        // unbind(delete binding information)는 구현하지 않기로 결정함.

        return instance;
    }

    /**
     * Service Instance의 정보를 갱신한다. 단, Plan ID, Kubernetes의 계정의 이름(ID),
     * Kubernetes 계정의 엑세스 토큰만 변경이 가능하다.
     * @param request (update available only these; plan id, account name, account access token)
     * @return ServiceInstance
     */
    @Override
    public ServiceInstance updateServiceInstance ( UpdateServiceInstanceRequest request ) throws ServiceBrokerException {
        JpaServiceInstance findInstance = instanceRepository.findByServiceInstanceId( request.getServiceInstanceId() );
        if (null == findInstance)
            throw new ServiceBrokerException( "Cannot find service instance id : " + request.getServiceInstanceId() );

        JpaServiceInstance instance = new JpaServiceInstance( request );

        if (findInstance.equals( instance )) {
            String planId = instance.getPlanId();
            logger.debug( "Plan ID : {}", planId);

            if (null != planId) {
                // 지정한 Plan ID가 실제 있는 Plan의 UUID가 맞는지 유효성 확인.
                logger.info( "Change Plan : {} -> {}", findInstance.getPlanId(), instance.getPlanId() );
                Plan oldPlan = this.getPlan( findInstance );
                Plan newPlan = this.getPlan( instance );
                if (oldPlan.getWeight() > newPlan.getWeight())
                    throw new ServiceBrokerException( "Cannot change lower plan. (current: " + oldPlan.getName() + " / new: " + newPlan.getName() + ")" );

                findInstance.setPlanId( planId );
                kubernetesService.changeResourceQuota(findInstance.getKubernetesNamespace(), newPlan);
            }

            this.save( findInstance );
        }

        return findInstance;
    }

    /**
     * Act only to save service instance data.
     * @param instance
     */
    public void save(JpaServiceInstance instance) {
        try {
            String jsonValue = jsonMapper.writeValueAsString( instance );
            logger.debug( "Save instance : {}", jsonValue );
        } catch (JsonProcessingException jpe) {
            logger.error( "Json write error.", jpe );
        }

        instanceRepository.save( instance );
    }

    /**
     * Act only to delete service instance data.
     * @param instance
     */
    public void delete(JpaServiceInstance instance) {
        try {
            logger.debug( "Delete instance : {}", jsonMapper.writeValueAsString( instance ) );
        } catch (JsonProcessingException jpe) {
            logger.error( "Json write error.", jpe );
        }

        instanceRepository.delete( instance );
    }

    // DashboardUrl 생성
    public String getDashboardUrl(String spaceName, String tokenName){
        return config.getDashboardUrl()+spaceName+"/token/"+tokenName;
    }

    /**
     * Namespace가 DB와 Kubernetes 양쪽에 모두 존재하는지 확인한다.
     * @param namespace
     * @return
     */
    public boolean existsNamespace(String namespace) {
        return kubernetesService.existsNamespace( namespace )
            || instanceRepository.existsByKubernetesNamespace( namespace );
    }

    /**
     * UUID를 생성한다. (현재는 쓰지 않는 메소드)
     * @param id
     * @return
     */
    @Deprecated
    public String generateUUID ( String id ) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            // TODO: handle exception
        }
        digest.update(id.getBytes());
        String uuid =
            new BigInteger(1, digest.digest()).toString(16).replaceAll("/[^a-zA-Z]+/", "").substring(0, 16);
        return uuid;
    }

    private Plan getPlan(JpaServiceInstance instance) throws ServiceBrokerException {
        logger.info("Get plan info. from Catalog service in this broker.");
        final List<Plan> plans =
            catalog.getServiceDefinition( instance.getServiceDefinitionId() ).getPlans();
        Plan plan = null;
        for (int size = plans.size(), i = 0; i < size; i++) {
            if ( plans.get( i ).getId().equals( instance.getPlanId() ) ) {
                plan = plans.get( i );
                break;
            }
        }

        if (null == plan)
            throw new ServiceBrokerException( "Cannot find plan using plan id into service instance info." );

        return plan;
    }

}
