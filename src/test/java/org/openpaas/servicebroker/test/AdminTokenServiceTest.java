package org.openpaas.servicebroker.test;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.openpaas.servicebroker.kubernetes.model.Constants;
import org.openpaas.servicebroker.kubernetes.model.JpaAdminToken;
import org.openpaas.servicebroker.kubernetes.repo.JpaAdminTokenRepository;
import org.openpaas.servicebroker.kubernetes.service.PropertyService;
import org.openpaas.servicebroker.kubernetes.service.RestTemplateService;
import org.openpaas.servicebroker.kubernetes.service.impl.AdminTokenService;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AdminTokenServiceTest {
	
	@Mock
	JpaAdminTokenRepository adminTokenRepository;
	
	@Mock
	RestTemplateService restTemplateService;
	
    @Spy
    private static PropertyService propertyService;
	
	@InjectMocks
	AdminTokenService adminTokenService;
	
	private static JpaAdminToken adminToken;
	private static String tokenValue = "token-value";
	private static JpaAdminToken adminTokenEmpty;
	private final String adminTokenId = "caas_admin";
	
	@Before
	public void setUp() throws Exception {
		
		adminToken = new JpaAdminToken(tokenValue);
		adminTokenEmpty = new JpaAdminToken();
		adminTokenEmpty.setTokenValue("def-value");
		adminTokenEmpty.getTokenValue();
		
		propertyService.setCaasUrl("hihi");
		propertyService.setDashboardUrl("asdasdasdasd");
		
		
		
	}

	// 토큰이 존재하고 밸리데이션 통과
	@Test
	public void testCheckToken() {
		
		// 값을 세팅한다.
//		when(propertyService.getAdminToken()).thenReturn(adminTokenId);
		when(adminTokenRepository.exists(adminTokenId)).thenReturn(true);
		when(restTemplateService.tokenValidation()).thenReturn(true);
		when(propertyService.getCaasClusterCommand()).thenReturn("pwd");
		
		// 실제 함수를 호출한다.
		adminTokenService.checkToken();
		
		// 값을 비교한다.
		// TODO : 리턴 값이 없는 함수를 호출하자!
		
	}
	
	// 토큰이 존재하고 밸리데이션 에러
		@Test
		public void testCheckTokenNoValidation() {
			
			// 값을 세팅한다.
//			when(propertyService.getAdminToken()).thenReturn(adminTokenId);
			when(adminTokenRepository.exists(Constants.TOKEN_KEY)).thenReturn(true);
			when(restTemplateService.tokenValidation()).thenReturn(false);
//			when(sshService.executeSsh("pwd")).thenReturn("yaho");;
			when(propertyService.getCaasClusterCommand()).thenReturn("pwd");
			when(adminTokenRepository.save(adminToken)).thenReturn(adminToken);
			
			// 실제 함수를 호출한다.
			adminTokenService.checkToken();
			
			// 값을 비교한다.
			// TODO : 리턴 값이 없는 함수를 호출하자!
			
		}
	
	// 토큰이 존재하지 않을 떄
	@Test
	public void testCheckTokenNull() {
		
		// 값을 세팅한다.	
//		when(propertyService.getAdminToken()).thenReturn(adminTokenId);
		when(adminTokenRepository.exists(adminTokenId)).thenReturn(false);
		when(propertyService.getCaasClusterCommand()).thenReturn("pwd");
//		when(sshService.executeSsh("pwd")).thenReturn("yaho");
		when(adminTokenRepository.save(adminToken)).thenReturn(adminToken);
		
		// 실제 함수를 호출한다.
		adminTokenService.checkToken();
		
		// 값을 비교한다.
		// TODO : 리턴 값이 없는 함수를 호출하자!
		
	}

}
