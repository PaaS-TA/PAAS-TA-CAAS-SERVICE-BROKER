package org.openpaas.servicebroker.model.fixture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openpaas.servicebroker.model.ServiceDefinition;
import org.paasta.servicebroker.apiplatform.common.TestConstants;


public class ServiceFixture {

    public static List<ServiceDefinition> getAllServices() {
        List<ServiceDefinition> services = new ArrayList<ServiceDefinition>();
        services.add(ServiceFixture.getService());
        return services;
    } 
    
    public static ServiceDefinition getService() {
        return new ServiceDefinition(
                "8a3f2d14-5283-487f-b6c8-6663639ad8b1", 
                "caas-kubernetes", 
                "for CaaS Plans, You can choose plan about CPU, Memory, disk.", 
                false,
                PlanFixture.getAllPlans());
    }

    public static List<ServiceDefinition> getCatalog() {
        List<ServiceDefinition> result = new ArrayList<>();
        result.add(getService());

        return result;
    }
    
}
/*return new Catalog( Arrays.asList(
new ServiceDefinition(
SERVICEDEFINITION_ID,
SERVICEDEFINITION_NAME,
SERVICEDEFINITION_DESC,
SERVICEDEFINITION_BINDABLE, // bindable
SERVICEDEFINITION_PLANUPDATABLE, // updatable
Arrays.asList(
    new Plan( SERVICEDEFINITION_PLAN1_ID,
        SERVICEDEFINITION_PLAN1_NAME,
        SERVICEDEFINITION_PLAN1_DESC,
        getPlanMetadata( SERVICEDEFINITION_PLAN1_TYPE ),
        true,
        SERVICEDEFINITION_PLAN1_CPU,
        SERVICEDEFINITION_PLAN1_MEMORY,
        SERVICEDEFINITION_PLAN1_DISK,
        SERVICEDEFINITION_PLAN1_WEIGHT ),
    new Plan( SERVICEDEFINITION_PLAN2_ID,
        SERVICEDEFINITION_PLAN2_NAME,
        SERVICEDEFINITION_PLAN2_DESC,
        getPlanMetadata( SERVICEDEFINITION_PLAN2_TYPE ),
        true,
        SERVICEDEFINITION_PLAN2_CPU,
        SERVICEDEFINITION_PLAN2_MEMORY,
        SERVICEDEFINITION_PLAN2_DISK,
        SERVICEDEFINITION_PLAN2_WEIGHT ),
    new Plan( SERVICEDEFINITION_PLAN3_ID,
        SERVICEDEFINITION_PLAN3_NAME,
        SERVICEDEFINITION_PLAN3_DESC,
        getPlanMetadata( SERVICEDEFINITION_PLAN3_TYPE ),
        true,
        SERVICEDEFINITION_PLAN3_CPU,
        SERVICEDEFINITION_PLAN3_MEMORY,
        SERVICEDEFINITION_PLAN3_DISK,
        SERVICEDEFINITION_PLAN3_WEIGHT ) ),
SERVICEDEFINITION_TAGS,
getServiceDefinitionMetadata(),
null,
null ) ) );*/