package com.example.springbatchitemreader.error_handler_item_reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.OnReadError;

@Slf4j
public class SkipRecordItemReaderListener {

    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getReadCount() > 0) {
            return stepExecution.getExitStatus();
        } else {
            return ExitStatus.FAILED;
        }
    }

    @OnReadError
    public void onReadError(Exception e) {
        if (e instanceof Exception) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onReadError exception error");
            log.error(stringBuilder.toString());
        }
    }
}
