CaaS Service broker for PaaS-TA
==================
CaaS 서비스 브로커는 CaaS 서비스를 파스-타(PaaS-TA) 서비스로 제공합니다.

이 서비스 브로커는 CaaS 서비스 중 쿠버네티스(Kubernetes)에 대응하고 있으며, 클라우드 컨트롤러와 서비스 브로커 간의 v2 서비스 API를 보여줍니다. 
이 API는 클라우드 컨트롤러 API와 혼동되어서는 안됩니다.

이 브로커에는 CaaS 서버 혹은 CaaS 릴리즈가 포함되어 있지 않습니다.
대신 CaaS 서버의 서비스를 관리하는 CaaS 자바 브로커를 배포하는 것을 의미합니다.

CaaS 서비스 브로커는 CaaS 서버와 PaaS-TA(클라우드 파운드리)간의 서비스를 제공하는 것을 말하며, 독립 실행하여 실행되는 CaaS 서버 응용프로그램은 별도로 지원하지 않습니다.

이 브로커가 수행하는 CaaS 관리 작업은 다음과 같습니다.
- CaaS 서비스의 카탈로그 등록
- CaaS 인스턴스 프로비저닝 생성 (네임스페이스 및 유저 생성)
- CaaS 인스턴스 프로비저닝 플랜의 갱신
- CaaS 인스턴스 프로비저닝 해제 (유저 및 네임스페이스 삭제)
이 브로커에서는 CaaS 서비스에 대한 바인딩/언바인딩은 제공하지 않습니다.
[서비스팩 개발 가이드](https://github.com/PaaS-TA/Documents-PaaSTA-1.0/blob/master/Development-Guide/ServicePack_develope_guide.md)의 API 개발 가이드를 참고하시면 아키텍쳐와 기술, 구현과 개발에 대해 자세히 알 수 있습니다.

--------------------
이 브로커의 소스코드를 사용하기 위한 환경은 다음과 같습니다.
- 빌드 환경 : Java 8 + Gradle 4.x
- 의존성 패키지
  - PaaS-TA Service Java broker (>= Java 8)
  - Spring boot 1.5.14
    > Spring boot JPA \
      Spring boot Web \
      Spring boot Security \
      Spring boot Session core \
      Spring boot Test
  
  - MySQL JDBC Driver (Runtime Provider)
  - Jayway Json-Path

----------

가능한 명령 목록 (로컬 환경)

- Catalog 조회 (GET) : http://localhost:8888/v2/catalog
  - Header
    > Authorization : Bearer type \
      X-Broker-Api-Version : 2.4 \
      Content-Type : application/json
  
  - Body : None

- 서비스 인스턴스 생성 (PUT) : 
  - Header
    > Authorization : Bearer type \
      X-Broker-Api-Version : 2.4 \
      Content-Type : application/json \
      Accept: application/json
  
  - Body
    > { \
        "service_id": \<service-id-string>, \
        "plan_id": \<plan-id-string>, \
        "organization_guid": \<organization-guid-string>, \
        "space_guid": \<space-guid-string>, \
        "parameters": { "userName": \<user-name-in-caas-service> } \
      }


- 서비스 인스턴스 갱신 (PATCH) : 
  - Header
    > Authorization : Bearer type \
      X-Broker-Api-Version : 2.4 \
      Content-Type : application/json 
  - Body
    > { \
        "plan_id": \<plan-id-string>, \
        "service_id": \<service-id-string> \
      } 


- 서비스 인스턴스 삭제 (DELETE) : 
  - Header
    > Authorization : Bearer type \
      X-Broker-Api-Version : 2.4 \
      Content-Type : application/json 
  - Body : None

