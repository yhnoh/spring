### Spring Batch 무중단 배포
- Spring Batch는 스케쥴러를 통해서 서버 내에 있는 jar파일을 통해서 Job 실행되는 경우가 많다.
  - 물론 Spring Web 과 batch를 함께 사용해서 요청하는 형식도 있을 수 도 있다.
- 스케쥴러가 1분 단위로도 Batch를 실행시킬 수 있기 때문에, 거의 24시간 멈추지않고 실행하고 있다 해도 무방하다.
  - 특히 해당 배포를 여러 사람이 하는 경우에는 더욱 문제가 발생할 수 있다.
    - Jenkins Pipeline을 통해서 Job을 실행시킬 대  
  - 때문에 배포에 대한  
- 



> https://taetaetae.github.io/2019/10/13/batch-nondisruptive-deploy/
> https://www.jigi.net/os/6006/
> 