package org.openpaas.servicebroker.kubernetes.service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openpaas.servicebroker.kubernetes.model.JpaAdminToken;
import org.openpaas.servicebroker.kubernetes.repo.JpaAdminTokenRepository;
import org.openpaas.servicebroker.kubernetes.service.impl.SshService;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AdminTokenServiceTest {
	
	@Mock
	JpaAdminTokenRepository adminTokenRepository;
	
	@Mock
	SshService sshService;
	
	@InjectMocks
	AdminTokenService adminTokenService;
	
	private static JpaAdminToken adminToken;
	private static List<JpaAdminToken> tokenList;
	private static String tokenValue = "token-value";
	
	@Before
	public void setUp() throws Exception {
		tokenList = new ArrayList<>();
		adminToken = new JpaAdminToken(tokenValue);
	}

	// 토큰이 존재하고 밸리데이션 통과
	@Test
	public void testCheckToken() {
		
		// 값을 세팅한다.
		tokenList.add(adminToken);
		when(adminTokenRepository.findAll()).thenReturn(tokenList);
		
		// 실제 함수를 호출한다.
		adminTokenService.checkToken();
		
		// 값을 비교한다.
		// TODO : 리턴 값이 없는 함수를 호출하자!
		
	}
	
	// 토큰이 존재하고 밸리데이션 에러
		@Test
		public void testCheckTokenNoValidation() {
			
			// 값을 세팅한다.
			tokenList.add(adminToken);
			when(adminTokenRepository.findAll()).thenReturn(tokenList);
			when(sshService.executeSsh("pwd")).thenReturn("yaho");
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
		when(adminTokenRepository.findAll()).thenReturn(tokenList);
		when(sshService.executeSsh("pwd")).thenReturn("yaho");
		when(adminTokenRepository.save(adminToken)).thenReturn(adminToken);
		
		// 실제 함수를 호출한다.
		adminTokenService.checkToken();
		
		// 값을 비교한다.
		// TODO : 리턴 값이 없는 함수를 호출하자!
		
	}

}
