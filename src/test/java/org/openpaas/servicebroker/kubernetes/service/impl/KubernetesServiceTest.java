package org.openpaas.servicebroker.kubernetes.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.openpaas.servicebroker.kubernetes.config.EnvConfig;
import org.openpaas.servicebroker.kubernetes.exception.KubernetesServiceException;
import org.openpaas.servicebroker.kubernetes.model.JpaServiceInstance;
import org.openpaas.servicebroker.kubernetes.service.RestTemplateService;
import org.openpaas.servicebroker.kubernetes.service.TemplateService;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.fixture.PlanFixture;
import org.openpaas.servicebroker.model.fixture.RequestFixture;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.springframework.http.HttpMethod;

@RunWith(MockitoJUnitRunner.class)
public class KubernetesServiceTest {
    
    private static String createNamespaceYml = "instance/create_namespace.ftl";
    
    private static String token = "{\"kind\":\"ServiceAccount\",\"apiVersion\":\"v1\",\"metadata\":{\"name\":\"org-dummy-testhyerin3049-admin\",\"namespace\":\"paas-test-hyerin-instance-caas\",\"selfLink\":\"/api/v1/namespaces/paas-test-hyerin-instance-caas/serviceaccounts/org-dummy-testhyerin3049-admin\",\"uid\":\"b4dc88ba-9c98-11e8-8917-005056900b9f\",\"resourceVersion\":\"4540178\",\"creationTimestamp\":\"2018-08-10T12:27:11Z\"},\"secrets\":[{\"name\":\"org_guid_001testuser001-admin-qwert\"}]}";
    
    private Map<String, Object> model;
    
    @Mock
    private TemplateService templateService;

    @Spy
    private static EnvConfig envConfig;
    
    @Mock
    RestTemplateService restTemplateService;
    
    @InjectMocks
    KubernetesService kubernetesService;
    
    private static JpaServiceInstance jpaServiceInstance;
	private static CreateServiceInstanceRequest request;
	
	@Before
	public void setUp() throws Exception {
		
		request = RequestFixture.getCreateServiceInstanceRequest();
		
		jpaServiceInstance = new JpaServiceInstance(request);
		jpaServiceInstance.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
		jpaServiceInstance.setCaasAccountAccessToken(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN);
		jpaServiceInstance.setCaasAccountName(TestConstants.JPA_CAAS_ACCOUNT_NAME);
		jpaServiceInstance.setCaasNamespace(TestConstants.JPA_CAAS_NAMESPACE);
		Map<String,Object> jpaMap = new HashMap<>();
		jpaMap.put("userName", TestConstants.PARAM_KEY_OWNER_VALUE);
		jpaServiceInstance.setParameters(jpaMap);
		
	}

    @Test
    public void testCreateNamespaceUser() throws KubernetesServiceException {
    	System.out.println(jpaServiceInstance.getServiceInstanceId());
    	envConfig.setAdminToken(token);
    	envConfig.setCaasUrl("hihi");
    	envConfig.setDashboardUrl("asdasdasdasd");
    	
        // 값을 세팅한다 (다른 서비스 호출한 것을 가짜로 대체한다)
        when(templateService.convert(createNamespaceYml, model)).thenReturn(createNamespaceYml);
        when(restTemplateService.send(envConfig.getCaasUrl() + "/api/v1/namespaces/" + TestConstants.JPA_CAAS_NAMESPACE, createNamespaceYml, HttpMethod.POST, String.class)).thenReturn(createNamespaceYml);
        when(restTemplateService.send(envConfig.getCaasUrl() + "/api/v1/namespaces/" + TestConstants.JPA_CAAS_NAMESPACE, HttpMethod.POST, String.class)).thenReturn(createNamespaceYml);
        
        // TODO : 책임님꼐 여쭤봐야함    값을 못 받아오는데 ㅇㅁㅇ?
        when(envConfig.getCaasUrl()).thenReturn("hohohoho");
        when(restTemplateService.send(envConfig.getCaasUrl() + "/api/v1/namespaces/" + TestConstants.JPA_CAAS_NAMESPACE + "/serviceaccounts/" + TestConstants.JPA_ORGANIZTION_GUID + TestConstants.JPA_CAAS_ACCOUNT_NAME, HttpMethod.GET, String.class)).thenReturn(token);
        // 실제로 테스트할 함수를 호출한다.
        JpaServiceInstance instance = kubernetesService.createNamespaceUser(jpaServiceInstance, PlanFixture.getPlanOne());
        
        // 결과 값이 맞는지 체크한다.
        assertThat(instance).isNotNull();
		assertEquals(jpaServiceInstance, instance);
		//assertEquals(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN, jpaServiceInstance.getCaasAccountAccessToken());
		assertEquals(TestConstants.ORG_GUID_001 + TestConstants.JPA_CAAS_ACCOUNT_NAME, jpaServiceInstance.getCaasAccountName());
		assertEquals(TestConstants.JPA_CAAS_NAMESPACE, jpaServiceInstance.getCaasNamespace());
		assertEquals(TestConstants.JPA_ORGANIZTION_GUID, jpaServiceInstance.getOrganizationGuid());
		assertEquals(TestConstants.JPA_SERVICE_DEFINITION_ID, jpaServiceInstance.getServiceDefinitionId());
		assertEquals(TestConstants.JPA_SPACE_GUID, jpaServiceInstance.getSpaceGuid());
		assertEquals(TestConstants.PARAM_KEY_OWNER_VALUE, jpaServiceInstance.getParameter(TestConstants.PARAM_KEY_OWNER));
	
    }
    
    @Test
    public void testDeleteNamespace() {
    	
    	// 값을 세팅한다.
    	when(restTemplateService.send(envConfig.getCaasUrl(), createNamespaceYml, HttpMethod.DELETE, String.class)).thenReturn(createNamespaceYml);
    	
    	// 실제로 테스트할 함수를 호출한다.
    	kubernetesService.deleteNamespace(jpaServiceInstance.getCaasNamespace());
    	
    	// 결과 값이 맞는지 체크한다.
    	// TODO : 결과 값이 없으면 어떻게 해야하지...?
    }
    
    @Test
    public void testChangeResourceQuota() throws KubernetesServiceException {
    	
    	// 값을 세팅한다.
    	when(templateService.convert(createNamespaceYml, model)).thenReturn(createNamespaceYml);
    	when(restTemplateService.send(envConfig.getCaasUrl(), createNamespaceYml, HttpMethod.PUT, String.class)).thenReturn(createNamespaceYml);
    	
    	// 실제로 테스트할 함수를 호출한다.
    	kubernetesService.changeResourceQuota(jpaServiceInstance.getCaasAccountName(), PlanFixture.getPlanOne());
    	
    	// 결과 값이 맞는지 체크한다.
    	// TODO : 결과 값이 없으면 어떻게 해야하지...?
    	
    }
    
    @Test
    public void testChangeResourceQuotaNull() throws KubernetesServiceException {
    	
    	// 값을 세팅한다.
    	when(templateService.convert(createNamespaceYml, model)).thenReturn(createNamespaceYml);
    	when(restTemplateService.send(envConfig.getCaasUrl(), createNamespaceYml, HttpMethod.PUT, String.class)).thenReturn(null);
    	
    	// 실제로 테스트할 함수를 호출한다.
    	kubernetesService.changeResourceQuota(jpaServiceInstance.getCaasAccountName(), PlanFixture.getPlanOne());
    	
    	// 결과 값이 맞는지 체크한다.
    	// TODO : 결과 값이 없으면 어떻게 해야하지...?
    	
    }

}

