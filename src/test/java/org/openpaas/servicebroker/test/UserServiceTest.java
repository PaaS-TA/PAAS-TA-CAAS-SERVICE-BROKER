package org.openpaas.servicebroker.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openpaas.servicebroker.kubernetes.model.JpaServiceInstance;
import org.openpaas.servicebroker.kubernetes.service.RestTemplateService;
import org.openpaas.servicebroker.kubernetes.service.impl.UserService;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.springframework.http.HttpMethod;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	
	@Mock
	RestTemplateService restTemplateService;
	
	@InjectMocks
	UserService userService;
	
	private static JpaServiceInstance jpaServiceInstance;
	
	@Before
    public void setup() {
		jpaServiceInstance = new JpaServiceInstance();
		jpaServiceInstance.setCaasAccountName(TestConstants.JPA_CAAS_ACCOUNT_NAME);
		jpaServiceInstance.setCaasAccountTokenName(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN);
		jpaServiceInstance.setCaasNamespace(TestConstants.JPA_CAAS_NAMESPACE);
		jpaServiceInstance.setPlanId(TestConstants.SERVICEDEFINITION_PLAN_ID);
		jpaServiceInstance.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
	}
	
	@Test
	public void TestRequestPost() {
		
		//when 서술
		
		//실제 테스트할 함수 호출
		userService.request(jpaServiceInstance, HttpMethod.POST);
	}
	
	@Test
	public void TestRequestDelete() {
		
		//when 서술
		
		// 실제 테스트할 함수 호출
		userService.request(jpaServiceInstance, HttpMethod.DELETE);
	}

}
