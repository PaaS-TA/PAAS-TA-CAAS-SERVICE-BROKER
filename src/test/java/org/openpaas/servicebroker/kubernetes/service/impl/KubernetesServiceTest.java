package org.openpaas.servicebroker.kubernetes.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openpaas.servicebroker.kubernetes.config.Application;
import org.openpaas.servicebroker.kubernetes.config.EnvConfig;
import org.openpaas.servicebroker.kubernetes.exception.KubernetesServiceException;
import org.openpaas.servicebroker.kubernetes.model.JpaServiceInstance;
import org.openpaas.servicebroker.kubernetes.service.RestTemplateService;
import org.openpaas.servicebroker.kubernetes.service.TemplateService;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.fixture.PlanFixture;
import org.openpaas.servicebroker.model.fixture.RequestFixture;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import com.mysql.fabric.xmlrpc.base.Array;

@RunWith(SpringRunner.class)
@PropertySource ( "classpath : application.yml")

public class KubernetesServiceTest {
    
    private static String createNamespaceYml = "instance/create_namespace.ftl";
    
    private static String token = "{\"kind\":\"ServiceAccount\",\"apiVersion\":\"v1\",\"metadata\":{\"name\":\"org-dummy-testhyerin3049-admin\",\"namespace\":\"paas-test-hyerin-instance-caas\",\"selfLink\":\"/api/v1/namespaces/paas-test-hyerin-instance-caas/serviceaccounts/org-dummy-testhyerin3049-admin\",\"uid\":\"b4dc88ba-9c98-11e8-8917-005056900b9f\",\"resourceVersion\":\"4540178\",\"creationTimestamp\":\"2018-08-10T12:27:11Z\"},\"secrets\":[{\"name\":\"org-dummy-testhyerin3049-admin-token-wtqhs\"}]}";
    
    private Map<String, Object> model;
    
    @Mock
    private TemplateService templateService;

    @Mock
    private EnvConfig envConfig;
    
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
		
//		jsonObj = new JSONObject();
//		List<Map<String, String>> list = new ArrayList<>();
//		Map<String, String> map = new HashMap<>();
//		map.put("name", TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN);
//		list.add(map);
//		jsonObj.put("secrets", list);
		
	}

    @Test
    public void testCreateNamespaceUser() throws KubernetesServiceException {
    	System.out.println(jpaServiceInstance.getServiceInstanceId());
        // 값을 세팅한다 (다른 서비스 호출한 것을 가짜로 대체한다)
        when(templateService.convert(createNamespaceYml, model)).thenReturn(createNamespaceYml);
        when(restTemplateService.send(envConfig.getCaasUrl(), createNamespaceYml, HttpMethod.POST, String.class)).thenReturn(createNamespaceYml);
        when(restTemplateService.send(envConfig.getCaasUrl(), HttpMethod.POST, String.class)).thenReturn(createNamespaceYml);
        
        // TODO : 책임님꼐 여쭤봐야함    값을 못 받아오는데 ㅇㅁㅇ?
        when(restTemplateService.send(envConfig.getCaasUrl(), HttpMethod.GET, String.class)).thenReturn(token);
        
        // 실제로 테스트할 함수를 호출한다.
        JpaServiceInstance instance = kubernetesService.createNamespaceUser(jpaServiceInstance, PlanFixture.getPlanOne());
        
        // 결과 값이 맞는지 체크한다.
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

}
