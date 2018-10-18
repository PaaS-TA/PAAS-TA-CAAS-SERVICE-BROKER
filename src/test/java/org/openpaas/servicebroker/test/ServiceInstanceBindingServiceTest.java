package org.openpaas.servicebroker.test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.openpaas.servicebroker.caas.service.impl.InstanceBindingServiceImpl;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.fixture.RequestFixture;

/**
 * Created by user on 2017-09-20.
 */
public class ServiceInstanceBindingServiceTest {

    @InjectMocks
    InstanceBindingServiceImpl instanceBindingService;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createServiceInstanceBinding() throws Exception {

        CreateServiceInstanceBindingRequest request = RequestFixture.getCreateServiceInstanceBindingRequest();

        assertThatThrownBy(() -> instanceBindingService.createServiceInstanceBinding(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Not Supported");
    }

    @Test
    public void test_deleteServiceInstanceBinding() throws Exception {

        DeleteServiceInstanceBindingRequest request = RequestFixture.getDeleteServiceInstanceBindingRequest();

        assertThatThrownBy(() -> instanceBindingService.deleteServiceInstanceBinding(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Not Supported");
    }


}
