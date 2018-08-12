package org.openpaas.servicebroker.kubernetes.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.kubernetes.config.EnvConfig;
import org.openpaas.servicebroker.kubernetes.model.JpaServiceInstance;
import org.openpaas.servicebroker.kubernetes.repo.JpaServiceInstanceRepository;
import org.openpaas.servicebroker.kubernetes.service.AdminTokenService;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.openpaas.servicebroker.model.fixture.PlanFixture;
import org.openpaas.servicebroker.model.fixture.RequestFixture;
import org.openpaas.servicebroker.model.fixture.ServiceFixture;
import org.openpaas.servicebroker.service.CatalogService;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)

//@ContextConfiguration(classes =  Application.class, loader = AnnotationConfigContextLoader.class) 이걸 없애면 @component 못 읽는 오류는 안난다.
//@SpringBootTest(classes = Application.class)
@PropertySource ( "classpath : application.yml")
//@TestPropertySource("classpath:application.yml")
//@AutoConfigureTestDatabase (replace = AutoConfigureTestDatabase.Replace.NONE) Testcode돌 때 mysql 설정할 때 쓰는데 안먹는다.

//@RunWith(PowerMockRunner.class)
//@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
public class InstanceServiceImplTest {
	
	private static final Logger logger = LoggerFactory.getLogger(InstanceServiceImplTest.class);
	
	@Mock
    private CatalogServiceImpl catalog;

	@Mock
    private JpaServiceInstanceRepository instanceRepository;

	@Mock
    KubernetesService kubernetesService;

	@Mock
    EnvConfig config;
    
	@Mock
	AdminTokenService adminTokenService;
	
	@InjectMocks
	InstanceServiceImpl serviceInstance;
	
	public static JpaServiceInstance jpaServiceInstance;
	
	public static JpaServiceInstance jpaServiceInstanceDef;

//	@MockBean
//	private static JpaServiceInstance newJpaServiceInstance;
	private CreateServiceInstanceRequest request;
	private static DeleteServiceInstanceRequest delRequest;
	private static UpdateServiceInstanceRequest upRequest;
	private static ServiceInstance instance;
	
	@Before
	public void setUp() throws Exception {
		
		request = RequestFixture.getCreateServiceInstanceRequest();
		delRequest = RequestFixture.getDeleteServiceInstanceRequest2();
		upRequest = RequestFixture.getUpdateServiceInstanceRequest();
		
		
		jpaServiceInstance = new JpaServiceInstance(request);
		jpaServiceInstance.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
		jpaServiceInstance.setCaasAccountAccessToken(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN);
		jpaServiceInstance.setCaasAccountName(TestConstants.JPA_CAAS_ACCOUNT_NAME);
		jpaServiceInstance.setCaasNamespace(TestConstants.JPA_CAAS_NAMESPACE);
		Map<String,Object> jpaMap = new HashMap<>();
		jpaMap.put("userName", TestConstants.PARAM_KEY_OWNER_VALUE);
		jpaServiceInstance.setParameters(jpaMap);
		
		jpaServiceInstanceDef = new JpaServiceInstance(RequestFixture.getCreateServiceInstanceRequest2());
		
		
	}
	
	/**
	 * instanceService의
	 * if ( findInstance != null ) 이 true일 때
	 * */ 
	@Test
	public void testCreateServiceInstanceFindInstanceNull() {
		
		// 값을 세팅한다.
		doNothing().when(adminTokenService).checkToken();
		when(instanceRepository.findByServiceInstanceId(request.getServiceInstanceId())).thenReturn(null);
		// 실제 코드를 호출한다.
		
		// 값을 비교한다.
		JpaServiceInstance findInstance;
		when(instanceRepository.findByServiceInstanceId(request.getServiceInstanceId())).thenReturn(null);
		logger.info("아 오 슈벙 {} ", jpaServiceInstance.toString());
		//JpaServiceInstance instance = (JpaServiceInstance) new JpaServiceInstance(request);
		logger.info("아 오 슈벙 {} ", jpaServiceInstance.toString());
		when(kubernetesService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
		when(instanceRepository.existsByCaasNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
		
		
	}

	
	// TODO : 통과
//	@Test
//	public void testGetServiceInstance() {
//		// 값을 세팅한다.
//		when(instanceRepository.getOne(request.getServiceInstanceId())).thenReturn(jpaServiceInstance);
//		// 실제로 테스트할 함수를 호출한다.
//		ServiceInstance instance = serviceInstance.getServiceInstance(request.getServiceInstanceId());
//		//결과 값이 맞는가 본다.
//		assertThat(instance).isNotNull();
//		assertEquals(jpaServiceInstance, instance);
//		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN, jpaServiceInstance.getCaasAccountAccessToken());
//		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_NAME, jpaServiceInstance.getCaasAccountName());
//		assertEquals(TestConstants.JPA_CAAS_NAMESPACE, jpaServiceInstance.getCaasNamespace());
//		assertEquals(TestConstants.JPA_ORGANIZTION_GUID, jpaServiceInstance.getOrganizationGuid());
//		assertEquals(TestConstants.JPA_SERVICE_DEFINITION_ID, jpaServiceInstance.getServiceDefinitionId());
//		assertEquals(TestConstants.JPA_SPACE_GUID, jpaServiceInstance.getSpaceGuid());
//		assertEquals(TestConstants.PARAM_KEY_OWNER_VALUE, jpaServiceInstance.getParameter(TestConstants.PARAM_KEY_OWNER));
//		
//	}
	
	/** 통과
	 * instanceService의
	 * if ( instance == null ) 이 true일 때
	 * */ 
//	@Test(expected=ServiceBrokerException.class)
//	public void testDeleteServiceInstanceNull() throws ServiceBrokerException {
//		//값을 세팅한다.
//		doNothing().when(adminTokenService).checkToken();
//		when(instanceRepository.getOne(request.getServiceInstanceId())).thenReturn(null).thenThrow(new ServiceBrokerException("instance doesn't exist in Database!!"));
//		//when(instanceRepository.getOne(request.getServiceInstanceId())).thenReturn(null).thenThrow(new ServiceBrokerException("instance doesn't exist in Database!!"));
//		
//		//when(instanceRepository.getOne(request.getServiceInstanceId())).thenThrow(throwables)(null);
//		//실제로 테스트할 함수를 호출한다.
//		ServiceInstance instance = serviceInstance.deleteServiceInstance(delRequest);		
//		//결과 값이 맞는가 본다.
//		// TODO : 오류일 때는여?
//		
//	}
	
	/** 통과
	 * instanceService의
	 * if ( instance == null )의 값 false 이고
	 * if (existsNamespace( instance.getCaasNamespace() ))의 existsNamespace 의 ruturn인
	 * kubernetesService.existsNamespace( namespace ) || instanceRepository.existsByCaasNamespace( namespace ) 
	 *  값 4가지 케이스중 둘다 true, true일 때 
	 * @throws ServiceBrokerException 
	 * */ 
//	@Test
//	public void testDeleteServiceNamespaceNotNull() throws ServiceBrokerException {
//		
//		// 값을 세팅한다.
//		request.setServiceDefinitionId(jpaServiceInstance.getServiceDefinitionId());
//		doNothing().when(adminTokenService).checkToken();
//		when(instanceRepository.findByServiceInstanceId(TestConstants.SV_INSTANCE_ID_001)).thenReturn(jpaServiceInstance);
//		when(kubernetesService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
//		
//		doNothing().when(kubernetesService).deleteNamespace(TestConstants.JPA_CAAS_NAMESPACE);
//		doNothing().when(instanceRepository).delete(jpaServiceInstance);
//		
//		
//		// 실제를 호출한다.
//		ServiceInstance instance = serviceInstance.deleteServiceInstance(delRequest);	
//		
//		assertThat(jpaServiceInstance).isNotNull();
//		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN, jpaServiceInstance.getCaasAccountAccessToken());
//		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_NAME, jpaServiceInstance.getCaasAccountName());
//		assertEquals(TestConstants.JPA_CAAS_NAMESPACE, jpaServiceInstance.getCaasNamespace());
//		assertEquals(TestConstants.JPA_ORGANIZTION_GUID, jpaServiceInstance.getOrganizationGuid());
//		assertEquals(TestConstants.JPA_SERVICE_DEFINITION_ID, jpaServiceInstance.getServiceDefinitionId());
//		assertEquals(TestConstants.JPA_SPACE_GUID, jpaServiceInstance.getSpaceGuid());
//		assertEquals(TestConstants.PARAM_KEY_OWNER_VALUE, jpaServiceInstance.getParameter(TestConstants.PARAM_KEY_OWNER));
//	}
	
	/** 통과
	 * instanceService의
	 * if ( instance == null )의 값 false 이고
	 * if (existsNamespace( instance.getCaasNamespace() ))의 existsNamespace 의 ruturn인
	 * kubernetesService.existsNamespace( namespace ) || instanceRepository.existsByCaasNamespace( namespace ) 
	 *  값 4가지 케이스중 둘다 false, true일 때 
	 * @throws ServiceBrokerException 
	 * */ 
//	@Test
//	public void testDeleteServiceNamespaceNotNull2() throws ServiceBrokerException {
//		doNothing().when(adminTokenService).checkToken();
//		when(instanceRepository.findByServiceInstanceId(TestConstants.SV_INSTANCE_ID_001)).thenReturn(jpaServiceInstance);
//		when(kubernetesService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(false);
//		
//		System.out.println("젠장!" + jpaServiceInstance);
//		// 실제를 호출한다.
//		ServiceInstance instance = serviceInstance.deleteServiceInstance(delRequest);
//		
//		// TODO : 오류를 뿝는 경우에는 어떻게 처리해야함?
//		/*assertThat(jpaServiceInstance).isNotNull();
//		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN, jpaServiceInstance.getCaasAccountAccessToken());
//		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_NAME, jpaServiceInstance.getCaasAccountName());
//		assertEquals(TestConstants.JPA_CAAS_NAMESPACE, jpaServiceInstance.getCaasNamespace());
//		assertEquals(TestConstants.JPA_ORGANIZTION_GUID, jpaServiceInstance.getOrganizationGuid());
//		assertEquals(TestConstants.JPA_SERVICE_DEFINITION_ID, jpaServiceInstance.getServiceDefinitionId());
//		assertEquals(TestConstants.JPA_SPACE_GUID, jpaServiceInstance.getSpaceGuid());
//		assertEquals(TestConstants.PARAM_KEY_OWNER_VALUE, jpaServiceInstance.getParameter(TestConstants.PARAM_KEY_OWNER));
//*/	
//	}
	
//	@Test
//	public void testUpdateServiceInstance() throws Exception {
//
//		//값을 세팅한다.
//		request.setPlanId(jpaServiceInstance.getPlanId());
//		request.setServiceDefinitionId(jpaServiceInstance.getServiceDefinitionId());
//
//		doNothing().when(adminTokenService).checkToken();
//		when(instanceRepository.findByServiceInstanceId(upRequest.getServiceInstanceId())).thenReturn(jpaServiceInstance);
//		when(catalog.getServiceDefinition(jpaServiceInstance.getServiceDefinitionId())).thenReturn(ServiceFixture.getService());
//		
//
//		//실제 함수 호출
//		serviceInstance.updateServiceInstance(upRequest);
//		
//	}
//	 // 플랜 ID가 없는 경우 익셉션을 내뿜는다.
//	@Test
//	public void testUpdateServiceInstanceNoPlanId() throws Exception {
//
//		//값을 세팅한다.
//		request.setPlanId(jpaServiceInstance.getPlanId());
//		request.setServiceDefinitionId(jpaServiceInstance.getServiceDefinitionId());
//		upRequest.setPlanId("가짜플랜");
//		
//		doNothing().when(adminTokenService).checkToken();
//		when(instanceRepository.findByServiceInstanceId(upRequest.getServiceInstanceId())).thenReturn(jpaServiceInstance);
//		when(catalog.getServiceDefinition(jpaServiceInstance.getServiceDefinitionId())).thenReturn(ServiceFixture.getService());
//		
//
//		//실제 함수 호출
//		serviceInstance.updateServiceInstance(upRequest);
//		
//		// 익셉션 비교하는 로직 필요?
//		
//	}
	
	// findeInstance 랑 instance 가 다를 때 통과
//	@Test
//	public void testUpdateServiceInstanceNotEqual() throws Exception {
//
//		//값을 세팅한다.
//		request.setPlanId(jpaServiceInstance.getPlanId());
//		request.setServiceDefinitionId(jpaServiceInstance.getServiceDefinitionId());
//		jpaServiceInstanceDef.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_002);
//		
//		doNothing().when(adminTokenService).checkToken();
//		when(instanceRepository.findByServiceInstanceId(upRequest.getServiceInstanceId())).thenReturn(jpaServiceInstanceDef);
//		when(catalog.getServiceDefinition(jpaServiceInstance.getServiceDefinitionId())).thenReturn(ServiceFixture.getService());
//	
//		
//		//실제 함수 호출
//		serviceInstance.updateServiceInstance(upRequest);
//	
//	    // 값 비교 find의 값을 출력해서 비교하는 구문 적어야함.
//	
//		
//	}
	
//	// plan Id 가 널일 때  통과
//	@Test
//	public void testUpdateServiceInstancePlanIdNull() throws Exception {
//
//		//값을 세팅한다.
//		upRequest.setPlanId(null);
//		upRequest.setServiceDefinitionId(jpaServiceInstance.getServiceDefinitionId());
//		
//		doNothing().when(adminTokenService).checkToken();
//		when(instanceRepository.findByServiceInstanceId(upRequest.getServiceInstanceId())).thenReturn(jpaServiceInstance);
//		when(catalog.getServiceDefinition(jpaServiceInstance.getServiceDefinitionId())).thenReturn(ServiceFixture.getService());
//		
//
//		//실제 함수 호출
//		serviceInstance.updateServiceInstance(upRequest);
//		
//		// 값 비교 로직 넣어야함
//		
//	}
	
	// findInstace가 널일때  통과 
//	@Test
//	public void testUpdateServiceInstanceNull() throws ServiceBrokerException {
//		doNothing().when(adminTokenService).checkToken();
//		when(instanceRepository.findByServiceInstanceId(upRequest.getServiceInstanceId())).thenReturn(null);
//		
//		//실제 함수 호출
//		serviceInstance.updateServiceInstance(upRequest);
//		
//		// TODO 에러를 뿜어야 했! 에러인지 체크하는 로직 필요
//	}
	

}

//		//PowerMockito.whenNew(JpaServiceInstance.class).withArguments(upRequest).thenReturn(jpaServiceInstance);