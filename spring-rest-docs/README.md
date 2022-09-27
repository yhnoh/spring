
### Spring Rest Docs

---

- 필요성
  - 프론트 개발자와 백엔드 개발자와의 소통을 하기 위해서는 **API 스펙이 정의된 문서가 필요**하다.
  - 하지만 문서를 수동으로 제작할 경우 고려해할 것이 많아진다.
    - **문서 작성을 위한 form**
    - **API가 수정되었을 경우 문서를 수정하지 않는 사람들**
  - Spring Rest Docs는 위와 같은 문제를 해결하기 위한 좋은 도구이다. 
<br/><br/>
- 장점
  - 코드만 적성하면 **문서를 자동으로 만들어준다.**
  - **프로덕션 코드에 추가적인 코드를 작성할 필요 없다.**
    - Swagger를 사용하면 Contrller 코드에서 가독성이 떨어지는 경우가 많다.
    - Sptring Rest Docs는 테스트 코드를 기반으로 문서가 만들어지기 때문에 프로덕션 코드의 가독성을 크게 신경 쓰지 않아도 된다.
  - 테스트 코드를 기반으로 문서 작성이 이루어짐으로 **테스트를 실패하면 문서가 만들어지지 않는다.**
    - 만들어진 API 문서에서 요청과 응답 스펙이 누락될 일이 없다.
<br/><br/>
- 단점
  - Spring Rest Docs를 구성하는 과정이 그리 간단하지는 않다.
  - Spring Rest Docs를 작성하는 코드 또한 그리 간단하지 않다.
    - 특히 Controller Layer 테스트와 HTTP에 대한 이해도가 없으면...

### Spring Rest Docs 만들어지는 과정

---

2. RestDocs 기반 테스트 코드 작성
3. 테스트 실행시 Asiidoctor 파일 빌드 폴더에 생성
4. 생성된 Asiidoctor 파일 기반으로 API 스펙 문서 생성
5. 빌드 이후 HTML, Markdown 파일로 자동 생성   
6. 사용자에게 배포



> **Reference**
> - [Spring Rest Docs 공식 레퍼런스](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#getting-started)  
> - [우아한 형제들 Spring Rest Docs 적용](https://techblog.woowahan.com/2597/)   
> - [Spring REST Docs 적용 및 최적화 하기](https://backtony.github.io/spring/2021-10-15-spring-test-3/)
> - [Spring rest docs 적용기(gradle 7.0.2)](https://velog.io/@max9106/Spring-Spring-rest-docs%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EB%AC%B8%EC%84%9C%ED%99%94)
> - [Spring REST Docs에 DTO의 Validation 정보 담기](https://velog.io/@dae-hwa/REST-Docs%EC%97%90-DTO%EC%9D%98-Validation-%EC%A0%95%EB%B3%B4-%EB%8B%B4%EA%B8%B0)
> - [AsciiiDoc 문법](https://docs.netapp.com/ko-kr/contribute/asciidoc_syntax.html)