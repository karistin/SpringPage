# SpringPage

    https://wikidocs.net/book/7601     

## Spring 예제 만들기 
- 페이징 및 스프링 시큐어리티 활용하기 
- JPA + MYSQL
- thymeleaf

## 엔티티

질문(question) 엔티티의 속성이다. 

| 속성명     | 설명         |
|---------|------------|
| id      | 질문의 고유번호   |
| subject | 질문의 제목     |
| content | 질문의 내용     |
| create_date | 질문을 작성한 일시 |

이의 답변(Answer)에 대한 속성 

| 속성명      | 설명         |
|----------|------------|
| id       | 답변의 고유번호   |
| question | 질문         |
| content  | 답변의 내용     |
| create_date  | 답변을 작성한 일시 |


## JPA의 객채와 테이블 매핑 

## `@Entity `
- DB 테이블에 대응하는 하나의 클래스
- JPA가 관리해주면 JPA를 사용하여 DB테이블과 매핑이 된다.
- 주의 사항 
  - public 또는 protected인 기본 생성자 필요
  - final, enum, interface, innner 클래스에는 사용 불가
  - 저장하려는 속성은 final이 있으면 않된다. 
- 속성 
  - name : JPA에서 사용할 엔티티의 이름을 지정한다. 보통 기본값인 클래스
  이름으로 한다. 

https://data-make.tistory.com/610

## `@Table`
- 엔티티와 매핑할 테이블을 지정한다. 
- Name : 매핑할 테이블 이름 (default. 엔티티 이름 사용)
- 속성 
  - Catalog : catalog 기능이 있는 DB에서 catalog 를 매핑 (default. DB 명)
  - Schema : schema 기능이 있는 DB에서 schema를 매핑 
  - uniqueConstraints : DDL 생성 시 유니크 제약조건을 만듦 
  - 스키마 자동 생성 기능을 사용해서 DDL을 만들 때만 사용