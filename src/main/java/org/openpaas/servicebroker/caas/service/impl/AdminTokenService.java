package org.openpaas.servicebroker.caas.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.openpaas.servicebroker.caas.model.Constants;
import org.openpaas.servicebroker.caas.repo.JpaAdminTokenRepository;
import org.openpaas.servicebroker.caas.service.PropertyService;
import org.openpaas.servicebroker.caas.service.RestTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Admin token에 관한 서비스 
 * @author Hyerin
 * @since 2018.08.22
 * @version 20180822
 */
@Service
public class AdminTokenService {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminTokenService.class);

	@Autowired
	JpaAdminTokenRepository adminTokenRepository;
	
	@Autowired
	PropertyService propertyService;	
	
	@Autowired
	RestTemplateService restTemplateService;
	
	/**
	 * broker DB에 token이 없을 경우, ssh통신으로 set-context 명령어 호출 
	 * 반환값음 없다.
	 * @author Hyerin
	 * @since 2018.08.22
	 * @version 20180822
	 */
	public void setContext() {
		int count = 0;
		boolean existToken = false;
		logger.info("execute ssh command to caas master server to set admin token");
		String[] cmd = {"/bin/bash","-c",""};
		cmd[2] = "sudo " + propertyService.getCaasClusterCommand();
		Process p = null;
		logger.info("command check {}", propertyService.getCaasClusterCommand());
		
		try {
			p = Runtime.getRuntime().exec(cmd);
		    String line;
		    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    while ((line = input.readLine()) != null) {
		        logger.info("{}", line);
		        // required true
		        if(line.equals(propertyService.getCaasClusterExitCode())) {
		        	logger.info("end of script ");
		        	input.close();
		        	break;
		        }
		    }  
		    p.destroy();
			
		} catch (IOException e) {
			logger.error("Something Wrong!!");
			e.printStackTrace();
		}
		
		// token값 DB 동기화를 기다린다. 10초 기다려서 안되면 일단 로그찍고 넘어간다.
		while(!existToken) {
			logger.info("waiting token.......{}", existToken);
			existToken = tokenExist();
			try {
				Thread.sleep(1000);
				count++;
			} catch (InterruptedException e) {
				logger.info("InterruptedException occured.");
			}
			
			if(count == 10) {
				logger.error("Check your CaaS maseter Node IP or Network status");
				return;
			}
		}
			
	}
	
	public boolean tokenExist() {
		return adminTokenRepository.exists(Constants.TOKEN_KEY);
	}
	
	public boolean tokenValidation() {
		return restTemplateService.tokenValidation();
	}
	
	/**
	 * 토큰의 존재유무, 값의 확인을 위한 함수
	 * @author Hyerin
	 * @since 2018.08.22
	 * @version 20180822
	 */
	public void checkToken() {
		logger.info("token check");
		
		// 토큰이 존재하지 않을 때
		if(!tokenExist()) {
			logger.info("does not exist token, So set admin token");
			setContext();
			return;
		}
		
		// 토큰이 존재하고, 갱신이 필요할 때
		if(!tokenValidation()) {
			setContext();
			return;
		}
			
	}
}
