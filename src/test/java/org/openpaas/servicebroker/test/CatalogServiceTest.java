package org.openpaas.servicebroker.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.Mock;
import org.openpaas.servicebroker.caas.service.impl.CatalogServiceImpl;
import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.openpaas.servicebroker.model.fixture.ServiceFixture;
import org.openpaas.servicebroker.service.CatalogService;
import org.paasta.servicebroker.apiplatform.common.TestConstants;

/**
 * Created by user on 2017-09-20.
 */
public class CatalogServiceTest {

	@Mock
    CatalogService catalogService;

    @Mock
    Catalog catalog;


    @Test
    public void test_getCatalog() throws Exception {
        catalog = new Catalog(ServiceFixture.getCatalog());
        catalogService = new CatalogServiceImpl(catalog);

        Catalog result = catalogService.getCatalog();

        assertThat(result.getServiceDefinitions().get(0).getId(), is(TestConstants.SERVICEDEFINITION_ID));
        assertThat(result.getServiceDefinitions().get(0).getName(), is(TestConstants.SERVICEDEFINITION_NAME));
        assertThat(result.getServiceDefinitions().get(0).isBindable(), is(false));
        assertThat(result.getServiceDefinitions().get(0).isPlanUpdatable(), is(false));

        ServiceDefinition serviceDefinition = catalogService.getServiceDefinition(TestConstants.SERVICEDEFINITION_ID);
        assertThat(serviceDefinition.getId(), is(TestConstants.SERVICEDEFINITION_ID));
        assertThat(serviceDefinition.getName(), is(TestConstants.SERVICEDEFINITION_NAME));
        assertThat(serviceDefinition.isBindable(), is(false));
        assertThat(serviceDefinition.isPlanUpdatable(), is(false));

    }
}




