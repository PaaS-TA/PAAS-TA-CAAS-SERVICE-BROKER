package org.openpaas.servicebroker.kubernetes.model;

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
 * Created by Hyerin on 2018-04-10. thanks to Mingu!
 * This Class made for using JPA
 * @author Hyerin
 * @author Hyungu Cho
 * @version 20180724
 */
@JsonAutoDetect(
    getterVisibility = JsonAutoDetect.Visibility.NONE
)
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
    @JsonProperty("kubernetes_namespace")
    @Column(name = "kubernetes_namespace")
    private String kubernetesNamespace;

    @JsonSerialize
    @JsonProperty("kubernetes_account_name")
    @Column(name = "kubernetes_account_name")
    private String kubernetesAccountName;

    @JsonSerialize
    @JsonProperty("kubernetes_account_access_token")
    @Column(name = "kubernetes_account_access_token")
    private String kubernetesAccountAccessToken;

    @JsonIgnore
    @Transient
    private Map<String, Object> parameters;

	public JpaServiceInstance() {
	    super();                                // empty datum,
	    setParameters( new HashMap<>() );      // and empty params.
    }

    @Deprecated
    public JpaServiceInstance(ServiceInstance instance) {
        super();

        super.setServiceInstanceId( instance.getServiceInstanceId() );
        super.setPlanId( instance.getPlanId() );
	    if (instance instanceof JpaServiceInstance) {
            // default setting
            setServiceDefinitionId( instance.getServiceDefinitionId() );
            setOrganizationGuid( instance.getOrganizationGuid() );
            setSpaceGuid( instance.getSpaceGuid() );

            // only JpaServiceInstance
            setParameters( ( ( JpaServiceInstance ) instance ).getParameters() );
            setAdditionalParameters( getParameters() );
        }
    }

    public JpaServiceInstance(CreateServiceInstanceRequest request) {
	    // service (definition) id, plan id, org guid, space guid, service instance id
	    super(request);

	    // default setting
	    setServiceDefinitionId( request.getServiceDefinitionId() );
	    setPlanId( request.getPlanId() );
	    setOrganizationGuid( request.getOrganizationGuid() );
	    setSpaceGuid( request.getSpaceGuid() );
	    setServiceInstanceId( request.getServiceInstanceId() );

	    // only JpaServiceInstance
	    setParameters( request.getParameters() );
        setAdditionalParameters( getParameters() );
    }

    public JpaServiceInstance(DeleteServiceInstanceRequest request) {
        // service (definition) id, service instance id, plan id
        super(request);

        setServiceDefinitionId( request.getServiceId() );
        setServiceInstanceId( request.getServiceInstanceId() );
        setPlanId( request.getPlanId() );
    }

    public JpaServiceInstance(UpdateServiceInstanceRequest request) {
        // service (definition) id, service instance id, plan id
        super(request);

        setServiceDefinitionId( request.getServiceDefinitionId() );
        setServiceInstanceId( request.getServiceInstanceId() );
        setPlanId( request.getPlanId() );

        setParameters( request.getParameters() );
        setAdditionalParameters( getParameters() );
    }

    private void setAdditionalParameters(final Map<String, Object> params ) {
        Object kubernetesNamespace = params.get( "kubernetes_namespace" );
        Object kubernetesAccountName = params.get( "kubernetes_account_name" );
        Object kubernetesAccountAccessToken = params.get( "kubernetes_account_access_token" );

        setKubernetesNamespace( null == kubernetesNamespace? null : kubernetesNamespace.toString() );
        setKubernetesAccountName( null == kubernetesAccountName? null : kubernetesAccountName.toString() );
        setKubernetesAccountAccessToken( null == kubernetesAccountAccessToken? null : kubernetesAccountAccessToken.toString() );
    }

    @Id
    @Column(name = "service_instance_id" )
    public String getServiceInstanceId() { return super.getServiceInstanceId(); }
    @Override
    public void setServiceInstanceId ( String serviceInstanceId ) { super.setServiceInstanceId( serviceInstanceId ); }

    public String getServiceDefinitionId() { return this.serviceDefinitionId; }
    private void setServiceDefinitionId(String id) { this.serviceDefinitionId = id; }

    @Column(name = "plan_id" )
    public String getPlanId() { return super.getPlanId(); }
    @Override
    public void setPlanId ( String planId ) { super.setPlanId( planId ); }

    public String getOrganizationGuid() { return this.organizationGuid; }
    private void setOrganizationGuid(String orgGuid) { this.organizationGuid = orgGuid; }

    public String getSpaceGuid() { return this.spaceGuid; }
    private void setSpaceGuid(String spaceGuid) { this.spaceGuid = spaceGuid; };

    @Column(name = "dashboard_url" )
    public String getDashboardUrl() { return super.getDashboardUrl(); }
    private void setDashboardUrl(String dashboardUrl) { super.withDashboardUrl( dashboardUrl ); }

    //// Under methods : Additional parameters on ServiceInstance ////
    public String getKubernetesNamespace() { return kubernetesNamespace; }
    public void setKubernetesNamespace(String kubernetesNamespace) { this.kubernetesNamespace = kubernetesNamespace; }

    public String getKubernetesAccountName () { return kubernetesAccountName; }
    public void setKubernetesAccountName ( String kubernetesAccountName ) { this.kubernetesAccountName = kubernetesAccountName; }

    public String getKubernetesAccountAccessToken () { return kubernetesAccountAccessToken; }
    public void setKubernetesAccountAccessToken ( String kubernetesAccountAccessToken ) { this.kubernetesAccountAccessToken = kubernetesAccountAccessToken; }

    @Transient
    public Map<String, Object> getParameters() { return parameters; }
    @Transient
    public void setParameters ( Map<String, Object> parameters ) {
        if ( null == parameters )
            this.parameters = new HashMap<>();
        else
            this.parameters = parameters;
    }

    public String getParameter(String key) {
        if (null == key || "".equals( key ))
            return null;

        if (getParameters().containsKey( key ))
            return getParameters().get( key ).toString();
        else
            return null;
    }

    @Override
    public boolean equals ( Object obj ) {
	    if ( this == obj )
	        return true;

        if ( false == obj instanceof JpaServiceInstance )
            return false;

        JpaServiceInstance other = ( JpaServiceInstance ) obj;
        if ( this.getServiceInstanceId().equals( other.getServiceInstanceId() ) &&
            this.getServiceDefinitionId().equals( other.getServiceDefinitionId() )
        ) {
            // Plan, account name, account token은 변경될 수 있는 값이므로,
            // ID로 쓰일 수 있는 값들만으로 equals의 조건문을 구성할 것.
            return true;
        }

        return false;
    }
}