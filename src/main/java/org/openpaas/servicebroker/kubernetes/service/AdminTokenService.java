package org.openpaas.servicebroker.kubernetes.service;

import org.openpaas.servicebroker.kubernetes.repo.JpaAdminTokenRepository;
import org.openpaas.servicebroker.kubernetes.service.impl.SshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminTokenService {

	@Autowired
	JpaAdminTokenRepository adminTokenRepository;
	
	@Autowired
	SshService sshService;
	
	
	public void getAdminToken() {
		// TODO : Database에서 가져오는 함수로 할까?
		//return token;
	}
	
	public String setContext() {
		// TODO : 실제로는 setContext 한 후의 결과값을 받아와야 하는데;;
		return sshService.executeSsh("pwd");
	}
	
	private boolean tokenExist() {
		if(adminTokenRepository.findAll().size() == 0) {return false;}
		return true;
	}
	
	public boolean tokenValidation(String token) {
		return adminTokenRepository.findAll().get(0).equals(token);
	}
	
}
