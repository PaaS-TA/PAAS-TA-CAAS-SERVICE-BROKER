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
	@JsonProperty("token_value")
	@Column(name = "token_value", length=1000)
	@Id
	private String tokenValue;

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

}
