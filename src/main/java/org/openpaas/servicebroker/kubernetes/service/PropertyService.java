package org.openpaas.servicebroker.kubernetes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * The type Property service.
 */
@Service
public class PropertyService {

    /**
     * The caas master host url.
     */
    @Value("${caas.cluster.url}")
    private String caasClusterUrl;
    
    @Value("${caas.cluster.port}")
    private int caasClusterPort;
    
    @Value("${caas.cluster.user.name}")
    private String caasClusterUserName;
    
    @Value("${caas.cluster.user.password}")
    private String caasClusterUserPassword;
	
    @Value("${dashboard.url}")
    private String dashboardUrl;
    
    @Value("${caas.url}")
    private String caasUrl;
    
    @Value("${caas.admin-token}")
    private String adminToken;
    
    @Value("${caas.common.url}")
    private String commonUrl;

	public String getCaasClusterUrl() {
		return caasClusterUrl;
	}

	public void setCaasClusterUrl(String caasClusterUrl) {
		this.caasClusterUrl = caasClusterUrl;
	}

	public int getCaasClusterPort() {
		return caasClusterPort;
	}

	public void setCaasClusterPort(int caasClusterPort) {
		this.caasClusterPort = caasClusterPort;
	}

	public String getCaasClusterUserName() {
		return caasClusterUserName;
	}

	public void setCaasClusterUserName(String caasClusterUserName) {
		this.caasClusterUserName = caasClusterUserName;
	}

	public String getCaasClusterUserPassword() {
		return caasClusterUserPassword;
	}

	public void setCaasClusterUserPassword(String caasClusterUserPassword) {
		this.caasClusterUserPassword = caasClusterUserPassword;
	}

    public String getCaasUrl() {
        return caasUrl;
    }

    public void setCaasUrl(String caasUrl) {
        this.caasUrl = caasUrl;
    }

    public String getAdminToken() {
        return adminToken;
    }

    public void setAdminToken(String adminToken) {
        this.adminToken = adminToken;
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

}
