package org.openpaas.servicebroker.kubernetes.service.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.kubernetes.config.Application;
import org.openpaas.servicebroker.kubernetes.config.BrokerConfig;
import org.openpaas.servicebroker.kubernetes.config.EnvConfig;
import org.openpaas.servicebroker.kubernetes.config.WebXml;
import org.openpaas.servicebroker.kubernetes.model.JpaServiceInstance;
import org.openpaas.servicebroker.kubernetes.repo.JpaServiceInstanceRepository;
import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.fixture.CatalogFixture;
import org.openpaas.servicebroker.model.fixture.RequestFixture;
import org.openpaas.servicebroker.model.fixture.ServiceFixture;
import org.openpaas.servicebroker.model.fixture.ServiceInstanceFixture;
import org.openpaas.servicebroker.service.CatalogService;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
//@ContextConfiguration(classes =  Application.class, loader = AnnotationConfigContextLoader.class) 이걸 없애면 @component 못 읽는 오류는 안난다.
//@SpringBootTest(classes = Application.class)
@PropertySource ( "classpath : application.yml")
//@TestPropertySource("classpath:application.yml")
//@AutoConfigureTestDatabase (replace = AutoConfigureTestDatabase.Replace.NONE) Testcode돌 때 mysql 설정할 때 쓰는데 안먹는다.

public class InstanceServiceImplTest {
	
	private static final Logger logger = LoggerFactory.getLogger(InstanceServiceImplTest.class);
	
	@Mock
    private CatalogService catalog;

	@Mock
    private JpaServiceInstanceRepository instanceRepository;

	@Mock
    KubernetesService kubernetesService;

	@Mock
    EnvConfig config;
    
	@Mock
    SshService sshService;
	
	@InjectMocks
	InstanceServiceImpl serviceInstance;
	
	private static JpaServiceInstance jpaServiceInstance;

//	@MockBean
//	private static JpaServiceInstance jpaServiceInstance2;
	private static CreateServiceInstanceRequest request;
	private static DeleteServiceInstanceRequest delRequest;
	private static ServiceInstance instance;
	
	@Before
	public void setUp() throws Exception {
		
		request = RequestFixture.getCreateServiceInstanceRequest();
		delRequest = RequestFixture.getDeleteServiceInstanceRequest();
		
		jpaServiceInstance = new JpaServiceInstance(request);
		jpaServiceInstance.setCaasAccountAccessToken(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN);
		jpaServiceInstance.setCaasAccountName(TestConstants.JPA_CAAS_ACCOUNT_NAME);
		jpaServiceInstance.setCaasNamespace(TestConstants.JPA_CAAS_NAMESPACE);
		Map<String,Object> jpaMap = new HashMap<>();
		jpaMap.put("userName", TestConstants.PARAM_KEY_OWNER_VALUE);
		jpaServiceInstance.setParameters(jpaMap);
		
	}
	
	/**
	 * instanceService의
	 * if ( findInstance != null ) 이 true일 때
	 * */ 
//	@Test
	public void testCreateServiceInstanceFindInstanceNull() {
		JpaServiceInstance findInstance;
		when(instanceRepository.findByServiceInstanceId(request.getServiceInstanceId())).thenReturn(null);
		logger.info("아 오 슈벙 {} ", jpaServiceInstance.toString());
		//JpaServiceInstance instance = (JpaServiceInstance) new JpaServiceInstance(request);
		logger.info("아 오 슈벙 {} ", jpaServiceInstance.toString());
		when(kubernetesService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
		when(instanceRepository.existsByCaasNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
		
		
	}

	
	@Test
	public void testGetServiceInstance() {
		// 값을 세팅한다.
		when(instanceRepository.getOne(request.getServiceInstanceId())).thenReturn(jpaServiceInstance);
		// 실제로 테스트할 함수를 호출한다.
		ServiceInstance instance = serviceInstance.getServiceInstance(request.getServiceInstanceId());
		//결과 값이 맞는가 본다.
		assertThat(instance).isNotNull();
		assertEquals(jpaServiceInstance, instance);
		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN, jpaServiceInstance.getCaasAccountAccessToken());
		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_NAME, jpaServiceInstance.getCaasAccountName());
		assertEquals(TestConstants.JPA_CAAS_NAMESPACE, jpaServiceInstance.getCaasNamespace());
		assertEquals(TestConstants.JPA_ORGANIZTION_GUID, jpaServiceInstance.getOrganizationGuid());
		assertEquals(TestConstants.JPA_SERVICE_DEFINITION_ID, jpaServiceInstance.getServiceDefinitionId());
		assertEquals(TestConstants.JPA_SPACE_GUID, jpaServiceInstance.getSpaceGuid());
		assertEquals(TestConstants.PARAM_KEY_OWNER_VALUE, jpaServiceInstance.getParameter(TestConstants.PARAM_KEY_OWNER));
		
	}
	
	/**
	 * instanceService의
	 * if ( instance == null ) 이 true일 때
	 * */ 
	@Test
	public void testDeleteServiceInstanceNull() throws ServiceBrokerException {
		//값을 세팅한다.
		when(serviceInstance.getServiceInstance(request.getServiceInstanceId())).thenReturn(null);
		//실제로 테스트할 함수를 호출한다.
		ServiceInstance instance = serviceInstance.deleteServiceInstance(delRequest);		
		//결과 값이 맞는가 본다.
		assertThat(instance).isNull();
	}
	
	/**
	 * instanceService의
	 * if ( instance == null )의 값 false 이고
	 * if (existsNamespace( instance.getCaasNamespace() ))의 existsNamespace 의 ruturn인
	 * kubernetesService.existsNamespace( namespace ) || instanceRepository.existsByCaasNamespace( namespace ) 
	 *  값 4가지 케이스중 둘다 true, true일 때 
	 * */ 
	@Test
	public void testDeleteServiceNamespaceNotNull() {
		when(serviceInstance.getServiceInstance(request.getServiceInstanceId())).thenReturn(jpaServiceInstance);
		when(kubernetesService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
		when(instanceRepository.existsByCaasNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
		
		when(kubernetesService.deleteNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
		doNothing().when(instanceRepository).delete(jpaServiceInstance);
		
		assertThat(jpaServiceInstance).isNotNull();
		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN, jpaServiceInstance.getCaasAccountAccessToken());
		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_NAME, jpaServiceInstance.getCaasAccountName());
		assertEquals(TestConstants.JPA_CAAS_NAMESPACE, jpaServiceInstance.getCaasNamespace());
		assertEquals(TestConstants.JPA_ORGANIZTION_GUID, jpaServiceInstance.getOrganizationGuid());
		assertEquals(TestConstants.JPA_SERVICE_DEFINITION_ID, jpaServiceInstance.getServiceDefinitionId());
		assertEquals(TestConstants.JPA_SPACE_GUID, jpaServiceInstance.getSpaceGuid());
		assertEquals(TestConstants.PARAM_KEY_OWNER_VALUE, jpaServiceInstance.getParameter(TestConstants.PARAM_KEY_OWNER));
	}
	
	/**
	 * instanceService의
	 * if ( instance == null )의 값 false 이고
	 * if (existsNamespace( instance.getCaasNamespace() ))의 existsNamespace 의 ruturn인
	 * kubernetesService.existsNamespace( namespace ) || instanceRepository.existsByCaasNamespace( namespace ) 
	 *  값 4가지 케이스중 둘다 false, true일 때 
	 * */ 
	@Test
	public void testDeleteServiceNamespaceNotNull2() {
		when(serviceInstance.getServiceInstance(request.getServiceInstanceId())).thenReturn(jpaServiceInstance);
		when(kubernetesService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(false);
		when(instanceRepository.existsByCaasNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
	
		when(kubernetesService.deleteNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
		doNothing().when(instanceRepository).delete(jpaServiceInstance);
		
		assertThat(jpaServiceInstance).isNotNull();
		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN, jpaServiceInstance.getCaasAccountAccessToken());
		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_NAME, jpaServiceInstance.getCaasAccountName());
		assertEquals(TestConstants.JPA_CAAS_NAMESPACE, jpaServiceInstance.getCaasNamespace());
		assertEquals(TestConstants.JPA_ORGANIZTION_GUID, jpaServiceInstance.getOrganizationGuid());
		assertEquals(TestConstants.JPA_SERVICE_DEFINITION_ID, jpaServiceInstance.getServiceDefinitionId());
		assertEquals(TestConstants.JPA_SPACE_GUID, jpaServiceInstance.getSpaceGuid());
		assertEquals(TestConstants.PARAM_KEY_OWNER_VALUE, jpaServiceInstance.getParameter(TestConstants.PARAM_KEY_OWNER));
	}
	
	/**
	 * instanceService의
	 * if ( instance == null )의 값 false 이고
	 * if (existsNamespace( instance.getCaasNamespace() ))의 existsNamespace 의 ruturn인
	 * kubernetesService.existsNamespace( namespace ) || instanceRepository.existsByCaasNamespace( namespace ) 
	 *  값 4가지 케이스중 둘다 true, false일 때 
	 * */ 
	@Test
	public void testDeleteServiceNamespaceNotNull3() {
		when(serviceInstance.getServiceInstance(request.getServiceInstanceId())).thenReturn(jpaServiceInstance);
		when(kubernetesService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
		when(instanceRepository.existsByCaasNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(false);
	
		when(kubernetesService.deleteNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(true);
		doNothing().when(instanceRepository).delete(jpaServiceInstance);
		
		assertThat(jpaServiceInstance).isNotNull();
		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN, jpaServiceInstance.getCaasAccountAccessToken());
		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_NAME, jpaServiceInstance.getCaasAccountName());
		assertEquals(TestConstants.JPA_CAAS_NAMESPACE, jpaServiceInstance.getCaasNamespace());
		assertEquals(TestConstants.JPA_ORGANIZTION_GUID, jpaServiceInstance.getOrganizationGuid());
		assertEquals(TestConstants.JPA_SERVICE_DEFINITION_ID, jpaServiceInstance.getServiceDefinitionId());
		assertEquals(TestConstants.JPA_SPACE_GUID, jpaServiceInstance.getSpaceGuid());
		assertEquals(TestConstants.PARAM_KEY_OWNER_VALUE, jpaServiceInstance.getParameter(TestConstants.PARAM_KEY_OWNER));
	}
	
	/**
	 * instanceService의
	 * if ( instance == null )의 값 false 이고
	 * if (existsNamespace( instance.getCaasNamespace() ))의 existsNamespace 의 ruturn인
	 * kubernetesService.existsNamespace( namespace ) || instanceRepository.existsByCaasNamespace( namespace ) 
	 *  값 4가지 케이스중 둘다 false, false일 때 
	 * */ 	
	@Test
	public void testDeleteServiceNamespaceNotNull4() {
		when(serviceInstance.getServiceInstance(request.getServiceInstanceId())).thenReturn(jpaServiceInstance);
		when(kubernetesService.existsNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(false);
		when(instanceRepository.existsByCaasNamespace(TestConstants.JPA_CAAS_NAMESPACE)).thenReturn(false);
		
		assertThat(jpaServiceInstance).isNotNull();
		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN, jpaServiceInstance.getCaasAccountAccessToken());
		assertEquals(TestConstants.JPA_CAAS_ACCOUNT_NAME, jpaServiceInstance.getCaasAccountName());
		assertEquals(TestConstants.JPA_CAAS_NAMESPACE, jpaServiceInstance.getCaasNamespace());
		assertEquals(TestConstants.JPA_ORGANIZTION_GUID, jpaServiceInstance.getOrganizationGuid());
		assertEquals(TestConstants.JPA_SERVICE_DEFINITION_ID, jpaServiceInstance.getServiceDefinitionId());
		assertEquals(TestConstants.JPA_SPACE_GUID, jpaServiceInstance.getSpaceGuid());
		assertEquals(TestConstants.PARAM_KEY_OWNER_VALUE, jpaServiceInstance.getParameter(TestConstants.PARAM_KEY_OWNER));
	}
	
	@Test
	public void testUpdateServiceInstance() {
		when(instanceRepository.findByServiceInstanceId(request.getServiceInstanceId())).thenReturn(jpaServiceInstance);
		
		
		
	}
	
	@Test
	public void testUpdateServiceInstanceNull() {
		when(instanceRepository.findByServiceInstanceId(request.getServiceInstanceId())).thenReturn(null);
		
		
		
	}

}
