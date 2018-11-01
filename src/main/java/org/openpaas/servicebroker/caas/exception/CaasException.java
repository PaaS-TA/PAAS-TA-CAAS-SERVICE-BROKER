package org.openpaas.servicebroker.caas.exception;

import org.openpaas.servicebroker.exception.ServiceBrokerException;

/**
 * Kubernetes 서비스 관련 에러 Exception 클래스
 *
 * @author Hyungu Cho
 * @version 20180724
 */
public class CaasException extends ServiceBrokerException {

    private static final long serialVersionUID = 4604824405877022727L;

    public CaasException(String message) {
        super(message);
    }
    
    public CaasException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CaasException(Throwable cause) {
    	super(cause);
    }
}
