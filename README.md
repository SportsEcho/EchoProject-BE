![header](https://capsule-render.vercel.app/api?type=waving&color=timeGradient&text=SportsEcho!!&animation=twinkling&fontSize=45&fontAlignY=40&fontAlign=50&height=250)
## 팀소개
| 이름  | 역할  |Github|블로그|
|-----|-----|---|---|
| 진유록 | 팀장  |<a href="https://github.com/jinyr1128">![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)</a>|https://velog.io/@jinyr1128/
| 정지성 | 부팀장 |<a href="https://github.com/zzzzseong">![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)</a>|https://zzzzseong.tistory.com/
| 김지현 | 팀원|<a href="https://github.com/zomeong">![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)</a>|https://velog.io/@zo_meong/posts
| 문정현 | 팀원|<a href="https://github.com/JungHyunMoon">![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)</a>|https://velog.io/@foqlzm12345/posts


---

## 프로젝트 소개
<br>
이 프로젝트는  축구(EPL), 농구(NBL), 야구(MLB) 종목의 최신 경기 일정 및 결과를 제공하고, 스포츠 관련 상품을 핫딜 형태로 판매하는 웹서비스입니다.<br> 이 서비스는 사용자들에게 경기 정보와 함께 상호 작용의 기회를 제공하며, 대용량 데이터 처리 및 트래픽 대응에 초점을 맞추고 있습니다.

---

## 기술 스택
### Frontend
- React: 사용자 인터페이스 구축을 위한 JavaScript 라이브러리
- JavaScript: 동적 웹 페이지 기능을 구현하는 스크립트 언어
- HTML/CSS: 웹 페이지의 구조와 스타일을 정의
- WebSocket: 실시간 양방향 데이터 전송을 위한 프로토콜
- STOMP: WebSocket을 통해 메시지를 전달하기 위한 프로토콜
- AWS S3: 클라우드 기반 객체 스토리지 서비스
- AWS RDS: 클라우드 기반 관계형 데이터베이스 서비스
- AWS EC2: 클라우드 기반 가상 서버 서비스
- AWS Route53: 클라우드 기반 DNS 서비스

### Backend
-  Java 17: 객체 지향 프로그래밍 언어, 안정적이고 확장 가능한 백엔드 개발을 위함
-  Spring Boot 3.2.1: 빠른 마이크로서비스 개발을 위한 Java 기반 프레임워크
-  Spring Security 6: 애플리케이션 보안 (인증 및 권한 부여)을 위한 프레임워크
-  MySQL 8.2.0: 관계형 데이터베이스 관리 시스템
-  Redis 7.2.3: 고성능 키-값 저장소
- JPA: 자바 ORM 기술로, 객체와 관계형 데이터베이스의 매핑을 위한 프레임워크
- QueryDSL: SQL 쿼리를 자바 코드로 작성할 수 있도록 도와주는 프레임워크
- Lombok: 자바 코드를 간결하게 작성할 수 있도록 도와주는 라이브러리
- JWT: JSON 포맷을 이용하여 사용자에 대한 속성을 저장하는 웹 토큰
- OAuth2: 인증 및 권한 부여를 위한 프로토콜
- Rapid API: 실시간 스포츠 데이터를 제공하는 API
- WebSocket: 실시간 양방향 데이터 전송을 위한 프로토콜
- AWS S3: 클라우드 기반 객체 스토리지 서비스
- AWS RDS: 클라우드 기반 관계형 데이터베이스 서비스
- AWS EC2: 클라우드 기반 가상 서버 서비스
- AWS Route53: 클라우드 기반 DNS 서비스

### DevOps
- Docker 24.0.7: 애플리케이션을 컨테이너화하여 쉽게 배포, 실행할 수 있도록 함
- nginx 1.24.0: 고성능 웹 서버 및 리버스 프록시 서버
- AWS: 클라우드 기반 인프라 서비스
- GitHub Actions: 지속적 통합 및 배포를 위한 워크플로우 자동화 도구
- nGrinder: 성능 테스트를 위한 오픈소스 로드 테스트 도구

### Tools
- IntelliJ: Java 개발을 위한 통합 개발 환경(IDE)
- Git: 소스 코드 버전 관리 시스템
---
## 프로젝트 구조
### 아키텍쳐
![스크린샷 2024-01-15 오후 8.41.27.png](src%2Fmain%2Fresources%2Fstatic%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-01-15%20%EC%98%A4%ED%9B%84%208.41.27.png)
### ERD
![Screenshot 2024-01-10 at 12.13.56 PM.png](src%2Fmain%2Fresources%2Fstatic%2FScreenshot%202024-01-10%20at%2012.13.56%20PM.png)
---

### Code Convention

- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)를 기반으로 작성합니다.
##### 줄 나누기

- 하나의 식이 한 줄에 들어가지 않을 때에는, 다음과 같은 일반적인 원칙들을 따라서 두 줄로 나눈다.
- 콤마 후에 두 줄로 나눈다.
- 연산자(operator) 앞에서 두 줄로 나눈다.
- 레벨이 낮은 원칙보다는 레벨이 높은 원칙에 따라 두 줄로 나눈다.
- 앞줄과 같은 레벨의 식(expression)이 시작되는 새로운 줄은 앞줄과 들여쓰기를 일치시킨다.
- 만약 위의 원칙들이 코드를 더 복잡하게 하거나 오른쪽 끝을 넘어간다면, 대신에 8개의 빈 칸을 사용해 들여쓴다
##### 문(Statements)
- 각각의 줄에는 최대한 하나의 문(statement)만 사용하도록 한다.
-
##### PascalCase

- 첫글자와 이어지는 단어의 첫글자를 대문자로 표기하는 방법
- 예) `GoodPerson`, `MyKakaoCake`, `IAmDeveloper`
- Pascal 이라는 프로그래밍 언어에서 이러한 표기법을 사용해서 유명해진 방식이야.
- **PascalCase** (파스칼 케이스)
    - 첫글자와 이어지는 단어의 첫글자를 대문자로 표기하는 방법
    - 예) `GoodPerson`, `MyKakaoCake`, `IAmDeveloper`
    - Pascal 이라는 프로그래밍 언어에서 이러한 표기법을 사용해서 유명해진 방식이야.

##### 공백(Blank Spaces)

- 공백은 다음과 같은 경우에 사용한다.

- 괄호와 함께 나타나는 키워드는 공백으로 나누어야 한다.
- 메서드 이름과 메서드의 여는 괄호 사이에 공백이 사용되어서는 안 된다는 것을 명심하자. 이렇게 하는 것은 메서드 호출과 키워드를 구별하는데 도움을 준다.
- 공백은 인자(argument) 리스트에서 콤마 이후에 나타나야 한다.
- .을 제외한 모든 이항(binary) 연산자는 연산수들과는 공백으로 분리되어져야 한다. 공백은 단항 연산자(++ 혹은 –)의 경우에는 사용해서는 안 된다.
## **camelCase**

(카멜 케이스)

- 첫단어는 소문자로 표기하지만, 이어지는 단어의 첫글자는 대문자로 표기하는 방법
- 예) `goodPerson`, `myKakaoCake`, `iAmDeveloper`

## 디렉토리 구조

도메인 - controller, dto, service, repository, entity,
Global,
Common

##### RequestDto 작성 양식
```java
@Getter
@NoArgsConstructor
public RequestDto {
  //...
}
```
###### ResponseDto 작성 양식
```java
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public ResponseDto {
    //...
    }
```

---

## 주요 기능

### 스포츠 경기 일정 및 결과 서비스
-  기능 설명: 사용자는 축구, 농구, 야구 경기의 일정과 결과를 확인할 수 있습니다. 이 데이터는 Rapid API를 통해 가져오며, DB에 저장됩니다.
-   데이터 업데이트: Scheduler를 활용해 15분마다 경기 결과 데이터를 업데이트합니다. 이는 Timezone 및 API 호출 제한을 고려한 결정입니다.
-   스케일 아웃: 서버의 확장성을 고려하여, 필요시 Scheduler를 별도의 서비스로 분리하여 이벤트를 처리할 수 있도록 계획하였습니다.
-   사용자 상호 작용: 각 경기 일정에 대해 사용자가 댓글을 달 수 있는 기능을 제공합니다.
### 핫딜 및 상품 구매 서비스
-  상품 제공: 스포츠 유니폼, 사인볼, 기념품 등 다양한 상품을 판매합니다.
-   기능: 상품 검색, 리뷰 확인, 장바구니 기능을 포함합니다.
- 특별 행사 및 할인: 사용자에게 최상의 거래를 제공하기 위해 특별 행사 및 할인 정보를 제공합니다.
-  성능 최적화: Redis Sorted Set을 사용해 대용량 트래픽 처리를 계획하고 있으며, 동시성 문제는 비관적 락을 통해 해결할 예정입니다.
### 응원 댓글 작성 서비스

WebSocket방식 : 사용자는 각 경기에 대해 응원 댓글을 작성할 수 있으며, 댓글은 WebSocket 방식을 통해 실시간으로 갱신됩니다.

### 대용량 데이터 처리 및 트래픽 대응
- 데이터베이스 설계: 경기 일정, 결과, 사용자 댓글 등의 대용량 데이터를 효율적으로 처리하기 위해 최적화된 데이터베이스 설계를 적용합니다.
- 최적화 전략: 데이터베이스 쿼리 최적화, 캐싱 전략, 데이터 파티셔닝을 통해 빠른 데이터 처리 속도를 보장합니다.
- 스케일링 및 로드 밸런싱: 스포츠 이벤트 중 발생하는 급격한 트래픽 증가에 대응하기 위해 확장 가능한 서버 구조를 설계하고, 로드 밸런싱 및 오토 스케일링 전략을 채택합니다.
- 성능 테스트: nGrinder를 이용한 부하테스트 및 최적화를 통해 100~300만 건의 댓글 데이터 처리를 위한 성능을 확보합니다.
---

## 성능 최적화 및 부하 테스트
경기 일정 및 결과 데이터에 대한 데이터베이스 쿼리 최적화<br>
nGrinder를 이용한 성능 테스트 및 최적화 수행<br>
Redis와 JPA를 사용하여 대용량 트래픽에 대한 정합성 및 동시성 처리

---

## 기여 방법
이 부분은 저희의 코드에 도움을 주시고자 하는 분들이 있을까 하여 만들어 둡니다!
<br>혹시 이 코드를 보고 싶어하시는 분들이 계시다면, 아래의 방법으로 저희의 코드를 확인하실 수 있습니다.
<br>또한, 이 코드에 대해 궁금하신 점이 있으시다면, 언제든지 이슈를 남겨주세요!
<br>저희가 확인 후 답변드리겠습니다.
<br>감사합니다!

1. 프로젝트 포크하기<br><br>
2. 로컬 환경에 클론하기
   포크한 저장소를 자신의 로컬 환경으로 클론(clone)합니다.
```bash
git clone [your-forked-repository-url]
```
3.브랜치 생성하기
작업할 새로운 브랜치(branch)를 생성합니다. 브랜치 이름은 작업 내용을 명확하게 표현하는 것이 좋습니다.
```sql
git checkout -b [new-branch-name]
```
4. 변경 사항 작업하기
   로컬 환경에서 변경 사항을 작업한 후에 변경 사항을 커밋(commit)합니다.
```sql
git commit -m "설명적인 커밋 메시지"
```
5. 변경 사항을 리모트 저장소에 푸시하기
```sql
git push origin [new-branch-name]
```
6. Pull Request 생성하기<br>
   프로젝트 원본 저장소에 변경 사항을 반영하기 위해 Pull Request를 생성합니다.<br><br>
7. 코드 리뷰 후 Merge<br>
   Pull Request를 통해 코드 리뷰를 진행하고, 변경 사항을 원본 저장소에 반영합니다.

---

