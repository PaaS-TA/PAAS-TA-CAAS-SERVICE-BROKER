# PAAS-TA-CAAS-SERVICE-BROKER
PaaS-TA 에서 제공하는 Container 서비스 브로커로 클라우드 컨트롤러와 서비스 브로커간의 v2 서비스 API 를 제공합니다.

Container 서비스 브로커가 수행하는 Container 서비스 관리 작업은 다음과 같습니다.
- Catalog : Container 서비스 카탈로그 조회
- Provisioning : Container 서비스 인스턴스 생성 ( parameters "owner", "org_name" 필수 )
- Updateprovisioning : Container 서비스 인스턴스 갱신
- Deprovisioning : Container 서비스 인스턴스 삭제

[서비스팩 개발 가이드](https://github.com/PaaS-TA/Guide-3.0-Penne-/blob/master/Service-Guide/Tools/PaaS-TA%20Container%20%EC%84%9C%EB%B9%84%EC%8A%A4%ED%8C%A9%20%EC%84%A4%EC%B9%98%20%EA%B0%80%EC%9D%B4%EB%93%9C_v1.0.md)의 API 개발 가이드를 참고하시면 아키텍쳐와 기술, 구현과 개발에 대해 자세히 알 수 있습니다.

## 개발 환경
- JDK 8
- Gradle 4.4.1
- Spring Boot 1.5.14
- Spring Boot Cf Service Broker 2.4.0
- JSch 0.1.54
- Hibernate Validator 5.1.0
- Json Path 2.2.0
- Jacoco 0.8.1

## 가능한 명령 목록 (로컬 환경)

- Catalog 조회 : http://localhost:8888/v2/catalog
  - Method : GET 
  - Header
    > Authorization : Bearer type \
      X-Broker-Api-Version : 2.4 \
      Content-Type : application/json
  - Body : None 
  - Parameters : None

- 서비스 인스턴스 생성 : http://localhost:8888/v2/service_instances/[new-instance-name]
  - Method : PUT 
  - Header
    > Authorization : Bearer type \
      X-Broker-Api-Version : 2.4 \
      Content-Type : application/json \
      Accept: application/json
  - Body
    > { \
        "service_id": \<service-id-string\>, \
        "plan_id": \<plan-id-string\>, \
        "organization_guid": \<organization-guid-string\>, \
        "space_guid": \<space-guid-string\>, \
        "parameters": { "userName": \<user-name-in-caas-service\> } \
      }
  - Parameters : None

- 서비스 인스턴스 갱신 : http://localhost:8888/v2/service_instances/[instance-name]
  - Method : PATCH 
  - Header
    > Authorization : Bearer type \
      X-Broker-Api-Version : 2.4 \
      Content-Type : application/json 
  - Body
    > { \
        "plan_id": \<plan-id-string\>, \
        "service_id": \<service-id-string\> \
      } 
  - Parameters : None

- 서비스 인스턴스 삭제 : http://localhost:8888/v2/service_instances/[instance-name]
  - Method : DELETE 
  - Header
    > Authorization : Bearer type \
      X-Broker-Api-Version : 2.4 \
      Content-Type : application/json 
  - Body : None
  - Parameters : 
    > service_id : \<predefined_service_id\> \
      plan_id : \<plan_id_of_service\>

