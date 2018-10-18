package org.openpaas.servicebroker.caas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * kuber와 관련 api를 호출 할 때 필요한 admin token을 저장하기 위한 model
 * token_name은 "caas_admin"으로 고정, property로 빼는 것이 좋은 것은 알지만
 * 릴리즈 수정을 줄이기 위함.
 * @author Hyerin
 * @since 2018.08.22
 * @version 20180822
 */
@Entity
@Table(name = "admin_token")
public class JpaAdminToken {
	
	@JsonSerialize
	@JsonProperty("token_name")
	@Column(name = "token_name")
	@Id
	private final String tokenName = Constants.TOKEN_KEY;
	
	@JsonSerialize
	@JsonProperty("token_value")
	@Column(name = "token_value", length=1000)
	private String tokenValue;
	
	public JpaAdminToken() {
		
	}
	
	public JpaAdminToken(String tokenValue) {
		this.tokenValue = tokenValue;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

}
