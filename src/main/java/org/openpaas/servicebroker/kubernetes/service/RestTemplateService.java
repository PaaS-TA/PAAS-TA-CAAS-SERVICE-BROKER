package org.openpaas.servicebroker.kubernetes.service;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openpaas.servicebroker.kubernetes.model.User;
import org.openpaas.servicebroker.kubernetes.repo.JpaAdminTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService {
	
	private static final Logger logger = LoggerFactory.getLogger(RestTemplateService.class);

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	PropertyService propertyService;
	
	@Autowired
	JpaAdminTokenRepository adminTokenRepository;
	
	HttpHeaders headers;

		
	/**
	 * RestTemplate Bean 객체를 생성하는 메소드 (단, SSL은 무시) <br>
	 * create restTemplate ignore ssl
	 *
	 * @author Hyerin
	 * @since 2018.07.24
	 */
	@Bean
	public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
				.build();

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		return restTemplate;
	}


	public boolean tokenValidation() {
		
		headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + adminTokenRepository.getOne(propertyService.getAdminToken()).getTokenValue());
		headers.add("Accept", "application/json,application/yaml,text/html");
		headers.add("Content-Type", "application/yaml;charset=UTF-8");
		HttpEntity<String> reqEntity = new HttpEntity<>(headers);
		
		try {
			restTemplate.exchange(propertyService.getCaasUrl() + "/api/v1/nodes", HttpMethod.GET, reqEntity, String.class);
		} catch (HttpStatusCodeException exception) {
		    logger.info("Maybe token was changed. {} : {}", exception.getStatusCode().value(), exception.getMessage());
		    return false;
		}
		return true;
	}
	
	public <T> T send(String url, HttpMethod httpMethod, Class<T> responseType) {
		return send(url, null, httpMethod, responseType);
	}
	
	public <T> T send(String url, String yml, HttpMethod httpMethod, Class<T> responseType) {
		
		headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + adminTokenRepository.getOne(propertyService.getAdminToken()).getTokenValue());
		headers.add("Accept", "application/json,application/yaml,text/html");
		headers.add("Content-Type", "application/yaml;charset=UTF-8");
		
		
		HttpEntity<String> reqEntity;
		if(yml == null) {  //null이면 
			reqEntity = new HttpEntity<>(headers);
		} else { // null이 아니면
			reqEntity = new HttpEntity<>(yml, headers);
		}
		ResponseEntity<T> resEntity = restTemplate.exchange(url, httpMethod, reqEntity, responseType);
		if (reqEntity.getBody() != null) {
			logger.info("Response Type: {}", resEntity.getBody().getClass());
        }
		
		return resEntity.getBody(); 
	}
	
	public void requestUser(User user, HttpMethod httpMethod) throws HttpStatusCodeException{
		
		headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Content-Type", "application/json;charset=UTF-8");
		
		HttpEntity<User> reqEntity = new HttpEntity<User>(user, headers);
			
		restTemplate.exchange(propertyService.getCommonUrl() + "/users", httpMethod, reqEntity, String.class);

	}

}
