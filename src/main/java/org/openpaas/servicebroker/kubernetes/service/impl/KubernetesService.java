package org.openpaas.servicebroker.kubernetes.service.impl;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openpaas.servicebroker.kubernetes.config.EnvConfig;
import org.openpaas.servicebroker.kubernetes.exception.KubernetesServiceException;
import org.openpaas.servicebroker.kubernetes.model.JpaServiceInstance;
import org.openpaas.servicebroker.kubernetes.service.TemplateService;
import org.openpaas.servicebroker.model.Plan;
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

/**
 * 이 서비스 브로커에서 접근하는 Kubernetes에 대한 서비스를 위한 클래스이다.
 *
 * @author hyerin
 * @author Hyungu Cho
 * @since 2018/07/24
 * @version 20180801
 */
@Service
public class KubernetesService {

    private static final Logger logger = LoggerFactory.getLogger( KubernetesService.class );

    @Autowired
    private TemplateService templateService;

    @Autowired
    private EnvConfig envConfig;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpHeaders httpHeaders;


    /**
     * RestTemplate Bean 객체를 생성하는 메소드 (단, SSL은 무시) <br>
     * create restTemplate ignore ssl
     *
     * @author Hyerin
     * @since 2018.07.24
     */
    @Bean
    public RestTemplate restTemplate () throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = ( X509Certificate[] chain, String authType ) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
            .loadTrustMaterial( null, acceptingTrustStrategy )
            .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory( sslContext );

        CloseableHttpClient httpClient = HttpClients.custom()
            .setSSLSocketFactory( csf )
            .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
            new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient( httpClient );
        RestTemplate restTemplate = new RestTemplate( requestFactory );
        return restTemplate;
    }

    /**
     * httpHeaders 정보를 세팅한다.
     *
     * @author Hyerin
     * @since 2018.07.24
     */
    @Bean
    public HttpHeaders httpHeaders () {
        HttpHeaders headers = new HttpHeaders();
        headers.add( "Authorization", "Bearer " + envConfig.getAdminToken() );
        headers.add( "Accept", "application/json,application/yaml,text/html" );
        headers.add( "Content-Type", "application/yaml;charset=UTF-8" );
        return headers;
    }

    /**
     * 1. namespace 생성
     * 2. namespace에 quota 할당
     * 3. namespace의 user 생성
     * 4. 생성한 user에 role 할당 및 binding
     * 5. 생성한 'user이름-token'으로 secret 생성
     *
     * @author Hyerin
     * @since 2018.07.30
     */
    public JpaServiceInstance createNamespaceUser ( JpaServiceInstance instance, Plan plan ) {
        logger.info( "In createNamespaceUser !!!" );

        String spaceName = createNamespace( instance.getOrganizationGuid() );
        this.createResourceQuota( spaceName, plan );

        String userName = createUser( spaceName, instance.getParameter( "userName" ) );

        createRole( spaceName, userName );
        createRoleBinding( spaceName, userName );
        String tokenName = createSecret( spaceName, userName );

        //String accessToken = getSecret(spaceName, userName);
        //System.out.println(getSecret(spaceName, userName));

        logger.info( "work done!!! {}  {}", spaceName, userName );

        // DB저장을 위한 JPAServiceInstance 리턴
        instance.setKubernetesNamespace( spaceName );
        instance.setKubernetesAccountName( userName );
        instance.setKubernetesAccountAccessToken( tokenName );
        return instance;

    }

    /**
     * spacename은 orgGuid에 timestamp를 찍어 준 후 특수문자를 '-'로 치환한다.
     * instance/create_namespace.ftl의 변수를 채운 후 restTemplate로 rest 통신한다.
     *
     * @author Hyerin
     * @since 2018.07.30
     */
    public String createNamespace ( String orgGuid ) {
        logger.debug( "create namespace!!! {}", orgGuid );

        Map<String, Object> model = new HashMap<>();
        String spaceName = "paas-" + orgGuid + "-caas";
        model.put( "name", spaceName );
        String yml = null;
        try {
            yml = templateService.convert( "instance/create_namespace.ftl", model );
        } catch ( KubernetesServiceException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.debug( "Here is your yml file!!! {}", yml );

        HttpEntity<String> reqEntity = new HttpEntity<>( yml, httpHeaders );
        restTemplate.exchange( envConfig.getCaasUrl() + "/api/v1/namespaces", HttpMethod.POST, reqEntity, Map.class );

        return spaceName;
    }

    /**
     * namespace에 quota를 할당한다. quota는 plan 정보대로 할당한다.
     * plan정보의 B -> i 로 바꾼 후에
     * instance/create_resource_quota.ftl의 변수를 채운 후 restTemplate로 rest 통신한다.
     *
     * @author Hyerin
     * @since 2018.07.30
     */
    public void createResourceQuota ( String spaceName, Plan plan ) {
        logger.info( "createUser Resource Quota ~~ space : {}   {}", spaceName, plan );

        Map<String, Object> model = new HashMap<>();
        plan.setMemory( plan.getMemory().replace( "B", "i" ) );
        plan.setDisk( plan.getDisk().replace( "B", "i" ) );
        model.put( "quotaName", spaceName + "-resourcequota" );
        model.put( "plan", plan );
        String yml = null;
        try {
            yml = templateService.convert( "instance/create_resource_quota.ftl", model );
        } catch ( KubernetesServiceException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpEntity<String> reqEntity = new HttpEntity<>( yml, httpHeaders );
        restTemplate.exchange( envConfig.getCaasUrl() + "/api/v1/namespaces/" + spaceName + "/resourcequotas", HttpMethod.POST, reqEntity, Map.class );

    }

    /**
     * parameter에 넘어온 userName 값으로 생선된 namespace의 관리자 계정을 생성한다.
     * user명은 web 등에서 kubernetes 명명규칙에 맞는 변수가 넘어왔다고 가정한다.
     * instance/create_account.ftl의 변수를 채운 후 restTemplate로 rest 통신한다.
     *
     * @author Hyerin
     * @since 2018.07.30
     */
    public String createUser ( String spaceName, String userName ) {
        logger.info( "createUser Account~~ {}", userName );

        String tmpString[] = userName.split( "@" );
        String convertName = tmpString[0].replaceAll( "([:.#$&!_\\(\\)`*%^~,\\<\\>\\[\\];+|-])+", "" ).toLowerCase() + "-admin";

        Map<String, Object> model = new HashMap<>();
        model.put( "spaceName", spaceName );
        model.put( "userName", convertName );
        String yml = null;
        try {
            yml = templateService.convert( "instance/create_account.ftl", model );
        } catch ( KubernetesServiceException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpEntity<String> reqEntity = new HttpEntity<>( yml, httpHeaders );
        restTemplate.exchange( envConfig.getCaasUrl() + "/api/v1/namespaces/" + spaceName + "/serviceaccounts", HttpMethod.POST, reqEntity, Map.class );

        return convertName;
    }

    /**
     * 생성된 namespace에 role을 생성한다.
     * role이름은 'namespace명-role' 이다.
     * instance/create_role.ftl의 변수를 채운 후 restTemplate로 rest 통신한다.
     *
     * @author Hyerin
     * @since 2018.07.30
     */
    public void createRole ( String spaceName, String userName ) {
        logger.info( "create Role And Binding~~ {}", userName );

        Map<String, Object> model = new HashMap<>();
        model.put( "spaceName", spaceName );
        model.put( "userName", userName );
        model.put( "roleName", spaceName + "-role" );
        String yml = null;
        try {
            yml = templateService.convert( "instance/create_role.ftl", model );
        } catch ( KubernetesServiceException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpEntity<String> reqEntity = new HttpEntity<>( yml, httpHeaders );
        restTemplate.exchange( envConfig.getCaasUrl() + "/apis/rbac.authorization.k8s.io/v1/namespaces/" + spaceName + "/roles", HttpMethod.POST, reqEntity, Map.class );

    }


    /**
     * 생성된 namespace에 roleBinding을 생성한다.
     * binding이름은 'namespace명-role-binding' 이다.
     * instance/create_roleBinding.ftl의 변수를 채운 후 restTemplate로 rest 통신한다.
     *
     * @author Hyerin
     * @since 2018.07.30
     */
    public void createRoleBinding ( String spaceName, String userName ) {
        logger.info( "create Binding {}", userName );

        Map<String, Object> model = new HashMap<>();
        model.put( "spaceName", spaceName );
        model.put( "userName", userName );
        model.put( "roleName", spaceName + "-role" );
        String yml = null;
        try {
            yml = templateService.convert( "instance/create_roleBinding.ftl", model );
        } catch ( KubernetesServiceException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpEntity<String> reqEntity = new HttpEntity<>( yml, httpHeaders );

        restTemplate.exchange( envConfig.getCaasUrl() + "/apis/rbac.authorization.k8s.io/v1/namespaces/" + spaceName + "/rolebindings", HttpMethod.POST, reqEntity, Map.class );

    }

    /**
     * 생성된 user에게 부여할 secret을 만든다.
     * secrete이름은 '유저명-token' 이다.
     * role이름은 namespace명-role 이고, binding이름은 namespace명-role-binding 이다.
     * instance/create_role.ftl의 변수를 채운 후 restTemplate로 rest 통신한다.
     *
     * @author Hyerin
     * @since 2018.07.30
     */
    public String createSecret ( String spaceName, String userName ) {
        logger.info( "create Secret for {}", spaceName, userName );

        Map<String, Object> model = new HashMap<>();
        model.put( "spaceName", spaceName );
        model.put( "userName", userName );
        model.put( "tokenName", userName + "-token" );
        String yml = null;
        try {
            yml = templateService.convert( "instance/create_secret.ftl", model );
        } catch ( KubernetesServiceException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpEntity<String> reqEntity = new HttpEntity<>( yml, httpHeaders );
        restTemplate.exchange( envConfig.getCaasUrl() + "/api/v1/namespaces/" + spaceName + "/secrets", HttpMethod.POST, reqEntity, Map.class );
        return userName + "-token";

    }

    /**
     * user에게 부여된 secret token을 반환한다.
     * token 값은 base64 인코딩 되어 있기 때문에, 디코드 하는 부분도 포함되어 있다.
     *
     * @author Hyerin
     * @since 2018.07.30
     */
    @Deprecated
    public String getSecret ( String spaceName, String userName ) {

        logger.info( "get Secret {}", userName );

        HttpEntity<String> reqEntity = new HttpEntity<>( httpHeaders );

        ResponseEntity<String> hihi = restTemplate.exchange( envConfig.getCaasUrl() + "/api/v1/namespaces/" + spaceName + "/secrets/" + userName, HttpMethod.GET, reqEntity, String.class );

        String token = null;
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = null;
        try {
            jsonObj = ( JSONObject ) jsonParser.parse( hihi.getBody() );
        } catch ( org.json.simple.parser.ParseException e ) {
            e.printStackTrace();
        }

        jsonObj = ( JSONObject ) jsonObj.get( "data" );
        token = jsonObj.get( "token" ).toString();

        Decoder decoder = Base64.getDecoder();
        String decodeToken = new String( decoder.decode( token ) );

        return decodeToken;
    }

    /**
     * namespace를 삭제한다.
     * 삭제를 신청하는 사람이 관리자일 것이라 생각하여, 중복체크, 공유유저 삭제 등의 행동은 하지 않는다.
     *
     * @author Hyerin
     * @since 2018.07.30
     */
    public boolean deleteNamespace ( String namespace ) {
        logger.debug( "Start to delete namespace in kubernetes." );

        // TODO kubernetes에 있는 namespace 삭제
        boolean isSuccess = false;

        HttpEntity<String> reqEntity = new HttpEntity<>( httpHeaders );
        restTemplate.exchange( envConfig.getCaasUrl() + "/api/v1/namespaces/" + namespace, HttpMethod.DELETE, reqEntity, Map.class );
        isSuccess = true;

        logger.debug( "Done to delete namespace in kubernetes." );

        return isSuccess;
    }

    /**
     * Namespace가 존재하는지 확인한다.
     *
     * @param namespace
     * @return
     */
    public boolean existsNamespace ( String namespace ) {
        // TODO namespace 존재 여부 확인 필요
        return false;
    }

    /**
     * namespace에 quota를 재할당한다. 하위->상위 플랜 유효성 확인은 InstanceServiceImpl에서 대신 수행한다.
     * plan정보의 B -> i 로 바꾼 후에 instance/change_resource_quota.ftl의 변수를 채운 후
     * restTemplate로 rest 통신한다.
     *
     * @author Hyerin
     * @author Hyungu Cho
     * @since 2018.07.30
     */
    public void changeResourceQuota ( String spaceName, Plan plan ) {
        logger.info( "changeUser Resource Quota ~~ space : {}   {}", spaceName, plan );

        Map<String, Object> model = new HashMap<>();
        plan.setMemory( plan.getMemory().replace( "B", "i" ) );
        plan.setDisk( plan.getDisk().replace( "B", "i" ) );
        model.put( "quotaName", spaceName + "-resourcequota" );
        model.put( "plan", plan );
        String yml = null;
        try {
            yml = templateService.convert( "instance/create_resource_quota.ftl", model );
        } catch ( KubernetesServiceException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpEntity<String> reqEntity = new HttpEntity<>( yml, httpHeaders );
        // ResourceQuota Create-POST / Replace-PUT
        ResponseEntity<Map> resEntity =
            restTemplate.exchange( envConfig.getCaasUrl() + "/api/v1/namespaces/" + spaceName + "/resourcequotas/" + spaceName +
                    "-resourcequota",
                HttpMethod.PUT, reqEntity, Map.class );

        // response body : YAML or JSON. 현재는 HttpHeader를 통해 JSON으로 강제한 상태임.
        Map<String, Object> responseBody = resEntity.getBody();
        if ( null != responseBody )
            logger.debug( "Change ResourceQuota response body : {}", responseBody.toString() );
    }
}
