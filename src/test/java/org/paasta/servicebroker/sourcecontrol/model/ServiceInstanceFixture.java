package org.paasta.servicebroker.sourcecontrol.model;

import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.fixture.RequestFixture;
import org.paasta.servicebroker.apiplatform.common.TestConstants;

public class ServiceInstanceFixture {

	public static ServiceInstance getServiceInstance() {

		return new ServiceInstance(
				RequestFixture.getCreateServiceInstanceRequest()
				.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001))
			.withDashboardUrl(TestConstants.DASHBOARD_URL + TestConstants.SV_INSTANCE_ID_001);
				
	}

	public static ServiceInstance getServiceInstance002() {

		return new ServiceInstance(
				RequestFixture.getCreateServiceInstanceRequest()
						.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_002))
				.withDashboardUrl(TestConstants.DASHBOARD_URL + TestConstants.SV_INSTANCE_ID_002);

	}

}
