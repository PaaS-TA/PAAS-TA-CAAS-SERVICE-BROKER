package org.openpaas.servicebroker.kubernetes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Persistent;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * CaaS service broker에서는 bind/unbind를 쓰지 않기로 했으나, service binding에 대한 동작은 남겨두어야 하는 것으로 판단했고,
 * 따라서 binding/unbinding에 대한 entity 모델 클래스 자체는 남겨둡니다.
 * @author Hyungu Cho
 * @since 2018.07.24
 * @version 20180724
 */
@Entity
@Table(name="service_binding")
public class JpaServiceInstanceBinding {
    @Id
    @Column(name="binding_id")
    private String bindingId;
    
    @Column(name="instance_id")
    private String serviceInstanceId;

    private String appGuid;

    @Transient
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "username")
    @Column(name = "password")
    @CollectionTable(name = "binding_credentials", joinColumns = {@JoinColumn(name = "binding_id")})
    private Map<String, Object> credentials = new HashMap();

    @Transient
    private String syslogDrainUrl;

    @JsonIgnore
    @Transient
    private HttpStatus httpStatus;

    JpaServiceInstanceBinding() {}

    public JpaServiceInstanceBinding(String bindingId, String serviceInstanceId, Map<String, Object> credentials, String syslogDrainUrl, String appGuid) {
        this.bindingId = bindingId;
        this.serviceInstanceId = serviceInstanceId;
        this.appGuid = appGuid;
        this.credentials = credentials;
        this.syslogDrainUrl = syslogDrainUrl;
        this.httpStatus = HttpStatus.CREATED;
    }

    //여기에 필요한 함수들 추가바람

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    public String getAppGuid() {
        return appGuid;
    }

    public void setAppGuid(String appGuid) {
        this.appGuid = appGuid;
    }

    public Map<String, Object> getCredentials() {
        return credentials;
    }

    public void setCredentials(Map<String, Object> credentials) {
        this.credentials = credentials;
    }

    public String getSyslogDrainUrl() {
        return syslogDrainUrl;
    }

    public void setSyslogDrainUrl(String syslogDrainUrl) {
        this.syslogDrainUrl = syslogDrainUrl;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatusOK() {
        this.httpStatus = HttpStatus.OK;
    }
}
