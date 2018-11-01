package org.openpaas.servicebroker.test.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.servicebroker.controller.ServiceInstanceController;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.openpaas.servicebroker.model.fixture.RequestFixture;
import org.openpaas.servicebroker.model.fixture.ServiceFixture;
import org.openpaas.servicebroker.model.fixture.ServiceInstanceFixture;
import org.openpaas.servicebroker.service.CatalogService;
import org.openpaas.servicebroker.service.ServiceInstanceService;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by user on 2017-09-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:test.properties")
public class ServiceInstanceControllerTest {

    @InjectMocks
    ServiceInstanceController controller;

    @Mock
    ServiceInstanceService serviceInstanceService;

    @Mock
    CatalogService catalogService;

    @Value("${broker.api.version}") String broker_api_version;
    @Value("${broker.auth.user}") String broker_auth_user;
    @Value("${broker.auth.pwd}") String broker_auth_pwd;

    private MockMvc mockMvc;

    private String basicAuth;

    private String serviceDefinitionId;

    private String requestUrl;
    private String dashboardUrl;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        // CatalogController 를 MockMvC 객체로 만듬.
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        basicAuth = "Basic " + (Base64.getEncoder().encodeToString((broker_auth_user + ":" + broker_auth_pwd).getBytes()));
        serviceDefinitionId = ServiceFixture.getService().getId();
        requestUrl = ServiceInstanceController.BASE_PATH + "/"+TestConstants.SV_INSTANCE_ID_001;
        dashboardUrl = TestConstants.DASHBOARD_URL + "/"+ TestConstants.SV_INSTANCE_ID_001;
    }

    @Test
    public void createServiceInstance() throws Exception {

        // 서비스 인스턴스 생성

        //// serviceDefinition 조회
        when(catalogService.getServiceDefinition(serviceDefinitionId)).thenReturn(ServiceFixture.getService());
        when(serviceInstanceService.createServiceInstance(any(CreateServiceInstanceRequest.class))).thenReturn(ServiceInstanceFixture.getServiceInstance());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(RequestFixture.getCreateServiceInstanceRequest());

        MvcResult result = this.mockMvc.perform(put(requestUrl)
                .header(TestConstants.Headers_KEY_API_VERSION, broker_api_version)
                .header(TestConstants.Headers_KEY_AUTHORIZATION, basicAuth)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dashboard_url", is(dashboardUrl)))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void updateServiceInstance() throws Exception {

        // 서비스 인스턴스 업데이트
        when(serviceInstanceService.updateServiceInstance(any(UpdateServiceInstanceRequest.class))).thenThrow(new ServiceBrokerException("Not Supported"));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(RequestFixture.getUpdateServiceInstanceRequest());

        MvcResult result = this.mockMvc.perform(patch(requestUrl)
                .header(TestConstants.Headers_KEY_API_VERSION, broker_api_version)
                .header(TestConstants.Headers_KEY_AUTHORIZATION, basicAuth)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", is("Not Supported")))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void deleteServiceInstance() throws Exception {

        // 서비스 인스턴스 삭제
        when(serviceInstanceService.deleteServiceInstance(any(DeleteServiceInstanceRequest.class))).thenReturn(ServiceInstanceFixture.getServiceInstance());

        MvcResult result = this.mockMvc.perform(delete(requestUrl)
                .header(TestConstants.Headers_KEY_API_VERSION, broker_api_version)
                .header(TestConstants.Headers_KEY_AUTHORIZATION, basicAuth)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("service_id", TestConstants.SERVICEDEFINITION_ID)
                .param("plan_id", TestConstants.SERVICEDEFINITION_PLAN_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
    }

}
