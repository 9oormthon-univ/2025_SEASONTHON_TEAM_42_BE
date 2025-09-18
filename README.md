# Next Career Server
시즌톤 42팀 넥스트-커리어 백엔드 레포지토리
<img width="1920" height="1080" alt="KakaoTalk_Photo_2025-09-07-07-43-22" src="https://github.com/user-attachments/assets/1985385d-edbc-4cfd-a730-66e3630b49a8" />

<br></br>

## 👬 Member
|      정성호     |                                        오원택                                         |                                                                                                   
| :------------------------------------------------------------------------------: |:----------------------------------------------------------------------------------:| 
|   <img src="https://avatars.githubusercontent.com/SeongHo5356?v=4" width=90px alt="정성호"/>       | <img src="https://avatars.githubusercontent.com/51taek?v=4" width=90px alt="오원택"/> |
|   [@SeongHo5356](https://github.com/SeongHo5356)   |                        [@51taek](https://github.com/51taek)                        | 

<br></br>

## 📝 Technology Stack
| Category             | Technology                          |
|----------------------|-------------------------------------|
| **Language**         | Java 21                             |
| **Framework**        | Spring Boot 3.4.9                   |
| **Databases**        | MySQL, Redis                        |
| **Authentication**   | JWT, Spring Security, OAuth2.0      |
| **Development Tools**| Lombok                              |
| **API Documentation**| Swagger UI (SpringDoc)              |
| **Storage**          | Naver Object Storage                |
| **Infrastructure**   | Terraform, NCP Server, ArgoCD, k3s  |
| **Build Tools**      | Gradle                              |

<br></br>
<br></br>

## 🔨 Project Architecture
<img width="7416" height="6000" alt="image" src="https://github.com/user-attachments/assets/93f12a4c-aaf4-45fa-a14e-bed1be42058d" />

<br></br>

## ⭐️ 기술스택/선정이유
### 1️⃣ Java 21

- Java 21은 최신 언어 기능(예: 패턴 매칭, 레코드, 가상 스레드 등)을 제공하여 코드의 가독성과 유지보수성을 높이며, 개발 생산성을 향상시킵니다.
- 최신 버전의 자바는 성능 최적화와 효율적인 메모리 관리 기능이 개선되어, 대규모 애플리케이션에서도 안정적이고 빠른 실행이 가능합니다.
- 장기 지원 버전이므로, 앞으로의 유지보수와 안정성 측면에서 신뢰할 수 있는 기반을 제공합니다.

### 2️⃣ SpringBoot 3.4.9

- 최신 버전의 Spring Boot는 스프링 프레임워크 및 관련 라이브러리와의 호환성이 뛰어나며, 보안 패치와 최신 기능들이 반영되어 있습니다.
- 자동 설정 기능과 다양한 내장 기능 덕분에 복잡한 설정 없이도 빠르게 애플리케이션을 개발할 수 있으며, 마이크로서비스 아키텍처 구축에 유리합니다.
- RESTful API, 데이터 액세스, 보안 등의 기능이 통합되어 있어 개발자가 비즈니스 로직에 집중할 수 있는 환경을 제공합니다.

### 3️⃣ SpringData JPA

- Spring Data JPA는 데이터베이스와의 인터랙션을 단순화하고, 불필요한 보일러플레이트 코드를 줄여 개발 효율성을 높여줍니다.

### 4️⃣ MySQL

- 안정적이고 널리 사용되는 관계형 데이터베이스로 운영 경험과 자료가 풍부합니다.
- 다양한 기능(트랜잭션, 인덱스, JSON 지원 등)으로 웹 서비스 개발에 적합합니다.

### 5️⃣ NCP(CLOVA studio, Server, Object Storage)

- NCP(네이버 클라우드 플랫폼)는 CLOVA Studio 및 서버 인프라 호스팅에 활용되어, 안정적이고 안전한 클라우드 환경을 제공함으로써 프로젝트의 운영 효율성을 높입니다.

### 6️⃣️ Terraform

- 인프라를 코드로 관리할 수 있게 해 주어, 반복 가능하고 일관된 인프라 구축 및 유지보수가 가능합니다.

### 🔟 K3

- 쿠버네티스 기능을 그대로 유지하면서도 가볍게 설치·운영할 수 있습니다.
- 리소스가 제한된 환경(소규모 서버, 개발용)에 적합합니다.
- 단일 바이너리와 간단한 설정으로 관리가 쉬워 빠른 실험과 배포에 유리합니다.

### 7️⃣ ArgoCD

- Git에 선언된 설정을 자동으로 반영해 배포 과정이 단순하고 일관됩니다.
- 애플리케이션 상태를 실시간으로 확인하고 원하는 시점으로 쉽게 되돌릴 수 있습니다.
- 

### 8️⃣ PineCone

- 벡터 데이터베이스로 유사한 정보를 빠르게 검색할 수 있습니다.
- API 기반으로 쉽게 데이터를 넣고 검색할 수 있어 개발 생산성이 높습니다.
- 추천 시스템이나 검색 서비스에 바로 활용할 수 있습니다.

### 9️⃣ Sentry

- 벡터 데이터베이스로 유사한 정보를 빠르게 검색할 수 있습니다.
- API 기반으로 쉽게 데이터를 넣고 검색할 수 있어 개발 생산성이 높습니다.
- 추천 시스템이나 검색 서비스에 바로 활용할 수 있습니다.


<br></br>

## 💬 Convention

**commit convention** <br>
`#이슈번호 conventionType: 구현한 내용` <br><br>


**convention Type** <br>
| convention type | description |
| --- | --- |
| `feat` | 새로운 기능 구현 |
| `chore` | 부수적인 코드 수정 및 기타 변경사항 |
| `docs` | 문서 추가 및 수정, 삭제 |
| `fix` | 버그 수정 |
| `test` | 테스트 코드 추가 및 수정, 삭제 |
| `refactor` | 코드 리팩토링 |

<br></br>

## 🪵 Branch
### 
- `컨벤션명/#이슈번호-작업내용`
- pull request를 통해 develop branch에 merge 후, branch delete
- 부득이하게 develop branch에 직접 commit 해야 할 경우, `!hotfix:` 사용

<br></br>

## 📁 Directory

```PlainText
src/
├── main/
│   ├── domain/
│   │   ├── entity/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   └── dto/
            ├── request/
            └── response/
│   ├── global/
│   │   ├── apiPayload/
│   │   ├── config/
│   │   ├── security/
		 
```

