package org.openpaas.servicebroker.kubernetes.service.impl;

import org.openpaas.servicebroker.kubernetes.config.EnvConfig;
import org.openpaas.servicebroker.kubernetes.repo.JpaAdminTokenRepository;
import org.openpaas.servicebroker.kubernetes.service.RestTemplateService;
import org.openpaas.servicebroker.kubernetes.service.SshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminTokenService {

	@Autowired
	JpaAdminTokenRepository adminTokenRepository;
	
	@Autowired
	SshService sshService;
	
	@Autowired
	EnvConfig envConfig;	
	
	@Autowired
	RestTemplateService restTemplateService;
	
//	public String getAdminToken() {
//		return adminTokenRepository.getOne(envConfig.getAdminToken()).getTokenValue();
//	}
	
	public void setContext() {
		sshService.executeSsh("pwd");
	}
	
	private boolean tokenExist() {
		return adminTokenRepository.exists(envConfig.getAdminToken());
	}
	
	//이거 수정해야 함.
	public boolean tokenValidation() {
		return restTemplateService.tokenValidation();
	}
	
	public void checkToken() {
		
		// 토큰이 존재하지 않을 때
		if(!tokenExist()) {
			setContext();
			// set Context를 한 뒤에 broker DB에 token 값을 저장해야 하는데 어떻게 함?
			//adminTokenRepository.save(new JpaAdminToken(getAdminToken()));
			return;
		}
		
		// 토큰이 존재하고, 갱신이 필요할 때
		if(!tokenValidation()) {
			setContext();
			//adminTokenRepository.save(new JpaAdminToken(getAdminToken()));
			return;
		}
			
	}
}
