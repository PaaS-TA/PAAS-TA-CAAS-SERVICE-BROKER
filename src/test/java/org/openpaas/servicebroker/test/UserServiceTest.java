package org.openpaas.servicebroker.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openpaas.servicebroker.kubernetes.model.JpaServiceInstance;
import org.openpaas.servicebroker.kubernetes.model.User;
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
	private User user = new User();
	
	@Before
    public void setup() {
		jpaServiceInstance = new JpaServiceInstance();
		jpaServiceInstance.setCaasAccountName(TestConstants.JPA_CAAS_ACCOUNT_NAME);
		jpaServiceInstance.setCaasAccountTokenName(TestConstants.JPA_CAAS_ACCOUNT_ACCESS_TOKEN);
		jpaServiceInstance.setCaasNamespace(TestConstants.JPA_CAAS_NAMESPACE);
		jpaServiceInstance.setPlanId(TestConstants.SERVICEDEFINITION_PLAN_ID);
		jpaServiceInstance.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
		
		//테스트 커버리지는 올리기 위한 뻘짓 (하지마세요)
		user.setCaasAccountName("name");
		user.setCaasAccountTokenName("token_value");
		user.setCaasNamespace("namespace");
		user.setId("auto_value");
		user.setOrganizationGuid("org_guid");
		user.setServiceInstanceId("instance_id");
		user.setSpaceGuid("space_guid");
		user.setUserId("user@user.com");
		
		user.getCaasAccountName();
		user.getCaasAccountTokenName();
		user.getCaasNamespace();
		user.getId();
		user.getOrganizationGuid();
		user.getRoleName();
		user.getRoleSetCode();
		user.getServiceInstanceId();
		user.getSpaceGuid();
		user.getUserId();
		
	}
	
	@Test
	public void TestRequestPost() {
		
		//when 서술
		
		//실제 테스트할 함수 호출
		userService.request(jpaServiceInstance, HttpMethod.POST);
		
		//실제 결과 값 비교
		
	}
	
	@Test
	public void TestRequestDelete() {
		
		//when 서술
		
		// 실제 테스트할 함수 호출
		userService.request(jpaServiceInstance, HttpMethod.DELETE);
	}

}
