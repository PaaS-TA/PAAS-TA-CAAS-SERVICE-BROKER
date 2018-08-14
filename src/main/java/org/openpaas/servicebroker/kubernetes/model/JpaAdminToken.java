package org.openpaas.servicebroker.kubernetes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "admin_token")
public class JpaAdminToken {
	
	@JsonSerialize
	@JsonProperty("token_name")
	@Column(name = "token_name")
	@Id
	private String tokenName;
	
	@JsonSerialize
	@JsonProperty("token_value")
	@Column(name = "token_value", length=1000)
	private String tokenValue;
	
	public JpaAdminToken() {
		
	}
	
	public JpaAdminToken(String tokenValue) {
		this.tokenName = "caas_admin";
		this.tokenValue = tokenValue;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

}
