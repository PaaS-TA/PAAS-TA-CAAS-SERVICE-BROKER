package org.openpaas.servicebroker.caas.service;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.openpaas.servicebroker.caas.model.Constants;
import org.openpaas.servicebroker.caas.model.User;
import org.openpaas.servicebroker.caas.repo.JpaAdminTokenRepository;
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

/**
 * RestTemplate를 관리하는 서비스 클래스
 * 크게 3가지의 통신으로 나뉜다.
 * 1. token Validation을 위한 메소드
 * 2. kuber api와 통신하기 위한 메소드
 * 3. common DB에 저장하기 위해 common-api와통신하는 메소드
 * @author Hyerin
 * @since 2018.08.22
 * @version 20180822
 */
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


	/**
	 * broker DB에 저장된 token값이 유효한지 확인하기 위한 메소드
	 * kuber의 get /nodes를 찔러서 확인한다. (node가 없는 kuber는 없을 것이라 가정했기 때문)
	 *
	 * @author Hyerin
	 * @since 2018.08.22
	 */
	public boolean tokenValidation() {
		
		headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + adminTokenRepository.getOne(Constants.TOKEN_KEY).getTokenValue());
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
	
	/**
	 * kuber api와 통신하기 위한 메소드
	 * get의 경우 body가 필요 없기 때문에 yml로 get,delete의 유무판별하여 body를 넣고 안넣고를 정함.
	 * @author Hyerin
	 * @since 2018.08.22
	 */
	public <T> T send(String url, String yml, HttpMethod httpMethod, Class<T> responseType) {
		
		headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + adminTokenRepository.getOne(Constants.TOKEN_KEY).getTokenValue());
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
	
	/**
	 * common-api와 통신하기 위한 메소드
	 * common DB에 저장하는 형식(User)에 필요한 파라미터만 채워서 보내준다. 
	 * @author Hyerin
	 * @since 2018.08.22
	 */
	public void requestUser(User user, HttpMethod httpMethod) throws HttpStatusCodeException{
		
		headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Content-Type", "application/json;charset=UTF-8");
		headers.add("authorization", "Basic " + createAuthKey());
		
		HttpEntity<User> reqEntity = new HttpEntity<User>(user, headers);
		
		if(HttpMethod.POST.equals(httpMethod)) {
			logger.info("Send User Save Request to Common API");
			restTemplate.exchange(propertyService.getCommonUrl() + "/users", httpMethod, reqEntity, String.class);
		} else if (HttpMethod.PUT.equals(httpMethod)){
			logger.info("Send User Update Request to Common API");
			restTemplate.exchange(propertyService.getCommonUrl() + "/users/serviceInstanceId/" + user.getServiceInstanceId(), httpMethod, reqEntity, String.class);
		}else {
			logger.info("Send User Delete Request to Common API");
			restTemplate.exchange(propertyService.getCommonUrl() + "/users/serviceInstanceId/" + user.getServiceInstanceId(), httpMethod, reqEntity, String.class);
		}

	}

	private String createAuthKey() {
		logger.info("createAuthKey() start!!!");
		String primitiveKey = propertyService.getCommonId() + ":" + propertyService.getCommonPassword();
		Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(primitiveKey.getBytes());
	}
}
