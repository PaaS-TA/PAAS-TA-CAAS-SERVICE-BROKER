package org.openpaas.servicebroker.kubernetes.service.impl;

import org.openpaas.servicebroker.kubernetes.model.JpaServiceInstance;
import org.openpaas.servicebroker.kubernetes.model.User;
import org.openpaas.servicebroker.kubernetes.service.RestTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	RestTemplateService restTemplateService;
	
	public void request(JpaServiceInstance jpaInstance, HttpMethod httpMethod) {
		restTemplateService.requestUser(convert(jpaInstance), httpMethod);
	}

	private User convert(JpaServiceInstance jpaInstance) {
		User user = new User();
		BeanUtils.copyProperties(jpaInstance, user);
		user.setUserId(jpaInstance.getParameter("owner"));
		user.setId(null);
		return user;
	}
}
