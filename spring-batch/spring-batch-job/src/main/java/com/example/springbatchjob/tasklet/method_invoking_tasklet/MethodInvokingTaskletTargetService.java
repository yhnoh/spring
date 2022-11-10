package com.example.springbatchjob.tasklet.method_invoking_tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MethodInvokingTaskletTargetService {

    public void targetTasklet(){
        log.info("MethodInvokingTasklet running");
    }
}
