package org.openpaas.servicebroker.kubernetes.exception;

import org.openpaas.servicebroker.exception.ServiceBrokerException;

/**
 * Kubernetes 서비스 관련 에러 Exception 클래스
 *
 * @author Hyungu Cho
 * @version 20180724
 */
public class KubernetesServiceException extends ServiceBrokerException {

    private static final long serialVersionUID = 4604824405877022727L;

    public KubernetesServiceException(String message) {
        super(message);
    }
}
