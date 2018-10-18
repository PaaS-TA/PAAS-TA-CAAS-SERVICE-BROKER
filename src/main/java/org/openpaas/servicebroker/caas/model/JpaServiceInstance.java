package org.openpaas.servicebroker.caas.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Service Instance에 대한 Entity 모델 클래스 (JPA를 이용해서 사용) <br>
 * (Created by Hyerin on 2018-04-10. thanks to Mingu!)
 * 
 * @author Hyerin
 * @author Hyungu Cho
 * @version 20180724
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@Entity
@Table(name = "service_instance")
public class JpaServiceInstance extends ServiceInstance {
	@JsonSerialize
	@JsonProperty("service_id")
	@Column(name = "service_definition_id")
	private String serviceDefinitionId;

	@JsonSerialize
	@JsonProperty("organization_guid")
	@Column(name = "organization_guid")
	private String organizationGuid;

	@JsonSerialize
	@JsonProperty("space_guid")
	@Column(name = "space_guid")
	private String spaceGuid;

	@JsonSerialize
	@JsonProperty("caas_namespace")
	@Column(name = "caas_namespace")
	private String caasNamespace;

	@JsonSerialize
	@JsonProperty("caas_account_name")
	@Column(name = "caas_account_name")
	private String caasAccountName;

	@JsonSerialize
	@JsonProperty("caas_account_token_name")
	@Column(name = "caas_account_token_name")
	private String caasAccountTokenName;
	
	@JsonSerialize
	@JsonProperty("user_id")
	@Column(name = "user_id")
	private String userId;

	@JsonIgnore
	@Transient
	private Map<String, Object> parameters;

	public JpaServiceInstance() {
		super(); // empty datum,
		setParameters(new HashMap<>()); // and empty params.
	}

	/**
	 * CreateServiceInstanceRequest가 들어왔을 경우의 생성자 (서비스 인스턴스 생성)
	 * 
	 * @param request
	 */
	public JpaServiceInstance(CreateServiceInstanceRequest request) {
		// service (definition) id, plan id, org guid, space guid, service instance id
		super(request);

		// default setting
		setServiceDefinitionId(request.getServiceDefinitionId());
		setPlanId(request.getPlanId());
		setOrganizationGuid(request.getOrganizationGuid());
		setSpaceGuid(request.getSpaceGuid());
		setServiceInstanceId(request.getServiceInstanceId());

		// only JpaServiceInstance
		setParameters(request.getParameters());
		setAdditionalParameters(getParameters());
	}

	/**
	 * DeleteServiceInstanceRequest가 들어왔을 경우의 생성자 (서비스 인스턴스 삭제)
	 * 
	 * @param request
	 */
	public JpaServiceInstance(DeleteServiceInstanceRequest request) {
		// service (definition) id, service instance id, plan id
		super(request);

		setServiceDefinitionId(request.getServiceId());
		setServiceInstanceId(request.getServiceInstanceId());
		setPlanId(request.getPlanId());
	}

	/**
	 * UpdateServiceInstanceRequest가 들어왔을 경우의 생성자 (서비스 인스턴스 수정)
	 * 
	 * @param request
	 */
	public JpaServiceInstance(UpdateServiceInstanceRequest request) {
		// service (definition) id, service instance id, plan id
		super(request);

		setServiceDefinitionId(request.getServiceDefinitionId());
		setServiceInstanceId(request.getServiceInstanceId());
		setPlanId(request.getPlanId());

		setParameters(request.getParameters());
		setAdditionalParameters(getParameters());
	}

	/**
	 * 서비스 인스턴스 생성/수정시 들어오는 파라미터가 있을 경우, 해당 파라미터를 설정해주기 위한 메소드.
	 * 
	 * @param params
	 */
	private void setAdditionalParameters(final Map<String, Object> params) {
		Object caasNamespace = params.get("caas_namespace");
		Object caasAccountName = params.get("caas_account_name");
		Object caasAccountAccessToken = params.get("caas_account_access_token");

		setCaasNamespace(null == caasNamespace ? null : caasNamespace.toString());
		setCaasAccountName(null == caasAccountName ? null : caasAccountName.toString());
		setCaasAccountTokenName(null == caasAccountAccessToken ? null : caasAccountAccessToken.toString());
	}

	@Id
	@Column(name = "service_instance_id")
	public String getServiceInstanceId() {
		return super.getServiceInstanceId();
	}

	@Override
	public void setServiceInstanceId(String serviceInstanceId) {
		super.setServiceInstanceId(serviceInstanceId);
	}

	public String getServiceDefinitionId() {
		return this.serviceDefinitionId;
	}

	private void setServiceDefinitionId(String id) {
		this.serviceDefinitionId = id;
	}

	@Column(name = "plan_id")
	public String getPlanId() {
		return super.getPlanId();
	}

	@Override
	public void setPlanId(String planId) {
		super.setPlanId(planId);
	}

	public String getOrganizationGuid() {
		return this.organizationGuid;
	}

	private void setOrganizationGuid(String orgGuid) {
		this.organizationGuid = orgGuid;
	}

	public String getSpaceGuid() {
		return this.spaceGuid;
	}

	private void setSpaceGuid(String spaceGuid) {
		this.spaceGuid = spaceGuid;
	};

	@Column(name = "dashboard_url")
	public String getDashboardUrl() {
		return super.getDashboardUrl();
	}

	private void setDashboardUrl(String dashboardUrl) {
		super.withDashboardUrl(dashboardUrl);
	}

	//// Under methods : Additional parameters on ServiceInstance ////
	public String getCaasNamespace() {
		return caasNamespace;
	}

	public void setCaasNamespace(String caasNamespace) {
		this.caasNamespace = caasNamespace;
	}

	public String getCaasAccountName() {
		return caasAccountName;
	}

	public void setCaasAccountName(String caasAccountName) {
		this.caasAccountName = caasAccountName;
	}

	public String getCaasAccountTokenName() {
		return caasAccountTokenName;
	}

	public void setCaasAccountTokenName(String caasAccountTokenName) {
		this.caasAccountTokenName = caasAccountTokenName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Transient
	public Map<String, Object> getParameters() {
		return parameters;
	}

	@Transient
	public void setParameters(Map<String, Object> parameters) {
		if (null == parameters)
			this.parameters = new HashMap<>();
		else
			this.parameters = parameters;
	}

	public String getParameter(String key) {
		if (null == key || "".equals(key))
			return null;

		if (getParameters().containsKey(key))
			return getParameters().get(key).toString();
		else
			return null;
	}

	/**
	 * JpaServiceInstance의 동일성 비교를 위한 메소드. service instance id와 service definition
	 * id가 모두 일치할 경우, 동일한 객체로 인식하고 true를 반환한다.
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (false == obj instanceof JpaServiceInstance)
			return false;

		JpaServiceInstance other = (JpaServiceInstance) obj;
		if (this.getServiceInstanceId().equals(other.getServiceInstanceId()) && this.getServiceDefinitionId().equals(other.getServiceDefinitionId())) {
			// Plan, account name, account token은 변경될 수 있는 값이므로,
			// ID로 쓰일 수 있는 값들만으로 equals의 조건문을 구성할 것.
			return true;
		}

		return false;
	}
}