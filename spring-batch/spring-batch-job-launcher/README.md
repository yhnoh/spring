### Spring Batch Multiple Job
- 스프링 배치 2.X 버전대에서는 `spring.batch.job.name`에서는, 쉼표로 구분하여 Multiple Job을 실행시킬 수 있다.
    > java -jar --job.names=helloJob2,helloJob1
- 하지만 위와 같은 방식으로 여러 잡을 실행시켰을 때 위 순서대로 잡이 실행하기를 예상했지만, 실제로는 그렇지 않은 문제가 발생하였다.
- 
- 위의 문제를 해결하기 위하여 `JobLauncherApplicationRunner`를 재가공하였다
- 왜 해당 클래스를 재가공할 수 밖에 , 그리고 왜 `JobLauncherApplicationRunner` 클래스를 재가공하여 사용할 수 밖에 없었는지에 대하여 한번 살펴보자.

> 여담이지만 스프링 배치 3.X 버전부터는 Multiple Job을 지원하지 않는다.
> [스프링 배치 3.0 마이그레이션 가이드](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
    > Running multiple batch jobs is no longer supported.
    > If the auto-configuration detects a single job is, it will be executed on startup.
    > If multiple jobs are found in the context, a job name to execute on startup must be supplied by the user using the spring.batch.job.name property.

    