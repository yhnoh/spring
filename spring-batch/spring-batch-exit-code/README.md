```text
//...
2023-10-07 16:49:56.235  INFO 12305 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=failedJob]] launched with the following parameters: [{}]
2023-10-07 16:49:56.259  INFO 12305 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [failedStep]
2023-10-07 16:49:56.270 ERROR 12305 --- [           main] o.s.batch.core.step.AbstractStep         : Encountered an error executing step failedStep in job failedJob

java.lang.RuntimeException: 실행 도중 에러 발생
//....

2023-10-07 16:49:56.274  INFO 12305 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [failedStep] executed in 14ms
2023-10-07 16:49:56.279  INFO 12305 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=failedJob]] completed with the following parameters: [{}] and the following status: [FAILED] in 31ms
2023-10-07 16:49:56.286  INFO 12305 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2023-10-07 16:49:56.292  INFO 12305 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.

Process finished with exit code 0
```

- job이 실패해도 exit code는 정상