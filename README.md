# SpringPage

    https://wikidocs.net/book/7601     

## Spring 예제 만들기 
- 페이징 및 스프링 시큐어리티 활용하기 
- JPA + MYSQL
- thymeleaf

# Spring 

## Controller
- `@ControllerAdvice`
  - ExceptionManager 
```java
@ControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<?> DataExceptionHandler(DataNotFoundException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(e.getErrorCode().name() + " " + e.getMessage());
    }
}
```
- `@PathVariable`
  - 요청 uri의 특정 값  
```java
 public class QuestionController {
  @GetMapping(value = "/question/detail/{id}")
  public String detail(Model model, @PathVariable("id") Integer id) {
    return "question_detail";
  }
}
```
- `@RequestParam`
  - Post 요청의 Body 
  - Json, String 등 다양한 종류로 가능하다.

## Validation

| 속성명     | 설명         |
|---------|------------|
|@Size	|문자 길이를 제한한다.|
|@NotNull |	Null을 허용하지 않는다.|
|@NotEmpty |	Null 또는 빈 문자열("")을 허용하지 않는다.|
|@Past |	과거 날짜만 가능|
|@Future |	미래 날짜만 가능 |
|@FutureOrPresent |	 미래 또는 오늘날짜만 가능|
|@Max |	 최대값|
|@Min |	최소값|
|@Pattern |	정규식으로 검증|

```java
public class QuestionForm {
    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=200)
    private String subject;

    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;
}
```
```java
public class QuestionController {
    
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        this.questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";
    }
}

```
- @Valid : QuestionForm의 @NotEmpty, @Size 등으로 설정한 검증 기능이 동작한다.
- BindingResult 매개변수는 @Valid 애너테이션으로 인해 검증이 수행된 결과를 의미하는 객체이다.
> .callout_info
> 
> BindingResult 매개변수는 항상 @Valid 매개변수 바로 뒤에 위치해야 한다. 만약 2개의 매개변수의 위치가 정확하지 않다면 @Valid만 적용이 되어 입력값 검증 실패 시 400 오류가 발생한다.
- 이후 bindResult.hasErrors()를 호출하여 오류가 있는 경우 다시 작성하도록 했다. 
```html
<form th:action="@{/question/create}" th:object="${questionForm}" method="post">
    <div class="alert alert-danger" role="alert" th:if="${#fields.hasAnyErrors()}">
        <div th:each="err : ${#fields.allErrors()}" th:text="${err}" />
    </div>
</form>
```

# 엔티티

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
- 객체와 테이블 매핑 : @Entity, @Table 
- 기본 키 매핑 : @Id 
- 필드와 컬럼 매핑 : @Column 
- 연관관계 매핑 : @ManyToOne, @JoinColumn
- Code Ex
```java
@Entity
// name과 age Column에 unique 제약조건 추가
@Table(name="MEMBER", uniqueConstraints = {@UniqueConstraint(
        name = "NAME_AGE_UNIQUE",
        columnNames = {"NAME", "AGE"} )})
public class Member {
 
    // 기본키 매핑
    @Id
    @Column(name = "ID")
    private String id;
 
    // not null, varchar(10)
    @Column(name = "NAME", nullable = false, length = 10)
    private String username;
 
    private Integer age;
 
    // Java의 enum 사용
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
 
    // Java의 날짜 타입
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
 
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
 
    // CLOB, BLOC 타입 매핑
    @Lob
    private String description;
 
    @Transient
    private String temp;
 
 
    //Getter, Setter
}
 
public enum RoleType {
    ADMIN, USER
}

```

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
  - 유니크 제약 조건
    - NOT NULL : 필수 입력 사항
    - UNIQUE : CONSTRANINTS 제약명 UNIQUE (컬럼2, 컬럼3), 컬럼2, 컬럼3의 조합이 유일해야 한다.
    - PRIMARY KEY :  CONSTRAINTS 기본키 이름 PRIMARY KEY (컬럼1)); 유일성 + 최소성 
    - FOREIGN KEY :  다른 테이블과 연관관계가 있는 키로 설정 
    - CHECK : 조건에 부합하는 데이터만 가능하다.
  - 스키마 자동 생성 기능을 사용해서 DDL을 만들 때만 사용

## 데이터 베이스 스키마 생성
- hibernate.hbm2ddl.auto 속성
- create : 기존 테이블을 삭제하고 새로 생성(DROP + CREATE)
- create-drop : CREATE 속성에 추가로 애플리케이션을 종료할 때 생성한 DDL을 제거 (DROP + CREATE + DROP)
- update : DB 테이블과 엔티티 매핑 정보를 비교해서 변경 사항만 수정 
- validate : DB 테이블과 엔티티 매핑정보를 비교해서 차이가 있으면 경고를 남기고 애플리케이션을 실행하지 않음. 
DDL을 수행하지 않음 
- none : 자동 생성 기능을 사용하지 않음
- 주의사항 
  - 개발 초기 단계는 create 또는 update
  - 초기화 상태로 자동화된 테스트를 진행하는 개발자 환경과 CI서버는 create 또는 create-drop
  - 테스트 서버는 update 또는 validate
  - 스테이징과 운영 서버는 validate 또는 none

## `@OneToMany`, `@ManyToOne`
- 1:N, N:1 제작시 사용
- mappedBy  : 연결하는 테이블
- cascade : 질문을 삭제하면 그에 달린 답변들도 모두 함께 삭제하기 위해서 @OneToMany의 속성으로 
cascade = CascadeType.REMOVE

# thymeleaf
- 기본적인 thymeleaf 사용법 
```java
@RequiredArgsConstructor
@Controller
public class QuestionController {

  private final QuestionRepository questionRepository;

  @GetMapping("/question/list")
  public String list(Model model) {
    List<Question> questionList = this.questionRepository.findAll();
    model.addAttribute("questionList", questionList);
    return "question_list";
  }
}
```
```html
model.addAttribute("questionList", questionList);
<table>
    <thead>
        <tr>
            <th>제목</th>
            <th>작성일시</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="question : ${questionList}">
            <td th:text="${question.subject}"></td>
            <td th:text="${question.createDate}"></td>
        </tr>
    </tbody>
</table>
```
- 주로 사용하는 속성
  - 조건문 설정
    - th:if="${question != null}" question이 null이 아닌 경우 표시된다.
  - 반복문 설정
    - th:each="question : ${questionList}" 
    - th:each="question, loop :${questionList}"
      - loop.index = 반복 순서 0부터 1씩 증가
      - loop.count = 반복 순서 1부터 1씩 증가
      - loop.size = 개수
      - loop.first - 루프의 첫번째 순서인 경우 true
      - loop.last - 루프의 마지막 순서인 경우 true
      - loop.odd - 루프의 홀수번째 순서인 경우 true
      - loop.even - 루프의 짝수번째 순서인 경우 true
      - loop.current - 현재 대입된 객체 (예: 위의 경우 question과 동일)
  - 텍스트 설정 
    - th:text="${question.subject}" 텍스트로 출력 
```html
  <tr th:each="question : ${questionList}">
    <td>[[${question.subject}]]</td>
    <td>[[${question.createDate}]]</td>
  </tr>
```
  - 문자열 연결 
    ```html<a th:href="@{|/question/detail/${question.id}|}" th:text="${question.subject}"></a>```
    - |기호로 감싸 주어야 한다.
  - 날짜 형식
    - #temporals.format(날짜객체, 날짜포맷) - 날짜객체를 날짜포맷에 맞게 변환한다.
```html
<td th:text="${#temporals.format(question.createDate, 'yyyy-MM-dd HH:mm')}"></td>
```
  - 템플릿 상속 
    - Html 표준등 내용을 상속해서 중복되는 내용을 줄일 수 있다.
```html
<!-- 기본 템플릿 안에 삽입될 내용 Start -->
<th:block layout:fragment="content"></th:block>
<!-- 기본 템플릿 안에 삽입될 내용 End -->
```
```html
<html layout:decorate="~{layout}">
<div layout:fragment="content" class="container my-3">
    <table class="table">
        (... 생략 ...)
    </table>
</div>
</html>
```
  - 링크 
    - 특정 URL로 연결
```html
<a th:href="@{/question/create}" class="btn btn-primary">질문 등록하기</a>
```
  - th:Object
    - from 전송시 특정 객체를 연결 시켜 준다.
    - 이외에는 $.ajax를 이용해서 서버에 전달할 수도 있다. 
    - `th:field="*{Object.object}"` 해당 객체의 속성에 값을 넣을수 있다.
```html
<form th:action="@{/question/create}" th:object="${questionForm}" method="post">
  <div class="alert alert-danger" role="alert" th:if="${#fields.hasAnyErrors()}">
      <div th:each="err : ${#fields.allErrors()}" th:text="${err}" />
  </div>
</form>
```
# URL
- redirect:<URL> - URL로 리다이렉트 (리다이렉트는 완전히 새로운 URL로 요청이 된다.)
  - 클라이언트에 의해 서버의 DB에 변화가 생기는 경우 사용자가 다시 HTTP 요청을 보내도록 한다.
  - DB의 유저 테이블을 변경하는 회원가입과 같은 경우에는 리다이렉트가 사용되어야 요청을 중복해서 보내는 것을 방지할 수 있다.
- forward:<URL> - URL로 포워드 (포워드는 기존 요청 값들이 유지된 상태로 URL이 전환된다.)
  - 서버 내부에서 일어나는 호출로 같은 URL로 정보를 보낸다. 
  - 포워드는 특정 URL에 대해 외부에 공개되지 말아야 하는 부분을 가리는데 사용하거나 조회를 위해 사용된다.
    - 스프링의 경우 /WEB-INF에 있는 view에 대한 정보들이 외부에 직접 공개되지 말아야 할 때 내부에서 포워딩을 통해 
/WEB-INF 경로를 가리키도록 한다. 예를 들어 lucas.com/95 로 요청을 하면  lucas.com/WEB-INF/95를 응답하는 형식이다.