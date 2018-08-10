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
import org.openpaas.servicebroker.kubernetes.config.EnvConfig;
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
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService {
	
	private static final Logger logger = LoggerFactory.getLogger(RestTemplateService.class);

	@Autowired
	RestTemplate restTemplate;
	//서비스로 따로 빼셈
	
	@Autowired
	EnvConfig envConfig;

	@Autowired
	HttpHeaders httpHeaders;
	// 서비스로 빼셈 
	
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
	 * httpHeaders 정보를 세팅한다.
	 *
	 * @author Hyerin
	 * @since 2018.07.24
	 */
	@Bean
	public HttpHeaders httpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + envConfig.getAdminToken());
		headers.add("Accept", "application/json,application/yaml,text/html");
		headers.add("Content-Type", "application/yaml;charset=UTF-8");
		return headers;
	}
	
	public <T> T send(String url, HttpMethod httpMethod, Class<T> responseType) {
		return send(url, null, httpMethod, responseType);
	}
	
	public <T> T send(String url, String yml, HttpMethod httpMethod, Class<T> responseType) {
		HttpEntity<String> reqEntity;
		if(yml == null) {  //null이면 
			reqEntity = new HttpEntity<>(httpHeaders);
		} else { // null이 아니면
			reqEntity = new HttpEntity<>(yml, httpHeaders);
		}
		ResponseEntity<T> resEntity = restTemplate.exchange(url, httpMethod, reqEntity, responseType);
		if (reqEntity.getBody() != null) {
			logger.info("Response Type: {}", resEntity.getBody().getClass());
        }
		
		return resEntity.getBody(); 
	}

}
