spring boot reactive 프로젝트를 위한 템플릿 입니다. 
JPA에 대한 설정도 포함되어 있습니다.

# 목차
- [주요 내용에 대한 설명](#주요-내용에-대한-설명)
- [프로젝트 표준 구성 사용 시 알아두어야 할 사항](#프로젝트-표준-구성-사용-시-알아두어야-할-사항)
- [profile 구분](#profile-구분)
- [API 문서 작성](#api-문서-작성)
- [테스트 코드 작성에 대하여](#테스트-코드-작성에-대하여)
---

작성기준일 : 2022년 12월 21일

# 주요 내용에 대한 설명

## 주요 기술스택

- java 17
- Spring boot 3.0.0 (release 2022.11.24)
- gradle 7.5.1 (release 2022.08.05)
- QueryDSL 5.0.0
- flyway 9.8.2
- PostgreSQL 14.6
- PostgreSQL Driver 42.5.1
- PostgreSQL R2DBC Driver 1.0.0.RELEASE
- Mongo DB 6.0.3
- restdocs 3.0.0
- junit jupiter 5.9.1, assertj 3.23.1
- testcontainers 1.17.6

## spring boot 3.0.0 주요 내용

- Java 17 베이스라인
- 실험적인 Spring Native 프로젝트를 대체하는 GraalVM으로 네이티브 이미지 생성 지원
- Micrometer 및 Micrometer Tracing 으로 관찰 가능성 향상
- EE 9 기준으로 Jakarta EE 10 지원

세부내용은 [https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Release-Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Release-Notes) 을 참조하십시오.

### spring boot 3.0.0과 spring framework 6.x 관련 문서 번역

spring boot 3.0.0 공식 문서를 번역한 내용을 정리해두었으니 참고하십시오.

- [Spring Boot 3.0 Release Notes](https://dptablo.github.io/spring%20boot/spring/spring-boot-3.0.0-release-notes/)
- [Spring Framework 6.x 변경사항](https://dptablo.github.io/spring/spring%20framework/spring-framework-6.x/)
- [Spring Boot 3.0 마이그레이션 가이드](https://dptablo.github.io/spring%20boot/spring-boot-3.0-migration-guide/)

## java 17 적용에 관하여

Spring Boot 3.0.0 이 릴리즈 되면서 Java 17을 베이스로 지원하게 되었습니다 ([https://spring.io/blog/2022/11/24/spring-boot-3-0-goes-ga](https://spring.io/blog/2022/11/24/spring-boot-3-0-goes-ga)).

Java 17은 Spring Boot 3 에서의 기준 언어이며 Spring Boot 3.0 에서는 Java 19 를 지원합니다. Java 17은 Java 11 대비 LTS 지원 기간이 2026년 9월까지 더 긴 지원을 보장합니다.

## blocking IO DB

Spring Data JPA + JDBC를 사용하여 ORM 형태의 repository 구현 방식을 사용합니다.

일반적으로 사용되는 RDBMS는 PostgreSQL 입니다.

## non-blocking IO DB

### 관계형 DB

Spring Reactive Stack 의 R2DBC 를 이용한 repository 구현방식을 사용합니다.

일반적으로 사용되는 RDBMS는 PostgreSQL 입니다.

### NoSQL DB

Spring Reactive Stack 의 MongoDB 를 이용한 repository 구현방식을 사용합니다.

## 테스팅

아래의 테스팅 프레임워크를 기본적으로 사용합니다.

- junit5 (with mockito)
- org.assertj
- com.squareup.okhttp3:mockwebserver

# 프로젝트 표준 구성 사용 시 알아두어야 할 사항

이 spring boot 프로젝트 표준에서는 사용하고자 하는 모든 기술 스택에 대한 프레임워크 의존성과 설정, 예제코드 등이 작성되어 있습니다. 그리고 적용된 기술 스택에 대하여 함께 사용 가능한 것을 전제로 구성되어 있습니다. 예를 들어 JPA-Datasource와 MongoDB-Reactive 를 사용하여 모두 사용하는 것을 의미합니다.

새로운 프로젝트로 개발을 시작하고자 할 때 프로젝트 구성을 이해하고 사용하고자 하는 부분을 제외한 다른 의존성과 설정, 코드를 제거하고 사용하십시오.

# profile 구분

4가지 profile 로 구분하고 각각의 용도를 구분합니다. Spring boot 컨테이너를 실행하거나 단위 테스트를 실행할 때 profile 이 지정되지 않은 경우는 모든 profile 상태에서 수행 결과가 동일함을 의미합니다.

## tc

가장 심플한 테스트를 위한 profile 입니다. 아래의 테스트 케이스에 대한 이 profile 을 사용합니다.

단위 테스트를 위한 profile 입니다. 쉽고 간결한 단위 테스트를 위하여 아래와 같은 제약 사항을 전제로 합니다.

- 외부 서비스에 대한 의존성이 없는 클래스 함수의 단위 테스트
- mocking 을 이용한 테스트
- 그 외 테스트를 수행함에 있어 실행되는 코드 외에 필요한 것이 없는 경우

외부 서비스 또는 프레임워크에 대한 의존성이 있는 경우에는 testcontainers 를 활용한 테스트를 작성 합니다. 이 경우 tc profile 을 사용하며 필요한 설정들을 추가하여 관리합니다.

`local` profile 을 이용한 테스트도 의존되는 외부 서비스 또는 프레임워크를 준비하여 사용하는 것은 비슷합니다. 하지만 tc profile 에서는 테스트 suite 단위로 의존 서비스가 준비되고 테스트가 종료되면 삭제됩니다. 의존 서비스가 휘발성을 가지는 것을 의미합니다.

## local

개발과정에서 테스트 또는 디버깅을 위한 profile 입니다. 연계되는 타 서비스들을 로컬환경에 구축하여 설정합니다. 모든 서비스가 로컬에 구성되지만 실제와 동일한 서비스입니다.

## dev

개발서버 환경을 의미합니다. Spring boot 는 개발환경에서 실행하지만 DB 서버, 메시징 서버 등 연계 시스템은 개발서버 환경에 연결합니다.

일반적으로 통합테스트를 위한 설정입니다.

## prod

운영환경을 의미합니다. 운영환경에 필요한 설정만을 정의하고 debug 을 위한 설정을 제거합니다.

이 profile 은 개발단계에서는 사용되지 않으며 운영환경에 배포를 위하여 사용되는 profile 입니다.

## 추가적인 테스트 레벨의 profile 정의 - tc

test profile 을 이용하여 in-memory DB에 대한 테스트를 합니다. 하지만 in-memory DB 의 h2, embeded DB 의 경우에는 특정 벤더의 DBMS에 대한 테스트 신뢰성을 보장하지 못합니다.

tc profile 에 testcontainers 를 사용에 필요한 사항을 설정하고 이를 사용하여 테스트 케이스 수행시 사용할 수 있습니다.

# IDE 환경설정

IDE는 intellij IDEA의 사용을 전제로 설명합니다. Intellij IDEA는 2022.3 를 기준으로 작성되었습니다.

아래의 설정은 개발자의 설정의 기호에 따라 변경할 수 있습니다. 변경하게 되면 테스트 환경, QueryDSL 설정과 관련된 부분이 영향을 받을 수 있습니다.

### Gradle 설정

Settings > Build, Execution, Deployment > Build Tools > Gradle 에 아래 항목을 설정합니다.

- Build and run using : IntelliJ IDEA
- Run tests using : IntelliJ IDEA
- Gradle JVM : Project SDK

![](/document/readme/readme_gradle_settings_in_intellij.jpg)

### Spring Boot Automatically

Spring Boot 의 실행 상태에서 코드 수정에 대한 자동 빌드 후 reload 를 설정하는 방법에 대한 내용입니다.

1. settings > Build, Execution, Deployment > Compiler 의 ’Build project automatically’ 를 활성화합니다.

![](/document/readme/readme_build_automatically_in_intellij_01.png)

2. Spring Boot 의 Run Configuration 의 아래 항목을 설정합니다.
    - Modify Options > On ‘Update’ action : Update classes and resources
    - Modify Options > On frame deactivation : Update classes and resources

![](/document/readme/readme_build_automatically_in_intellij_02.png)

### Annotation Prosessing

Intellij의 Settings > Build, Execution, Deployment > Compiler > Annotation Processors 에서 아래와 같이 설정합니다. lombok, querydsl 등 어노테이션과 관련된 개발환경에 대한 빌드 클래스가 생성됩니다.

설정값을 아래 이미지와 다르게 할 수 있습니다. 이 경우 변경된 사항에 맞추어 gradle과 IDE 설정을 변경해야 할 수 있습니다.

![](/document/readme/readme_annotation_processing_settings_in_intellij.png)

### Shorten command line

Intellij 에서 Spring Boot, JUnit 테스트 등 run 을 실행할 경우 오류가 발생하는 경우 Shorten command line 으로 실행하십시오.

> MacOS 에서는 오류가 발생하는 경우가 없었으며 Windows 11 에서 발생하였습니다.

![](/document/readme/readme_shorten_command_line.png)

# API 문서 작성

프로젝트 API 문서 작성은 아래 주요 사항을 준수합니다.

- [Open API 3](https://www.openapis.org/) Spec으로 작성합니다.
- Open API 3 Spec의 파일 포맷은 YAML로 작성합니다.
- [Spring REST Docs](https://docs.spring.io/spring-restdocs/docs/current/reference/htmlsingle/#documenting-your-api) 로 테스트 기반의 API 문서 작성하며, 작성된 문서에서 OAS 3 Spec을 생성합니다.

프로젝트 개발 여건에 따라 문서를 작성하는 방식은 선택할 수 있습니다. 가장 권장되는 방법은 Spring REST Docs를 사용하는 것입니다.

Spring REST Docs 로 작성할 수 없다면 아래 방법을 선택하십시오.

- OAS3 Spec의 java 어노테이션을 이용한 작성 방법 (swagger v3 oas)
- OAS3 Spec 규격의 YAML 문서를 작성 (어노테이션을 사용하지 않는 방법)

### OAS3 Spec 규격의 YAML 문서를 작성에 대하여

`src/docs/oas-doc.yaml` 에 문서 내용을 작성합니다. 작성된 spec은 검증이 되었다는 것을 전제로 이를 배포합니다.

만약 Spring REST Docs 를 사용했다면 위 spec 파일의 내용은 작성하지 않으며, Spring REST Docs를 이용하여 OAS 3 Spec 을 생성해야 합니다.

### Spring REST Docs를 OAS 3로 변환하는 것에 대하여

아래 오픈 소스 프로젝트를 이용하여 변환할 수 있습니다.

[https://github.com/ePages-de/restdocs-api-spec](https://github.com/ePages-de/restdocs-api-spec)

> 아직 Spring Boot 3.x 에서 사용할 수 없으며, 추후 업데이트에 따라 적용될 예정입니다.
>

# 테스트 코드 작성에 대하여

### 기본적인 테스트 작성 원칙

- Controller, Service, Repository, Component 등 각각의 요소들은 각자의 테스트만 수행하고 주입되는 의존 클래스에 대한 것은 mock 객체를 사용하여 작성합니다.
- Controller 테스트는 클라이언트로부터 전달되는 request 의 처리와 response 에 대한 검증 테스트를 작성합니다.
- Service, component 테스트는 처리 프로세스 로직에 대한 테스트를 작성합니다.
- Repository 테스트는 DB에 대한 CRUD 가 올바르게 처리 되는가에 대한 테스트를 작성합니다.

### testcontainers 사용에 관하여

테스트의 멱등성과 실제 사용 DBMS 에 대한 테스트 신뢰성을 위하여 Testcontainers ([https://www.testcontainers.org/](https://www.testcontainers.org/)) 를 적용하였습니다. Testcontainers 를 이용한 테스트 케이스에서는 데이터의 롤백이나 사전 준비작업 등에 대한 처리를 하지 않아도 됩니다.

Testcontainers 를 이용한 테스트 케이스 작성을 강제하지 않습니다. 하지만 반복하여 테스트 실행을 할 수 있도록 구현해야 합니다. 테스트를 위한 별도의 서비스가 준비되어야 한다면 install 가이드와 준비과정에 대한 문서를 제공해야 합니다.

### 테스트 작성 가이드

- [DataSource PostgreSQL JPA Repository 테스트 작성 가이드](https://github.com/dpTablo/spring-boot-template-reactive/wiki/DataSource-PostgreSQL-JPA-Repository-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%9E%91%EC%84%B1-%EA%B0%80%EC%9D%B4%EB%93%9C)
- [DataSource PostreSQL QueryDSL Repository 테스트 작성 가이드](https://github.com/dpTablo/spring-boot-template-reactive/wiki/DataSource-PostreSQL-QueryDSL-Repository-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%9E%91%EC%84%B1-%EA%B0%80%EC%9D%B4%EB%93%9C)
- [R2DBC PostgreSQL Repository 테스트 작성 가이드](https://github.com/dpTablo/spring-boot-template-reactive/wiki/R2DBC---PostgreSQL-Repository-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%9E%91%EC%84%B1-%EA%B0%80%EC%9D%B4%EB%93%9C)
- [Reactive MongoDB Repository 테스트 작성 가이드](https://github.com/dpTablo/spring-boot-template-reactive/wiki/Reactive-MongoDB-Repository-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%9E%91%EC%84%B1-%EA%B0%80%EC%9D%B4%EB%93%9C)