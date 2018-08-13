package org.openpaas.servicebroker.test;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.openpaas.servicebroker.kubernetes.service.PropertyService;

public class PropertyTest {
	
	@InjectMocks
	PropertyService property;
	
	@Before
	public void setUp() {
		property = new PropertyService();
		property.setCaasClusterPort(22);
		property.setCaasClusterUrl("www.test.com");
		property.setCaasClusterUserName("test-user");
		property.setCaasClusterUserPassword("test-pw");
		property.getCaasClusterPort();
		property.getCaasClusterUrl();
		property.getCaasClusterUserName();
		property.getCaasClusterUserPassword();
	}

	@Test
	public void test() {
		
	}

}
