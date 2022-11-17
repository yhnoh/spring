package com.example.springbatchjoblauncher.rest_api_job_launcher;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final JobLauncher jobLauncher;

    private final ApplicationContext applicationContext;
    private final JobExplorer jobExplorer;


    /**
     * 일반적으로 배치는 시간이 오래 걸리는 작업들이 많기 때문에 비동기 방식으로 실행해는 것이 적합하며 JobExecution의 Id만 반환한다.
     */
    @PostMapping("/run")
    public ExitStatus runJob(@RequestBody JobLauncherRequest request) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Job job = applicationContext.getBean(request.getName(), Job.class);

        JobParameters jobParameters = new JobParametersBuilder(request.getJobParameters(), jobExplorer)
                .getNextJobParameters(job)
                .toJobParameters();
        /**
         * 스프링 배치는 기본적으로 유일한 JobLauncher 구현체인 SimpleJobLauncher를 제공한다.
         *
         * SimpleJobLauncher는 기본적으로 JobParmeters 조작을 지원하지 않는다.
         * 잡에 JobParametersIncrementer를 사용해야 한다면,
         * 해당 파라미터가 SimpleJobLauncher로 전달되기 전에 적용해야 한다.
         *
         * .getNextJobParameters(job)을 통해서 run.id 증가에 대한 메시지를 전달한다.
         */
        return jobLauncher.run(job, jobParameters)
                .getExitStatus();
    }
}
