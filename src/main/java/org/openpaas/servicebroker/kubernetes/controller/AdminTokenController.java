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


/**
 * kuber-master에서 set-context한 후 token값 DB저장을 위해 호출하는 REST API용 컨트롤러
 * @author Hyerin
 * @since 2018.08.22
 * @version 20180822
 */
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
