package org.openpaas.servicebroker.caas.model;

/**
 * common DB에 저장하기 위한 REST 통신용 모델
 * roleName, roleSetCode는 상의하에 결정하는 것
 * @author Hyerin
 * @since 2018.08.22
 * @version 20180822
 */
public class User {
	
    private String id;

    private String userId;

	private String serviceInstanceId;

    private String caasNamespace;

    private String caasAccountTokenName;

    private String caasAccountName;

    private String organizationGuid;

    private String spaceGuid;

    private String roleSetCode;
    
    private String planName;
    
    private String planDescription;
        
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public void setServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	public String getCaasNamespace() {
		return caasNamespace;
	}

	public void setCaasNamespace(String caasNamespace) {
		this.caasNamespace = caasNamespace;
	}

	public String getCaasAccountTokenName() {
		return caasAccountTokenName;
	}

	public void setCaasAccountTokenName(String caasAccountTokenName) {
		this.caasAccountTokenName = caasAccountTokenName;
	}

	public String getCaasAccountName() {
		return caasAccountName;
	}

	public void setCaasAccountName(String caasAccountName) {
		this.caasAccountName = caasAccountName;
	}

	public String getOrganizationGuid() {
		return organizationGuid;
	}

	public void setOrganizationGuid(String organizationGuid) {
		this.organizationGuid = organizationGuid;
	}

	public String getSpaceGuid() {
		return spaceGuid;
	}

	public void setSpaceGuid(String spaceGuid) {
		this.spaceGuid = spaceGuid;
	}

	public String getRoleSetCode() {
		return roleSetCode;
	}
	
	public void setRoleSetCode(String roleSetCode) {
		this.roleSetCode = roleSetCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanDescription() {
		return planDescription;
	}

	public void setPlanDescription(String planDescription) {
		this.planDescription = planDescription;
	}

}
