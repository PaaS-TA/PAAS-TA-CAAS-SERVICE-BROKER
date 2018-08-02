package org.openpaas.servicebroker.kubernetes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 읽어들인 환경 변수를 가지고 있는 클래스
 *
 * @author Hyerin
 * @since 2018.07.24
 * @version 20180724
 */
@Component
public class EnvConfig {
	
	@Value("${dashboard.url}")
	String dashboardUrl;
	
	@Value("${caas.url}")
	String caasUrl;
	
	@Value("${caas.admin-token}")
	String adminToken;

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

	public String getDashboardUrl() {
		return dashboardUrl;
	}

	public void setDashboardUrl(String dashboardUrl) {
		this.dashboardUrl = dashboardUrl;
	}
}