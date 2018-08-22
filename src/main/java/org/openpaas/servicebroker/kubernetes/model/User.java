package org.openpaas.servicebroker.kubernetes.model;


public class User {
	
    private String id;

    private String userId;

	private String serviceInstanceId;

    private String caasNamespace;

    private String caasAccountTokenName;

    private String caasAccountName;

    private String organizationGuid;

    private String spaceGuid;

    private String roleName = "ROLE_ADMIN";

    private String roleSetCode = "0001";
        
    
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

}
