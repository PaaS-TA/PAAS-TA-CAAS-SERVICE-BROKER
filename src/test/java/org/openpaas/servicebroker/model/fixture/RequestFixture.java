package org.openpaas.servicebroker.model.fixture;

import java.util.HashMap;
import java.util.Map;

import org.openpaas.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.PreviousValues;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.paasta.servicebroker.apiplatform.common.TestConstants;

public class RequestFixture {


    public static Map<String, Object> getParameters(String num) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("owner", "testuser"+num);
        parameters.put("org_name", "org_name_"+num);
        return parameters;
    }

    public static PreviousValues getPreviousValues() {

        CreateServiceInstanceRequest createServiceInstanceRequest = getCreateServiceInstanceRequest();

        return new PreviousValues(createServiceInstanceRequest.getPlanId(),
                createServiceInstanceRequest.getServiceDefinitionId(),
                createServiceInstanceRequest.getOrganizationGuid(),
                createServiceInstanceRequest.getSpaceGuid());

    }

    public static CreateServiceInstanceRequest getCreateServiceInstanceRequest() {

        ServiceDefinition service = ServiceFixture.getService();

        return new CreateServiceInstanceRequest(
                TestConstants.SERVICEDEFINITION_ID,
                TestConstants.SERVICEDEFINITION_PLAN_ID,
                TestConstants.ORG_GUID_001,
                TestConstants.SPACE_GUID_001,
                getParameters("001")
        );
    }

    public static CreateServiceInstanceRequest getCreateServiceInstanceRequest2() {

        ServiceDefinition service = ServiceFixture.getService();

        return new CreateServiceInstanceRequest(
                service.getId(),
                service.getPlans().get(0).getId(),
                TestConstants.ORG_GUID_002,
                TestConstants.SPACE_GUID_002,
                getParameters("002")
        );
    }
    
    public static CreateServiceInstanceRequest getCreateServiceInstanceRequestHyerin() {

        ServiceDefinition service = ServiceFixture.getService();

        return new CreateServiceInstanceRequest(
        		TestConstants.SERVICEDEFINITION_ID,
        		TestConstants.SERVICEDEFINITION_PLAN_ID,
                TestConstants.ORG_GUID_001,
                TestConstants.SPACE_GUID_001,
                getParameters("001")
        );
    }

    public static CreateServiceInstanceBindingRequest getCreateServiceInstanceBindingRequest() {

        ServiceDefinition service = ServiceFixture.getService();

        return new CreateServiceInstanceBindingRequest(
                service.getId(),
                service.getPlans().get(0).getId(),
                "app_guid");
    }

    public static DeleteServiceInstanceRequest getDeleteServiceInstanceRequest() {

        ServiceInstance service = ServiceInstanceFixture.getServiceInstance();

        return new DeleteServiceInstanceRequest(
                service.getServiceInstanceId(),
                service.getServiceDefinitionId(),
                service.getPlanId());
    }
    
    public static DeleteServiceInstanceRequest getDeleteServiceInstanceRequest2() {

        ServiceInstance service = ServiceInstanceFixture.getServiceInstance();

        return new DeleteServiceInstanceRequest(
        		TestConstants.SV_INSTANCE_ID_001,
        		TestConstants.SERVICEDEFINITION_ID,
                TestConstants.SERVICEDEFINITION_PLAN_ID);
    }

    public static UpdateServiceInstanceRequest getUpdateServiceInstanceRequest() {

        ServiceInstance service = ServiceInstanceFixture.getServiceInstance();
        
        return new UpdateServiceInstanceRequest(
        		TestConstants.SERVICEDEFINITION_PLAN_ID,
        		TestConstants.SERVICEDEFINITION_ID).withInstanceId(TestConstants.SV_INSTANCE_ID_001);
    }

    public static DeleteServiceInstanceBindingRequest getDeleteServiceInstanceBindingRequest() {

        ServiceInstance service = ServiceInstanceFixture.getServiceInstance();

        return new DeleteServiceInstanceBindingRequest(
                "test_bindingId",
                service,
                service.getServiceDefinitionId(),
                service.getPlanId());
    }

}
