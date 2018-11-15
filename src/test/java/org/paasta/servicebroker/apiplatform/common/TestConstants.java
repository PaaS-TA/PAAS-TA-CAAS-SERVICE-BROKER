package org.paasta.servicebroker.apiplatform.common;

/**
 * Created by user on 2017-09-12.
 */
public class TestConstants {


    public static final String Headers_KEY_API_VERSION = "X-Broker-Api-Version";
    public static final String Headers_KEY_AUTHORIZATION = "Authorization";

    public static final String PARAM_KEY_OWNER = "owner";
    public static final String PARAM_KEY_OWNER_VALUE = "testuser001";

    public static final String PARAM_SERVICE_TYPE_SHARED = "Shared";
    public static final String PARAM_SERVICE_TYPE_DEDICATED = "Dedicated";


    public static final String SERVICEDEFINITION_ID = "8a3f2d14-5283-487f-b6c8-6663639ad8b1";
    public static final String SERVICEDEFINITION_NAME = "caas-kubernetes";
    public static final String SERVICEDEFINITION_PLAN_ID = "f02690d6-6965-4756-820e-3858111ed674";

    public static final String ORG_GUID_001 = "org_guid_001";
    public static final String ORG_GUID_002 = "org_guid_002";

    public static final String SPACE_GUID_001 = "space_guid_001";
    public static final String SPACE_GUID_002 = "space_guid_002";

    public static final String SV_INSTANCE_ID_001 = "sv_instance_id_001";
    public static final String SV_INSTANCE_ID_002 = "sv_instance_id_002";

    public static final String DASHBOARD_URL = "http://test-dashboardurl";
    public static final String ROLE_SET_CODE = "RS0001";
    public static final String PLAN_NAME = "Small";
    public static final String PLAN_DESC = "4 CPUs, 6GB Memory";


    public static final String BINDING_ID = "service_instance_binding_id";
   
    public static final String JPA_SERVICE_DEFINITION_ID = SERVICEDEFINITION_ID;
    public static final String JPA_ORGANIZTION_GUID = ORG_GUID_001;
    public static final String JPA_SPACE_GUID = SPACE_GUID_001;
    public static final String JPA_CAAS_NAMESPACE = "paas-" + SV_INSTANCE_ID_001 + "-caas";
    public static final String JPA_CAAS_ACCOUNT_NAME = PARAM_KEY_OWNER_VALUE + "-admin";
    public static final String JPA_CAAS_ACCOUNT_ACCESS_TOKEN = JPA_ORGANIZTION_GUID + JPA_CAAS_ACCOUNT_NAME + "-qwert";


}
