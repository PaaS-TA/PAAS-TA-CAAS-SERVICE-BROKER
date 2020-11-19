package org.openpaas.servicebroker.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openpaas.servicebroker.container.platform.model.JpaServiceInstance;
import org.openpaas.servicebroker.container.platform.model.User;
import org.openpaas.servicebroker.container.platform.repo.JpaServiceInstanceRepository;
import org.openpaas.servicebroker.container.platform.service.PropertyService;
import org.openpaas.servicebroker.container.platform.service.RestTemplateService;
import org.openpaas.servicebroker.container.platform.service.impl.CatalogServiceImpl;
import org.openpaas.servicebroker.container.platform.service.impl.InstanceServiceImpl;
import org.openpaas.servicebroker.container.platform.service.impl.ContainerPlatformService;
import org.openpaas.servicebroker.container.platform.service.impl.UserService;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceExistsException;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.Plan;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.openpaas.servicebroker.model.fixture.PlanFixture;
import org.openpaas.servicebroker.model.fixture.RequestFixture;
import org.openpaas.servicebroker.model.fixture.ServiceFixture;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpStatusCodeException;


@RunWith(MockitoJUnitRunner.class)
public class InstanceServiceImplTest {
    
    private static final Logger logger = LoggerFactory.getLogger(InstanceServiceImplTest.class);
    
    @Mock
    private CatalogServiceImpl catalog;

    @Mock
    private JpaServiceInstanceRepository instanceRepository;

    @Mock
    ContainerPlatformService caasService;
    
    @Mock
    PropertyService propertyService;
    
    @Mock
    UserService userService;
    
    @Mock
    RestTemplateService restTemplateService;
    
    @InjectMocks
    InstanceServiceImpl serviceInstance;
    
    public static JpaServiceInstance jpaServiceInstance;
    
    public static JpaServiceInstance jpaServiceInstanceDef;

    private CreateServiceInstanceRequest request;
    private static DeleteServiceInstanceRequest delRequest;
    private static UpdateServiceInstanceRequest upRequest;
    private static Plan plan;
    private static User user;
    
    @Before
    public void setUp() throws Exception {
        
        request = RequestFixture.getCreateServiceInstanceRequest();
        delRequest = RequestFixture.getDeleteServiceInstanceRequest2();
        upRequest = RequestFixture.getUpdateServiceInstanceRequest();
        
        
        jpaServiceInstance = new JpaServiceInstance(request);
        jpaServiceInstance.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        jpaServiceInstance.setCaasAccountTokenName(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN);
        jpaServiceInstance.setCaasAccountName(TestConstants.JPA_CAAS_ACCOUNT_NAME);
        jpaServiceInstance.setCaasNamespace(TestConstants.JPA_CAAS_NAMESPACE);
        jpaServiceInstance.setUserId(TestConstants.PARAM_KEY_OWNER_VALUE);
        Map<String,Object> jpaMap = new HashMap<>();
        jpaMap.put(TestConstants.PARAM_KEY_OWNER, TestConstants.PARAM_KEY_OWNER_VALUE);
        jpaServiceInstance.setParameters(jpaMap);
        
        jpaServiceInstanceDef = new JpaServiceInstance(RequestFixture.getCreateServiceInstanceRequest2());
        
        plan = new Plan("test", "Micro", "Test-desc");
        
    }
    
    /**
     * 잘 만들어진 것?!
     * @throws ServiceBrokerException 
     * @throws ServiceInstanceExistsException 
     * */ 
    @Test
    public void testCreateServiceInstance() throws ServiceInstanceExistsException, ServiceBrokerException {
        
        // 값을 세팅한다.
        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        
        when(instanceRepository.findByServiceInstanceId(TestConstants.SV_INSTANCE_ID_001)).thenReturn(null);
        when(caasService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
        when(catalog.getServiceDefinition(jpaServiceInstance.getServiceDefinitionId())).thenReturn(ServiceFixture.getService());
        when(caasService.createNamespaceUser(jpaServiceInstance, PlanFixture.getPlanOne())).thenReturn(jpaServiceInstance);
        when(instanceRepository.save(jpaServiceInstance)).thenReturn(jpaServiceInstance);
        doNothing().when(userService).request(jpaServiceInstance, HttpMethod.POST);
        //when(propertyService.getDashboardUrl(jpaServiceInstance.getServiceInstanceId())).thenReturn(TestConstants.DASHBOARD_URL);
        //when(inspectionProjectService.deleteProject(gTestResultJobModel)).thenThrow(Exception.class);
        
        // 실제 코드를 호출한다.
        JpaServiceInstance result = serviceInstance.createServiceInstance(request);
        
        // 값을 비교해야함.        
        
    }
    
//    /**
//     * kubernetes namespace잘 만들고, DB 저장이 실패할 경우
//     * @throws ServiceBrokerException 
//     * @throws ServiceInstanceExistsException 
//     * */ 
//    @Test(expected=HttpStatusCodeException.class)
//    public void testCreateServiceInstanceFailSave() throws ServiceInstanceExistsException, ServiceBrokerException, HttpStatusCodeException {
//        
//        // 값을 세팅한다.
//        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
//        
//        when(instanceRepository.findByServiceInstanceId(TestConstants.SV_INSTANCE_ID_001)).thenReturn(null);
//        when(caasService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
//        when(catalog.getServiceDefinition(jpaServiceInstance.getServiceDefinitionId())).thenReturn(ServiceFixture.getService());
//        when(caasService.createNamespaceUser(jpaServiceInstance, PlanFixture.getPlanOne())).thenReturn(jpaServiceInstance);
//        when(instanceRepository.save(jpaServiceInstance)).thenReturn(jpaServiceInstance);
//        doNothing().when(userService).request(jpaServiceInstance, plan, HttpMethod.POST);
//        when(userService.convert(jpaServiceInstance, plan)).thenReturn(user);
//        doThrow(HttpStatusCodeException.class).when(restTemplateService).requestUser(user, HttpMethod.POST);
////        doThrow(HttpStatusCodeException.class).when(userService).request(jpaServiceInstance, plan, HttpMethod.POST); //HttpStatusCodeException
//        //userService.request(instance, getPlan(instance), HttpMethod.POST);
//        // 실제 코드를 호출한다.
//        JpaServiceInstance result = serviceInstance.createServiceInstance(request);
//        
//        // 값을 비교해야함.        
//        
//    }
    
    /**
     * kubernetes namespace잘 만들고, DB 저장성공 후 common DB에 저장 실패한 경우
     * @throws ServiceBrokerException 
     * @throws ServiceInstanceExistsException 
     * */ 
    @Test
    public void testCreateServiceInstanceFailSend() throws ServiceInstanceExistsException, ServiceBrokerException {
        
        // 값을 세팅한다.
        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        
        when(instanceRepository.findByServiceInstanceId(TestConstants.SV_INSTANCE_ID_001)).thenReturn(null);
        when(caasService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
        when(catalog.getServiceDefinition(jpaServiceInstance.getServiceDefinitionId())).thenReturn(ServiceFixture.getService());
        when(caasService.createNamespaceUser(jpaServiceInstance, PlanFixture.getPlanOne())).thenReturn(jpaServiceInstance);
        when(instanceRepository.save(jpaServiceInstance)).thenReturn(jpaServiceInstance);
        doNothing().when(userService).request(jpaServiceInstance, HttpMethod.POST);
        //when(inspectionProjectService.deleteProject(gTestResultJobModel)).thenThrow(Exception.class);
        
        // 실제 코드를 호출한다.
        JpaServiceInstance result = serviceInstance.createServiceInstance(request);
        
        // 값을 비교해야함.        
        
    }
    
    
    
    /**
     * instanceService의
     * if ( findInstance == null ) 이지만
     * namespace가 실제로 존재할 때  통과
     * @throws ServiceBrokerException 
     * @throws ServiceInstanceExistsException 
     * */ 
    @Test(expected=ServiceBrokerException.class)
    public void testCreateServiceInstanceFindInstanceNullExistNamespace() throws ServiceInstanceExistsException, ServiceBrokerException {
        
        // 값을 세팅한다.
        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        
        when(instanceRepository.findByServiceInstanceId(TestConstants.SV_INSTANCE_ID_001)).thenReturn(null);
        when(caasService.existsNamespace(TestConstants.SV_INSTANCE_ID_001)).thenReturn(true);
        
        // 실제 코드를 호출한다.
        serviceInstance.createServiceInstance(request);
        
        // 익셉션 처리를 해야한다.!        
        
    }
    
    /**
     * instanceService의
     * if ( findInstance == null ) 이지만
     * org guid가 이미 존재 
     * @throws ServiceBrokerException 
     * @throws ServiceInstanceExistsException 
     * */ 
    @Test(expected=ServiceBrokerException.class)
    public void testCreateServiceInstanceFindInstanceNullExistOrg() throws ServiceInstanceExistsException, ServiceBrokerException {
        
        // 값을 세팅한다.
        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        
        when(instanceRepository.findByServiceInstanceId(TestConstants.SV_INSTANCE_ID_001)).thenReturn(null);
        when(instanceRepository.existsByOrganizationGuid(TestConstants.ORG_GUID_001)).thenReturn(true);
        
        // 실제 코드를 호출한다.
        serviceInstance.createServiceInstance(request);
        
        // 익셉션 처리를 해야한다.!        
        
    }
    
    /**
     * instanceService의
     * if ( findInstance != null ) 이 true일 때
     * findInstance 랑 instance 값이 싹다 같을 때
     * @throws ServiceBrokerException 
     * @throws ServiceInstanceExistsException 
     * */ 
    @Test(expected=ServiceBrokerException.class)
    public void testCreateServiceInstanceFindInstanceNotNull() throws ServiceInstanceExistsException, ServiceBrokerException {
        
        // 값을 세팅한다.
        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        
        when(instanceRepository.findByServiceInstanceId(TestConstants.SV_INSTANCE_ID_001)).thenReturn(jpaServiceInstance);
        
        // 실제 코드를 호출한다.
        serviceInstance.createServiceInstance(request);
        
        // 값을 비교한다.        
        
    }
    
    /**
     * instanceService의
     * if ( findInstance != null ) 이 true일 때
     * findInstance 랑 instance 값이 다를 때 통과
     * @throws ServiceBrokerException 
     * @throws ServiceInstanceExistsException 
     * */ 
    @Test(expected=ServiceInstanceExistsException.class)
    public void testCreateServiceInstanceFindInstanceNotNullDef() throws ServiceInstanceExistsException, ServiceBrokerException {
        
        // 값을 세팅한다.
        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        jpaServiceInstanceDef.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_002);
        
        when(instanceRepository.findByServiceInstanceId(TestConstants.SV_INSTANCE_ID_001)).thenReturn(jpaServiceInstanceDef);
                
        
        // 실제 코드를 호출한다.
        serviceInstance.createServiceInstance(request);
        
        // 값을 비교한다.        
        
    }

    
    // TODO : 통과
    @Test
    public void testGetServiceInstance() {
        // 값을 세팅한다.
        when(instanceRepository.findByServiceInstanceId(request.getServiceInstanceId())).thenReturn(jpaServiceInstance);
        // 실제로 테스트할 함수를 호출한다.
        ServiceInstance instance = serviceInstance.getServiceInstance(request.getServiceInstanceId());
        //결과 값이 맞는가 본다.
        assertThat(instance).isNotNull();
        assertEquals(jpaServiceInstance, instance);
        assertEquals(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN, jpaServiceInstance.getCaasAccountTokenName());
        assertEquals(TestConstants.JPA_CAAS_ACCOUNT_NAME, jpaServiceInstance.getCaasAccountName());
        assertEquals(TestConstants.JPA_CAAS_NAMESPACE, jpaServiceInstance.getCaasNamespace());
        assertEquals(TestConstants.JPA_ORGANIZTION_GUID, jpaServiceInstance.getOrganizationGuid());
        assertEquals(TestConstants.JPA_SERVICE_DEFINITION_ID, jpaServiceInstance.getServiceDefinitionId());
        assertEquals(TestConstants.JPA_SPACE_GUID, jpaServiceInstance.getSpaceGuid());
        assertEquals(TestConstants.PARAM_KEY_OWNER_VALUE, jpaServiceInstance.getParameter(TestConstants.PARAM_KEY_OWNER));
        assertEquals(TestConstants.PARAM_KEY_OWNER_VALUE, jpaServiceInstance.getUserId());
        
    }
    
    /** 통과
     * instanceService의
     * if ( instance == null ) 이 true일 때
     * */ 
    @Test
    public void testDeleteServiceInstanceNull() throws ServiceBrokerException {
        //값을 세팅한다.
        when(instanceRepository.findByServiceInstanceId(request.getServiceInstanceId())).thenReturn(null);

        //실제로 테스트할 함수를 호출한다.
        ServiceInstance instance = serviceInstance.deleteServiceInstance(delRequest);    
        assertNull(instance);
        
    }
    
    /** 
     * instanceService의
     * if ( instance == null ) 이 true이고, 
     * if ( existsNamespace ) 가 false 일 때
     * */ 
    @Test
    public void testDeleteServiceInstanceNullExistFalse() throws ServiceBrokerException {
        //값을 세팅한다.
        when(instanceRepository.findByServiceInstanceId(request.getServiceInstanceId())).thenReturn(null);

        when(caasService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(false);
        
        //실제로 테스트할 함수를 호출한다.
        ServiceInstance instance = serviceInstance.deleteServiceInstance(delRequest);    
        assertNull(instance);
        
    }
    
    /**
     * instanceService의
     * if ( instance == null ) 이 true이고, 
     * if ( existsNamespace ) 가 true 일 때
     * */ 
    @Test
    public void testDeleteServiceInstanceNullExistTrue() throws ServiceBrokerException {
        //값을 세팅한다.
        when(instanceRepository.findByServiceInstanceId(request.getServiceInstanceId())).thenReturn(null);
        
        when(caasService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
        doNothing().when(caasService).deleteNamespace(TestConstants.JPA_CAAS_NAMESPACE);

        //실제로 테스트할 함수를 호출한다.
        ServiceInstance instance = serviceInstance.deleteServiceInstance(delRequest);    
        assertNull(instance);
        
    }
    
    /** 통과
     * instanceService의
     * if ( instance == null )의 값 false 이고
     * if (existsNamespace( instance.getContainerPlatformNamespace() ))의 existsNamespace 의 return인
     * caasService.existsNamespace( namespace ) 가 true일 때 
     * @throws ServiceBrokerException 
     * */ 
    @Test
    public void testDeleteServiceNamespaceNotNull() throws ServiceBrokerException {
        
        // 값을 세팅한다.
        request.setServiceDefinitionId(jpaServiceInstance.getServiceDefinitionId());
        when(instanceRepository.findByServiceInstanceId(TestConstants.SV_INSTANCE_ID_001)).thenReturn(jpaServiceInstance);
        when(caasService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
        
        doNothing().when(caasService).deleteNamespace(TestConstants.JPA_CAAS_NAMESPACE);
        doNothing().when(instanceRepository).delete(jpaServiceInstance);
        
        
        // 실제를 호출한다.
        serviceInstance.deleteServiceInstance(delRequest);    
        
        assertThat(jpaServiceInstance).isNotNull();
        assertEquals(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN, jpaServiceInstance.getCaasAccountTokenName());
        assertEquals(TestConstants.JPA_CAAS_ACCOUNT_NAME, jpaServiceInstance.getCaasAccountName());
        assertEquals(TestConstants.JPA_CAAS_NAMESPACE, jpaServiceInstance.getCaasNamespace());
        assertEquals(TestConstants.JPA_ORGANIZTION_GUID, jpaServiceInstance.getOrganizationGuid());
        assertEquals(TestConstants.JPA_SERVICE_DEFINITION_ID, jpaServiceInstance.getServiceDefinitionId());
        assertEquals(TestConstants.JPA_SPACE_GUID, jpaServiceInstance.getSpaceGuid());
        assertEquals(TestConstants.PARAM_KEY_OWNER_VALUE, jpaServiceInstance.getParameter(TestConstants.PARAM_KEY_OWNER));
    }
    
    @Test
    public void testUpdateServiceInstance() throws Exception {

        //값을 세팅한다.
        request.setPlanId(jpaServiceInstance.getPlanId());
        request.setServiceDefinitionId(jpaServiceInstance.getServiceDefinitionId());

        when(instanceRepository.findByServiceInstanceId(upRequest.getServiceInstanceId())).thenReturn(jpaServiceInstance);
        when(catalog.getServiceDefinition(jpaServiceInstance.getServiceDefinitionId())).thenReturn(ServiceFixture.getService());
        

        //실제 함수 호출
        serviceInstance.updateServiceInstance(upRequest);
        
    }
     // 플랜 ID가 없는 경우 익셉션을 내뿜는다.
    @Test(expected=ServiceBrokerException.class)
    public void testUpdateServiceInstanceNoPlanId() throws Exception {

        //값을 세팅한다.
        request.setPlanId(jpaServiceInstance.getPlanId());
        request.setServiceDefinitionId(jpaServiceInstance.getServiceDefinitionId());
        upRequest.setPlanId("가짜플랜");
        
        when(instanceRepository.findByServiceInstanceId(upRequest.getServiceInstanceId())).thenReturn(jpaServiceInstance);
        when(catalog.getServiceDefinition(jpaServiceInstance.getServiceDefinitionId())).thenReturn(ServiceFixture.getService());
        

        //실제 함수 호출
        serviceInstance.updateServiceInstance(upRequest);
        
        // 익셉션 비교하는 로직 필요?
        
        
    }
    
    // findeInstance 랑 instance 가 다를 때 통과
    @Test
    public void testUpdateServiceInstanceNotEqual() throws Exception {

        //값을 세팅한다.
        request.setPlanId(jpaServiceInstance.getPlanId());
        request.setServiceDefinitionId(jpaServiceInstance.getServiceDefinitionId());
        jpaServiceInstanceDef.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_002);
        
        when(instanceRepository.findByServiceInstanceId(upRequest.getServiceInstanceId())).thenReturn(jpaServiceInstanceDef);
        when(catalog.getServiceDefinition(jpaServiceInstance.getServiceDefinitionId())).thenReturn(ServiceFixture.getService());
    
        
        //실제 함수 호출
        serviceInstance.updateServiceInstance(upRequest);
    
        // 값 비교 find의 값을 출력해서 비교하는 구문 적어야함.
    
        
    }
    
    // plan Id 가 널일 때  통과
    @Test
    public void testUpdateServiceInstancePlanIdNull() throws Exception {

        //값을 세팅한다.
        upRequest.setPlanId(null);
        upRequest.setServiceDefinitionId(jpaServiceInstance.getServiceDefinitionId());
        
        when(instanceRepository.findByServiceInstanceId(upRequest.getServiceInstanceId())).thenReturn(jpaServiceInstance);
        when(catalog.getServiceDefinition(jpaServiceInstance.getServiceDefinitionId())).thenReturn(ServiceFixture.getService());
        

        //실제 함수 호출
        serviceInstance.updateServiceInstance(upRequest);
        
        // 값 비교 로직 넣어야함
        
    }
    
    // findInstace가 널일때  통과 
    @Test(expected=ServiceBrokerException.class)
    public void testUpdateServiceInstanceNull() throws ServiceBrokerException {
        when(instanceRepository.findByServiceInstanceId(upRequest.getServiceInstanceId())).thenReturn(null);
        
        //실제 함수 호출
        serviceInstance.updateServiceInstance(upRequest);
        
        // TODO 에러를 뿜어야 했! 에러인지 체크하는 로직 필요
    }
    

}

//        //PowerMockito.whenNew(JpaServiceInstance.class).withArguments(upRequest).thenReturn(jpaServiceInstance);
