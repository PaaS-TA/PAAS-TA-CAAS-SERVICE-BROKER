package org.openpaas.servicebroker.kubernetes.service.impl;

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

}
