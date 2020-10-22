package org.openpaas.servicebroker.container.platform.exception;

import org.openpaas.servicebroker.exception.ServiceBrokerException;

/**
 * Kubernetes 서비스 관련 에러 Exception 클래스
 *
 * @author Hyungu Cho
 * @version 20180724
 */
public class ContainerPlatformException extends ServiceBrokerException {

    private static final long serialVersionUID = 4604824405877022727L;

    public ContainerPlatformException(String message) {
        super(message);
    }
    
    public ContainerPlatformException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ContainerPlatformException(Throwable cause) {
        super(cause);
    }
}
