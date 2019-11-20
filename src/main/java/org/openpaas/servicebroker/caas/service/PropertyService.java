package org.openpaas.servicebroker.caas.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Property 변수를 담은 서비스 클래스
 * @author Hyerin
 * @since 2018.08.22
 * @version 20180822
 */
@Service
public class PropertyService {

    /**
     * The caas master host url.
     */
    @Value("${caas.cluster.command}")
    private String caasClusterCommand;
    
	@Value("${caas.cluster.exit-code}")
    private String caasClusterExitCode;
	
    @Value("${dashboard.url}")
    private String dashboardUrl;
    
    @Value("${caas.url}")
    private String caasUrl;
       
    @Value("${caas.common.url}")
    private String commonUrl;
    
    @Value("${caas.common.id}")
    private String commonId;
    
    @Value("${caas.common.password}")
    private String commonPassword;

    @Value("${roleSet.administratorCode}")
    private String roleSetCode;

    @Value("${private.docker.registry.auth.id}")
	private String authId;

	@Value("${private.docker.registry.auth.password}")
	private String authPassword;

	@Value("${private.docker.registry.uri}")
	private String privateDockerUri;

	@Value("${private.docker.registry.port}")
	private String privateDockerPort;

	@Value("${private.docker.registry.secret.name}")
	private String privateDockerSecretName;

    public String getCaasClusterCommand() {
		return caasClusterCommand;
	}

	public void setCaasClusterCommand(String caasClusterCommand) {
		this.caasClusterCommand = caasClusterCommand;
	}

    public String getCaasClusterExitCode() {
		return caasClusterExitCode;
	}

	public void setCaasClusterExitCode(String caasClusterExitCode) {
		this.caasClusterExitCode = caasClusterExitCode;
	}

	public String getCaasUrl() {
        return caasUrl;
    }

    public void setCaasUrl(String caasUrl) {
        this.caasUrl = caasUrl;
    }

    public String getCommonId() {
		return commonId;
	}

	public void setCommonId(String commonId) {
		this.commonId = commonId;
	}

	public String getCommonPassword() {
		return commonPassword;
	}

	public void setCommonPassword(String commonPassword) {
		this.commonPassword = commonPassword;
	}

	public String getDashboardUrl(String serviceInstanceId) {
        return dashboardUrl + serviceInstanceId;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

	public String getCommonUrl() {
		return commonUrl;
	}

	public void setCommonUrl(String commonUrl) {
		this.commonUrl = commonUrl;
	}

	public String getRoleSetCode() {
		return roleSetCode;
	}

	public void setRoleSetCode(String roleSetCode) {
		this.roleSetCode = roleSetCode;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getAuthPassword() {
		return authPassword;
	}

	public void setAuthPassword(String authPassword) {
		this.authPassword = authPassword;
	}

	public String getPrivateDockerUri() {
		return privateDockerUri;
	}

	public void setPrivateDockerUri(String privateDockerUri) {
		this.privateDockerUri = privateDockerUri;
	}

	public String getPrivateDockerPort() {
		return privateDockerPort;
	}

	public void setPrivateDockerPort(String privateDockerPort) {
		this.privateDockerPort = privateDockerPort;
	}

	public String getPrivateDockerSecretName() {
		return privateDockerSecretName;
	}

	public void setPrivateDockerSecretName(String privateDockerSecretName) {
		this.privateDockerSecretName = privateDockerSecretName;
	}
}
