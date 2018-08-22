package org.openpaas.servicebroker.kubernetes.controller;

import org.openpaas.servicebroker.kubernetes.model.JpaAdminToken;
import org.openpaas.servicebroker.kubernetes.repo.JpaAdminTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tokens")
public class AdminTokenController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminTokenController.class);
	
	@Autowired
	JpaAdminTokenRepository jpaAdminTokenRepository;

	@PostMapping
	public JpaAdminToken createToken (@RequestBody JpaAdminToken adminToken) {
		logger.info("Create new token RequestBody is {}" , adminToken);
		return jpaAdminTokenRepository.save(adminToken);
	}

}
